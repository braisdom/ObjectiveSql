package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.*;
import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import lombok.AccessLevel;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.HandleGetter;
import lombok.javac.handlers.HandleSetter;
import org.kohsuke.MetaInfServices;

import java.sql.SQLException;

import static com.github.braisdom.funcsql.util.StringUtil.splitNameOf;
import static lombok.javac.handlers.JavacHandlerUtil.*;

@MetaInfServices(JavacAnnotationHandler.class)
public class DomainModelCodeGenerator extends JavacAnnotationHandler<DomainModel> {

    @Override
    public void handle(AnnotationValues<DomainModel> annotationValues, JCAnnotation jcAnnotation, JavacNode javacNode) {
        JavacNode typeNode = javacNode.up();
        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
        DomainModel domainModel = annotationValues.getInstance();

        JCAnnotation annotation = treeMaker.Annotation(
                chainDots(typeNode, splitNameOf(PrimaryKey.class)),
                List.of(treeMaker.Assign(treeMaker.Ident(typeNode.toName("name")),
                        treeMaker.Literal(domainModel.primaryColumnName()))));

        JCVariableDecl fieldDecl = FieldBuilder.newField()
                .ofType(domainModel.primaryClass())
                .withModifiers(Flags.PRIVATE)
                .withAnnotations(annotation)
                .withName(domainModel.primaryFieldName())
                .buildWith(typeNode);

        JavacNode fieldNode = new JavacNode(javacNode.getAst(), fieldDecl, null, AST.Kind.FIELD) {
            @Override
            public JavacNode up() {
                return typeNode;
            }
        };

        new HandleGetter().generateGetterForField(fieldNode,null, AccessLevel.PUBLIC, false);
        new HandleSetter().generateSetterForField(fieldNode, typeNode, AccessLevel.PUBLIC);

        JCMethodDecl createPersistenceMethod = createPersistenceMethod(treeMaker, typeNode);
        JCMethodDecl createQueryMethod = createQueryMethod(treeMaker, typeNode);
        JCMethodDecl saveMethod = createSaveMethod(treeMaker, typeNode);

        injectMethod(typeNode, createPersistenceMethod);
        injectMethod(typeNode, createQueryMethod);
        injectMethod(typeNode, saveMethod);

        injectField(typeNode, fieldDecl);
    }

    private JCTree.JCMethodDecl createSaveMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        JCVariableDecl parameter = FieldBuilder.newField()
                .ofType(Boolean.class)
                .withName("skipValidation")
                .withModifiers(Flags.PARAMETER)
                .buildWith(typeNode);

        jcStatements.append(
                treeMaker.VarDef(
                        treeMaker.Modifiers(Flags.PARAMETER),
                        typeNode.toName("persistence"),
                        chainDots(typeNode, splitNameOf(Persistence.class)),
                        treeMaker.Apply(
                                List.nil(),
                                treeMaker.Select(
                                        treeMaker.Ident(typeNode.toName("this")),
                                        typeNode.toName("createPersistence")
                                ), List.nil()
                        )
                )
        );

        jcStatements.append(
                treeMaker.Exec(
                        treeMaker.Apply(
                                List.nil(),
                                treeMaker.Select(
                                        treeMaker.Ident(typeNode.toName("persistence")),
                                        typeNode.toName("save")
                                )
                                , List.of(
                                        treeMaker.Ident(typeNode.toName("this")),
                                        treeMaker.Ident(typeNode.toName("skipValidation"))
                                )
                        ))
        );

        return MethodBuilder.newMethod()
                .withModifiers(Flags.PUBLIC | Flags.FINAL)
                .withName("save")
                .withParameters(List.of(parameter))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .withThrowsClauses(List.of(
                        treeMaker.Throw(chainDots(typeNode, splitNameOf(SQLException.class))).getExpression(),
                        treeMaker.Throw(chainDots(typeNode, splitNameOf(PersistenceException.class))).getExpression()
                ))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl createQueryMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();

        Name queryFactoryName = typeNode.toName("queryFactory");
        JCExpression queryFactoryRef = chainDots(typeNode, splitNameOf(QueryFactory.class));
        JCExpression getQueryFactoryInv = treeMaker.Select(chainDots(typeNode, splitNameOf(Database.class)),
                typeNode.toName("getQueryFactory"));

        JCExpression createQueryFactoryInv = treeMaker.Select(
                treeMaker.Ident(typeNode.toName(typeNode.getName())),typeNode.toName("class"));
        JCExpression createQueryInv = treeMaker.Select(
                treeMaker.Ident(typeNode.toName("queryFactory")), typeNode.toName("createQuery"));

        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), queryFactoryName,
                queryFactoryRef, treeMaker.Apply(List.nil(), getQueryFactoryInv, List.nil())));
        jcStatements.append(treeMaker.Return(treeMaker.Apply(List.nil(), createQueryInv, List.of(createQueryFactoryInv))));

        return MethodBuilder.newMethod()
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("createQuery")
                .withReturnType(chainDots(typeNode, splitNameOf(Query.class)))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl createPersistenceMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();

        Name persistenceFactoryName = typeNode.toName("persistenceFactory");
        JCExpression persistenceFactoryRef = chainDots(typeNode, splitNameOf(PersistenceFactory.class));
        JCExpression getPersistenceFactoryInv = treeMaker.Select(
                chainDots(typeNode, splitNameOf(Database.class)), typeNode.toName("getPersistenceFactory"));

        JCExpression createPersistenceInv = treeMaker.Select(
                treeMaker.Ident(typeNode.toName("persistenceFactory")), typeNode.toName("createPersistence"));
        JCExpression modelClassRef = treeMaker.Select(treeMaker.Ident(typeNode.toName(typeNode.getName())), typeNode.toName("class"));

        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), persistenceFactoryName,
                persistenceFactoryRef, treeMaker.Apply(List.nil(), getPersistenceFactoryInv, List.nil())));
        jcStatements.append(treeMaker.Return(treeMaker.Apply(List.nil(), createPersistenceInv, List.of(modelClassRef))));

        return MethodBuilder.newMethod()
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("createPersistence")
                .withReturnType(chainDots(typeNode, splitNameOf(Persistence.class)))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .buildWith(typeNode);
    }
}
