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
        HandleGetter handleGetter = new HandleGetter();
        HandleSetter handleSetter = new HandleSetter();
        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
        DomainModel domainModel = annotationValues.getInstance();

        JCAnnotation annotation = treeMaker.Annotation(
                chainDots(typeNode, splitNameOf(PrimaryKey.class)),
                List.of(treeMaker.Assign(treeMaker.Ident(typeNode.toName("name")),
                        treeMaker.Literal(domainModel.primaryColumnName()))));

        JCVariableDecl iDFieldDecl = FieldBuilder.newField(typeNode)
                .ofType(domainModel.primaryClass())
                .withModifiers(Flags.PRIVATE)
                .withAnnotations(annotation)
                .withName(domainModel.primaryFieldName())
                .build();

        JavacNode fieldNode = new JavacNode(javacNode.getAst(), iDFieldDecl, null, AST.Kind.FIELD) {
            @Override
            public JavacNode up() {
                return typeNode;
            }
        };

        handleGetter.generateGetterForField(fieldNode, null, AccessLevel.PUBLIC, false);
        handleSetter.generateSetterForField(fieldNode, typeNode, AccessLevel.PUBLIC);

        JCMethodDecl createPersistenceMethod = handleCreatePersistenceMethod(treeMaker, typeNode);
        JCMethodDecl createQueryMethod = handleCreateQueryMethod(treeMaker, typeNode);
        JCMethodDecl saveMethod = handleCreateSaveMethod(treeMaker, typeNode);
        JCMethodDecl save2Method = handleCreateSave2Method(treeMaker, typeNode);

        generateFieldSG(typeNode, handleGetter, handleSetter);

        injectMethod(typeNode, createPersistenceMethod);
        injectMethod(typeNode, createQueryMethod);
        injectMethod(typeNode, save2Method);
        injectMethod(typeNode, saveMethod);

        injectField(typeNode, iDFieldDecl);
    }

    private void generateFieldSG(JavacNode typeNode, HandleGetter handleGetter, HandleSetter handleSetter) {
        for (JavacNode field : typeNode.down()) {
            if (handleGetter.fieldQualifiesForGetterGeneration(field))
                handleGetter.generateGetterForField(field, null, AccessLevel.PUBLIC, false);

            if (field.getKind() != AST.Kind.FIELD) continue;
            JCVariableDecl fieldDecl = (JCVariableDecl) field.get();
            //Skip fields that start with $
            if (fieldDecl.name.toString().startsWith("$")) continue;
            //Skip static fields.
            if ((fieldDecl.mods.flags & Flags.STATIC) != 0) continue;
            //Skip final fields.
            if ((fieldDecl.mods.flags & Flags.FINAL) != 0) continue;

            handleSetter.generateSetterForField(field, typeNode, AccessLevel.PUBLIC);
        }
    }

    private JCTree.JCMethodDecl handleCreateSave2Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        JCTree.JCMethodInvocation thisSaveInv = treeMaker.Apply(List.nil(),
                treeMaker.Select(treeMaker.Ident(typeNode.toName("this")),
                        typeNode.toName("save")), List.of(treeMaker.Literal(false)));

        jcStatements.append(treeMaker.Exec(thisSaveInv));

        List<JCExpression> exceptions = List.of(treeMaker.Throw(chainDots(typeNode, splitNameOf(SQLException.class))).getExpression(),
                treeMaker.Throw(chainDots(typeNode, splitNameOf(PersistenceException.class))).getExpression());

        return MethodBuilder.newMethod()
                .withModifiers(Flags.PUBLIC | Flags.FINAL)
                .withName("save")
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .withThrowsClauses(exceptions)
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleCreateSaveMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        JCVariableDecl parameter = FieldBuilder.newField(typeNode)
                .ofType(Boolean.class)
                .withName("skipValidation")
                .withModifiers(Flags.PARAMETER)
                .build();
        JCTree.JCMethodInvocation createPersistenceInv = treeMaker.Apply(List.nil(),
                treeMaker.Select(treeMaker.Ident(typeNode.toName("this")),
                        typeNode.toName("createPersistence")), List.nil());
        JCExpression createPersistenceType = chainDots(typeNode, splitNameOf(Persistence.class));

        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER),
                typeNode.toName("persistence"), createPersistenceType, createPersistenceInv));

        JCTree.JCFieldAccess saveAcc = treeMaker.Select(
                treeMaker.Ident(typeNode.toName("persistence")),
                typeNode.toName("save")
        );

        List<JCTree.JCExpression> saveParameters = List.of(treeMaker.Ident(typeNode.toName("this")),
                treeMaker.Ident(typeNode.toName("skipValidation")));

        jcStatements.append(treeMaker.Exec(treeMaker.Apply(List.nil(), saveAcc, saveParameters)));

        List<JCExpression> exceptions = List.of(treeMaker.Throw(chainDots(typeNode, splitNameOf(SQLException.class))).getExpression(),
                treeMaker.Throw(chainDots(typeNode, splitNameOf(PersistenceException.class))).getExpression());

        return MethodBuilder.newMethod()
                .withModifiers(Flags.PUBLIC | Flags.FINAL)
                .withName("save")
                .withParameters(List.of(parameter))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .withThrowsClauses(exceptions)
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleCreateQueryMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();

        Name queryFactoryName = typeNode.toName("queryFactory");
        JCExpression queryFactoryRef = chainDots(typeNode, splitNameOf(QueryFactory.class));
        JCExpression getQueryFactoryInv = treeMaker.Select(chainDots(typeNode, splitNameOf(Database.class)),
                typeNode.toName("getQueryFactory"));

        JCExpression createQueryFactoryInv = treeMaker.Select(
                treeMaker.Ident(typeNode.toName(typeNode.getName())), typeNode.toName("class"));
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

    private JCTree.JCMethodDecl handleCreatePersistenceMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
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

    private JCTree.JCMethodDecl handleCreateMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();

        JCVariableDecl parameter = FieldBuilder.newField(typeNode)
                .ofType(Boolean.class)
                .withName("dirtyObject")
                .withModifiers(Flags.PARAMETER)
                .build();

        Name persistenceFactoryName = typeNode.toName("persistenceFactory");
        JCExpression persistenceFactoryRef = chainDots(typeNode, splitNameOf(PersistenceFactory.class));
        JCExpression getPersistenceFactoryRef = treeMaker.Select(
                chainDots(typeNode, splitNameOf(Database.class)), typeNode.toName("getPersistenceFactory"));

        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), persistenceFactoryName,
                persistenceFactoryRef, treeMaker.Apply(List.nil(), getPersistenceFactoryRef, List.nil())));

        JCExpression createPersistenceRef = treeMaker.Select(
                treeMaker.Ident(typeNode.toName("persistenceFactory")), typeNode.toName("createPersistence"));
        JCExpression modelClassRef = treeMaker.Select(treeMaker.Ident(typeNode.toName(typeNode.getName())), typeNode.toName("class"));

        jcStatements.append(treeMaker.Return(treeMaker.Apply(List.nil(), createPersistenceRef, List.of(modelClassRef))));

        return MethodBuilder.newMethod()
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("createPersistence")
                .withParameters(List.of(parameter))
                .withReturnType(chainDots(typeNode, splitNameOf(Persistence.class)))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .buildWith(typeNode);
    }
}
