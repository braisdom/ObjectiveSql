package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.Database;
import com.github.braisdom.funcsql.Persistence;
import com.github.braisdom.funcsql.PersistenceFactory;
import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import lombok.AccessLevel;
import lombok.core.AST;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;
import lombok.javac.handlers.HandleGetter;
import lombok.javac.handlers.HandleSetter;
import lombok.javac.handlers.JavacHandlerUtil;
import org.mangosdk.spi.ProviderFor;

import static com.github.braisdom.funcsql.util.StringUtil.splitNameOf;
import static lombok.javac.handlers.JavacHandlerUtil.*;

@ProviderFor(JavacAnnotationHandler.class)
public class PrimaryFieldGenerator extends JavacAnnotationHandler<DomainModel> {

    @Override
    public void handle(AnnotationValues<DomainModel> annotationValues, JCTree.JCAnnotation jcAnnotation, JavacNode javacNode) {
        JavacNode typeNode = javacNode.up();
        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
        DomainModel domainModel = annotationValues.getInstance();

        JCTree.JCAnnotation annotation = treeMaker.Annotation(
                chainDots(typeNode, splitNameOf(PrimaryKey.class)),
                List.of(treeMaker.Assign(treeMaker.Ident(typeNode.toName("name")),
                        treeMaker.Literal(domainModel.primaryColumnName()))));

        JCTree.JCVariableDecl fieldDecl = FieldBuilder.newField()
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

        JCTree.JCMethodDecl createPersistenceMethod = createPersistenceMethod(treeMaker, typeNode);

        injectMethod(typeNode, createPersistenceMethod);
        injectField(typeNode, fieldDecl);
    }

    private JCTree.JCMethodDecl createPersistenceMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();

        jcStatements.append(
                treeMaker.VarDef(
                        treeMaker.Modifiers(Flags.PARAMETER),
                        typeNode.toName("persistenceFactory"),
                        chainDots(typeNode, splitNameOf(PersistenceFactory.class)),
                        treeMaker.Apply(
                                List.nil(),
                                treeMaker.Select(
                                        chainDots(typeNode, splitNameOf(Database.class)),
                                        typeNode.toName("getPersistenceFactory")
                                ), List.nil()
                        )
                )
        );

        jcStatements.append(
                treeMaker.Return(
                        treeMaker.Apply(
                                List.nil(),
                                treeMaker.Select(
                                        treeMaker.Ident(typeNode.toName("persistenceFactory")),
                                        typeNode.toName("createPersistence")
                                )
                                , List.of(
                                        treeMaker.Select(
                                                treeMaker.Ident(typeNode.toName(typeNode.getName())),
                                                typeNode.toName("class")
                                        )
                                )
                        ))
        );

        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC + Flags.STATIC + Flags.FINAL),
                typeNode.toName("createPersistence"),
                chainDots(typeNode, splitNameOf(Persistence.class)),
                List.nil(),
                List.nil(),
                List.nil(),
                treeMaker.Block(0, jcStatements.toList()),
                null
        );
    }
}
