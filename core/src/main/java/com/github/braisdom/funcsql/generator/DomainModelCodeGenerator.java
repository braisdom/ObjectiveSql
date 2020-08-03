package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.*;
import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.reflection.ClassUtils;
import com.github.braisdom.funcsql.reflection.PropertyUtils;
import com.github.braisdom.funcsql.util.JCTreeUtil;
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
import java.util.Arrays;
import java.util.Map;

import static com.github.braisdom.funcsql.util.StringUtil.splitNameOf;
import static lombok.javac.Javac.CTC_BOOLEAN;
import static lombok.javac.Javac.CTC_INT;
import static lombok.javac.handlers.JavacHandlerUtil.*;

@MetaInfServices(JavacAnnotationHandler.class)
public class DomainModelCodeGenerator extends JavacAnnotationHandler<DomainModel> {

    @Override
    public void handle(AnnotationValues<DomainModel> annotationValues, JCAnnotation jcAnnotation, JavacNode javacNode) {
        JavacNode typeNode = javacNode.up();
        JCTree.JCClassDecl classDecl = (JCTree.JCClassDecl) typeNode.get();
        HandleGetter handleGetter = new HandleGetter();
        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
        DomainModel domainModel = annotationValues.getInstance();

        if (!domainModel.disableGeneratedId()) {
            JCVariableDecl idFieldDecl = createIdField(treeMaker, typeNode, domainModel);
            JavacNode fieldNode = new JavacNode(javacNode.getAst(), idFieldDecl, null, AST.Kind.FIELD) {
                @Override
                public JavacNode up() {
                    return typeNode;
                }
            };

            injectField(typeNode, idFieldDecl);
            handleGetter.generateGetterForField(fieldNode, null, AccessLevel.PUBLIC, false);
            HandleSetter.createSetter(Flags.PUBLIC, fieldNode, treeMaker, toSetterName(fieldNode), domainModel.fluent(),
                    typeNode, List.nil(), List.nil());
        }

        handleFieldSG(treeMaker, domainModel, typeNode, handleGetter);
        handleTableNameField(treeMaker, typeNode);

        JCMethodDecl[] methodDeclArray = new JCMethodDecl[]{
                handleCreatePersistenceMethod(treeMaker, typeNode),
                handleCreateQueryMethod(treeMaker, typeNode),
                handleSaveMethod(treeMaker, typeNode),
                handleSave2Method(treeMaker, typeNode),
                handleCreateMethod(treeMaker, typeNode),
                handleCreate2Method(treeMaker, typeNode),
                handleCreateArrayMethod(treeMaker, typeNode),
                handleCreateArray2Method(treeMaker, typeNode),
                handleUpdateMethod(treeMaker, typeNode),
                handleUpdate2Method(treeMaker, typeNode),
                handleUpdate3Method(treeMaker, typeNode),
                handleDestroyMethod(treeMaker, typeNode),
                handleDestroy2Method(treeMaker, typeNode),
                handleExecuteMethod(treeMaker, typeNode),
                handleNewInstanceFromMethod(treeMaker, typeNode),
                handleNewInstanceFrom2Method(treeMaker, typeNode),
                handleQueryMethod(treeMaker, typeNode)
        };

        Arrays.stream(methodDeclArray).forEach(methodDecl -> {
            if (!JCTreeUtil.containsMethod(classDecl.sym, methodDecl, false))
                injectMethod(typeNode, methodDecl);
        });
    }

    private void handleFieldSG(JavacTreeMaker treeMaker, DomainModel domainModel, JavacNode typeNode, HandleGetter handleGetter) {
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

            injectMethod(typeNode, HandleSetter.createSetter(Flags.PUBLIC, field, treeMaker, toSetterName(field), domainModel.fluent(),
                    typeNode, List.nil(), List.nil()));
        }
    }

    private JCTree.JCMethodDecl handleSave2Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        JCTree.JCMethodInvocation thisSaveInv = treeMaker.Apply(List.nil(),
                treeMaker.Select(treeMaker.Ident(typeNode.toName("this")),
                        typeNode.toName("save")), List.of(treeMaker.Literal(false)));

        jcStatements.append(treeMaker.Exec(thisSaveInv));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.FINAL)
                .withName("save")
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .withThrowsClauses(createPersistenceExceptions(treeMaker, typeNode))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleSaveMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
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

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.FINAL)
                .withName("save")
                .withParameters(List.of(parameter))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .withThrowsClauses(createPersistenceExceptions(treeMaker, typeNode))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleCreateQueryMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        Name modelClassName = typeNode.toName(typeNode.getName());
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

        JCExpression returnType = treeMaker.TypeApply(genTypeRef(typeNode, Query.class.getName()),
                List.of(treeMaker.Ident(modelClassName)));
        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("createQuery")
                .withReturnType(returnType)
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

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("createPersistence")
                .withReturnType(chainDots(typeNode, splitNameOf(Persistence.class)))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleCreateMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        Name modelClassName = typeNode.toName(typeNode.getName());
        JCVariableDecl dirtyObjectVar = createParameter(typeNode, treeMaker.Ident(modelClassName), "dirtyObject");
        JCVariableDecl skipValidationVar = createParameter(typeNode, treeMaker.TypeIdent(CTC_BOOLEAN), "skipValidation");

        addPersistenceRefStatement(treeMaker, typeNode, jcStatements);

        JCExpression insertRef = treeMaker.Select(treeMaker.Ident(typeNode.toName("persistence")), typeNode.toName("insert"));
        JCExpression skipValidationRef = treeMaker.Ident(typeNode.toName("skipValidation"));
        JCExpression dirtyObjectRef = treeMaker.Ident(typeNode.toName("dirtyObject"));
        JCTree.JCMethodInvocation returnInv = treeMaker.Apply(List.nil(), insertRef, List.of(dirtyObjectRef, skipValidationRef));

        jcStatements.append(treeMaker.Return(treeMaker.TypeCast(treeMaker.Ident(modelClassName), returnInv)));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("create")
                .withParameters(List.of(dirtyObjectVar, skipValidationVar))
                .withThrowsClauses(createPersistenceExceptions(treeMaker, typeNode))
                .withReturnType(treeMaker.Ident(modelClassName))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleCreate2Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        Name modelClassName = typeNode.toName(typeNode.getName());
        JCVariableDecl dirtyObjectVar = createParameter(typeNode, treeMaker.Ident(modelClassName), "dirtyObject");
        JCTree.JCIdent dirtyObjectRef = treeMaker.Ident(typeNode.toName("dirtyObject"));
        JCTree.JCMethodInvocation thisSaveInv = treeMaker.Apply(List.nil(),
                treeMaker.Ident(typeNode.toName("create")), List.of(dirtyObjectRef, treeMaker.Literal(false)));

        jcStatements.append(treeMaker.Return(thisSaveInv));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.FINAL | Flags.STATIC)
                .withName("create")
                .withParameters(List.of(dirtyObjectVar))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .withReturnType(typeNode.getName())
                .withThrowsClauses(createPersistenceExceptions(treeMaker, typeNode))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleCreateArray2Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        Name modelClassName = typeNode.toName(typeNode.getName());
        JCVariableDecl dirtyArrayObjectVar = createParameter(typeNode, treeMaker.TypeArray(treeMaker.Ident(modelClassName)),
                "dirtyObjects");
        JCTree.JCIdent dirtyObjectsRef = treeMaker.Ident(typeNode.toName("dirtyObjects"));
        JCTree.JCMethodInvocation thisSaveInv = treeMaker.Apply(List.nil(),
                treeMaker.Ident(typeNode.toName("create")), List.of(dirtyObjectsRef, treeMaker.Literal(false)));

        jcStatements.append(treeMaker.Return(thisSaveInv));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.FINAL | Flags.STATIC)
                .withName("create")
                .withParameters(List.of(dirtyArrayObjectVar))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .withReturnType(treeMaker.TypeArray(treeMaker.TypeIdent(CTC_INT)))
                .withThrowsClauses(createPersistenceExceptions(treeMaker, typeNode))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleCreateArrayMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        Name modelClassName = typeNode.toName(typeNode.getName());
        JCVariableDecl dirtyArrayObjectVar = FieldBuilder.newField(typeNode)
                .ofType(treeMaker.TypeArray(treeMaker.Ident(modelClassName)))
                .withName("dirtyObjects")
                .withModifiers(Flags.PARAMETER)
                .build();

        JCVariableDecl skipValidationVar = FieldBuilder.newField(typeNode)
                .ofType(Boolean.class)
                .withName("skipValidation")
                .withModifiers(Flags.PARAMETER)
                .build();

        addPersistenceRefStatement(treeMaker, typeNode, jcStatements);

        JCExpression insertRef = treeMaker.Select(
                treeMaker.Ident(typeNode.toName("persistence")), typeNode.toName("insert"));
        JCExpression skipValidationRef = treeMaker.Ident(typeNode.toName("skipValidation"));
        JCExpression dirtyObjectsRef = treeMaker.Ident(typeNode.toName("dirtyObjects"));
        JCTree.JCMethodInvocation returnInv = treeMaker.Apply(List.nil(), insertRef, List.of(dirtyObjectsRef, skipValidationRef));
        jcStatements.append(treeMaker.Return(returnInv));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("create")
                .withParameters(List.of(dirtyArrayObjectVar, skipValidationVar))
                .withReturnType(treeMaker.TypeArray(treeMaker.TypeIdent(CTC_INT)))
                .withThrowsClauses(createPersistenceExceptions(treeMaker, typeNode))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleUpdateMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        Name modelClassName = typeNode.toName(typeNode.getName());
        JCVariableDecl idVar = createParameter(typeNode, genJavaLangTypeRef(typeNode, Object.class.getSimpleName()), "id");
        JCVariableDecl dirtyObjectVar = createParameter(typeNode, treeMaker.Ident(modelClassName), "dirtyObject");
        JCVariableDecl skipValidationVar = createParameter(typeNode, treeMaker.TypeIdent(CTC_BOOLEAN), "skipValidation");

        addPersistenceRefStatement(treeMaker, typeNode, jcStatements);

        JCExpression updateRef = treeMaker.Select(treeMaker.Ident(typeNode.toName("persistence")), typeNode.toName("update"));
        JCExpression idRef = treeMaker.Ident(typeNode.toName("id"));
        JCExpression skipValidationRef = treeMaker.Ident(typeNode.toName("skipValidation"));
        JCExpression dirtyObjectRef = treeMaker.Ident(typeNode.toName("dirtyObject"));

        JCTree.JCMethodInvocation returnInv = treeMaker.Apply(List.nil(), updateRef, List.of(idRef, dirtyObjectRef, skipValidationRef));
        jcStatements.append(treeMaker.Return(returnInv));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("update")
                .withParameters(List.of(idVar, dirtyObjectVar, skipValidationVar))
                .withThrowsClauses(createPersistenceExceptions(treeMaker, typeNode))
                .withReturnType(treeMaker.TypeIdent(CTC_INT))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleUpdate2Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        Name modelClassName = typeNode.toName(typeNode.getName());
        JCVariableDecl idVar = FieldBuilder.newField(typeNode)
                .ofType(genJavaLangTypeRef(typeNode, Object.class.getSimpleName()))
                .withName("id")
                .withModifiers(Flags.PARAMETER)
                .build();
        JCVariableDecl dirtyObjectVar = FieldBuilder.newField(typeNode)
                .ofType(treeMaker.Ident(modelClassName))
                .withName("dirtyObject")
                .withModifiers(Flags.PARAMETER)
                .build();
        JCTree.JCIdent idRef = treeMaker.Ident(typeNode.toName("id"));
        JCTree.JCIdent dirtyObjectsRef = treeMaker.Ident(typeNode.toName("dirtyObject"));
        JCTree.JCMethodInvocation thisSaveInv = treeMaker.Apply(List.nil(),
                treeMaker.Ident(typeNode.toName("update")), List.of(idRef, dirtyObjectsRef, treeMaker.Literal(false)));

        jcStatements.append(treeMaker.Return(thisSaveInv));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.FINAL | Flags.STATIC)
                .withName("update")
                .withParameters(List.of(idVar, dirtyObjectVar))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .withReturnType(treeMaker.TypeIdent(CTC_INT))
                .withThrowsClauses(createPersistenceExceptions(treeMaker, typeNode))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleUpdate3Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        JCVariableDecl updatesVar = createParameter(typeNode, genJavaLangTypeRef(typeNode, String.class.getSimpleName()), "updates");
        JCVariableDecl predicationVar = createParameter(typeNode, genJavaLangTypeRef(typeNode, String.class.getSimpleName()), "predication");

        addPersistenceRefStatement(treeMaker, typeNode, jcStatements);

        JCExpression updateRef = treeMaker.Select(treeMaker.Ident(typeNode.toName("persistence")), typeNode.toName("update"));
        JCExpression updatesRef = treeMaker.Ident(typeNode.toName("updates"));
        JCExpression predicationRef = treeMaker.Ident(typeNode.toName("predication"));

        JCTree.JCMethodInvocation returnInv = treeMaker.Apply(List.nil(), updateRef, List.of(updatesRef, predicationRef));
        jcStatements.append(treeMaker.Return(returnInv));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("update")
                .withParameters(List.of(updatesVar, predicationVar))
                .withThrowsClauses(createPersistenceExceptions(treeMaker, typeNode))
                .withReturnType(treeMaker.TypeIdent(CTC_INT))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleDestroyMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        JCVariableDecl idVar = createParameter(typeNode, genJavaLangTypeRef(typeNode, Object.class.getSimpleName()), "id");

        addPersistenceRefStatement(treeMaker, typeNode, jcStatements);

        JCExpression deleteRef = treeMaker.Select(treeMaker.Ident(typeNode.toName("persistence")), typeNode.toName("delete"));
        JCExpression idRef = treeMaker.Ident(typeNode.toName("id"));

        JCTree.JCMethodInvocation returnInv = treeMaker.Apply(List.nil(), deleteRef, List.of(idRef));
        jcStatements.append(treeMaker.Return(returnInv));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("destroy")
                .withParameters(List.of(idVar))
                .withThrowsClauses(createPersistenceExceptions(treeMaker, typeNode))
                .withReturnType(treeMaker.TypeIdent(CTC_INT))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleDestroy2Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        JCVariableDecl predicationVar = createParameter(typeNode,
                genJavaLangTypeRef(typeNode, String.class.getSimpleName()), "predication");

        addPersistenceRefStatement(treeMaker, typeNode, jcStatements);

        JCExpression deleteRef = treeMaker.Select(treeMaker.Ident(typeNode.toName("persistence")), typeNode.toName("delete"));
        JCExpression predicationRef = treeMaker.Ident(typeNode.toName("predication"));

        JCTree.JCMethodInvocation returnInv = treeMaker.Apply(List.nil(), deleteRef, List.of(predicationRef));
        jcStatements.append(treeMaker.Return(returnInv));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("destroy")
                .withParameters(List.of(predicationVar))
                .withThrowsClauses(createPersistenceExceptions(treeMaker, typeNode))
                .withReturnType(treeMaker.TypeIdent(CTC_INT))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleExecuteMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        JCVariableDecl sqlVar = createParameter(typeNode,
                genJavaLangTypeRef(typeNode, String.class.getSimpleName()), "sql");

        addPersistenceRefStatement(treeMaker, typeNode, jcStatements);

        JCExpression executeRef = treeMaker.Select(treeMaker.Ident(typeNode.toName("persistence")), typeNode.toName("execute"));
        JCExpression sqlRef = treeMaker.Ident(typeNode.toName("sql"));

        JCTree.JCMethodInvocation returnInv = treeMaker.Apply(List.nil(), executeRef, List.of(sqlRef));
        jcStatements.append(treeMaker.Return(returnInv));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("execute")
                .withParameters(List.of(sqlVar))
                .withThrowsClauses(createPersistenceExceptions(treeMaker, typeNode))
                .withReturnType(treeMaker.TypeIdent(CTC_INT))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleNewInstanceFromMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        JCVariableDecl sourceVar = createParameter(typeNode,
                genTypeRef(typeNode, Map.class.getName()), "source");

        JCExpression domainClassRef = treeMaker.Select(treeMaker.Ident(typeNode.toName(typeNode.getName())), typeNode.toName("class"));

        Name targetName = typeNode.toName("target");
        JCExpression createNewInstanceRef = treeMaker.Select(
                genTypeRef(typeNode, ClassUtils.class.getName()), typeNode.toName("createNewInstance"));

        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), targetName,
                genTypeRef(typeNode, Object.class.getName()), treeMaker.Apply(List.nil(), createNewInstanceRef, List.of(domainClassRef))));

        JCExpression populateRef = treeMaker.Select(genTypeRef(typeNode, PropertyUtils.class.getName()), typeNode.toName("populate"));
        JCExpression sourceRef = treeMaker.Ident(typeNode.toName("source"));
        JCExpression targetRef = treeMaker.Ident(typeNode.toName("target"));

        jcStatements.append(treeMaker.Exec(treeMaker.Apply(List.nil(), populateRef, List.of(targetRef, sourceRef))));
        jcStatements.append(treeMaker.Return(treeMaker.TypeCast(treeMaker.Ident(typeNode.toName(typeNode.getName())), targetRef)));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("newInstanceFrom")
                .withParameters(List.of(sourceVar))
                .withReturnType(treeMaker.Ident(typeNode.toName(typeNode.getName())))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleNewInstanceFrom2Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        JCVariableDecl sourceVar = createParameter(typeNode,
                genTypeRef(typeNode, Map.class.getName()), "source");
        JCVariableDecl underlineVar = createParameter(typeNode,
                treeMaker.TypeIdent(CTC_BOOLEAN), "underline");

        JCExpression domainClassRef = treeMaker.Select(treeMaker.Ident(typeNode.toName(typeNode.getName())), typeNode.toName("class"));

        Name targetName = typeNode.toName("target");
        JCExpression createNewInstanceRef = treeMaker.Select(
                genTypeRef(typeNode, ClassUtils.class.getName()), typeNode.toName("createNewInstance"));

        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), targetName,
                genTypeRef(typeNode, Object.class.getName()), treeMaker.Apply(List.nil(), createNewInstanceRef, List.of(domainClassRef))));

        JCExpression populateRef = treeMaker.Select(genTypeRef(typeNode, PropertyUtils.class.getName()), typeNode.toName("populate"));
        JCExpression sourceRef = treeMaker.Ident(typeNode.toName("source"));
        JCExpression targetRef = treeMaker.Ident(typeNode.toName("target"));
        JCExpression underlineRef = treeMaker.Ident(typeNode.toName("underline"));

        jcStatements.append(treeMaker.Exec(treeMaker.Apply(List.nil(), populateRef, List.of(targetRef, sourceRef, underlineRef))));
        jcStatements.append(treeMaker.Return(treeMaker.TypeCast(treeMaker.Ident(typeNode.toName(typeNode.getName())), targetRef)));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("newInstanceFrom")
                .withParameters(List.of(sourceVar, underlineVar))
                .withReturnType(treeMaker.Ident(typeNode.toName(typeNode.getName())))
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .buildWith(typeNode);
    }

    private JCTree.JCMethodDecl handleQueryMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        Name modelClassName = typeNode.toName(typeNode.getName());
        JCVariableDecl sqlVar = createParameter(typeNode,
                genJavaLangTypeRef(typeNode, String.class.getSimpleName()), "sql");

        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);

        Name queryName = typeNode.toName("query");
        JCExpression createQueryRef = treeMaker.Select(treeMaker.Ident(modelClassName), typeNode.toName("createQuery"));

        JCExpression returnType = treeMaker.TypeApply(genTypeRef(typeNode, List.class.getName()),
                List.of(genTypeRef(typeNode, Row.class.getName())));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withReturnType(List.class, Row.class)
                .withName("query")
                .withParameters(sqlVar)
                .withBody(treeMaker.Block(0, jcStatements.toList()))
                .buildWith(typeNode);
    }

    private void addPersistenceRefStatement(JavacTreeMaker treeMaker, JavacNode typeNode,
                                            ListBuffer<JCTree.JCStatement> jcStatements) {
        Name modelClassName = typeNode.toName(typeNode.getName());

        Name persistenceFactoryName = typeNode.toName("persistenceFactory");
        JCExpression persistenceFactoryRef = chainDots(typeNode, splitNameOf(PersistenceFactory.class));
        JCExpression getPersistenceFactoryRef = treeMaker.Select(
                chainDots(typeNode, splitNameOf(Database.class)), typeNode.toName("getPersistenceFactory"));
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), persistenceFactoryName,
                persistenceFactoryRef, treeMaker.Apply(List.nil(), getPersistenceFactoryRef, List.nil())));

        Name persistenceName = typeNode.toName("persistence");
        JCExpression persistenceRef = genTypeRef(typeNode, Persistence.class.getName());
        JCExpression createPersistenceRef = treeMaker.Select(
                treeMaker.Ident(typeNode.toName("persistenceFactory")), typeNode.toName("createPersistence"));
        JCExpression modelClassRef = treeMaker.Select(treeMaker.Ident(modelClassName), typeNode.toName("class"));
        JCTree.JCModifiers persistenceModifier = treeMaker.Modifiers(Flags.PARAMETER);
        jcStatements.append(treeMaker.VarDef(persistenceModifier, persistenceName,
                persistenceRef, treeMaker.Apply(List.nil(), createPersistenceRef, List.of(modelClassRef))));
    }

    private JCVariableDecl createIdField(JavacTreeMaker treeMaker, JavacNode typeNode, DomainModel domainModel) {
        JCAnnotation annotation = treeMaker.Annotation(chainDots(typeNode, splitNameOf(PrimaryKey.class)),
                List.of(treeMaker.Assign(treeMaker.Ident(typeNode.toName("name")),
                        treeMaker.Literal(domainModel.primaryColumnName()))));

        return FieldBuilder.newField(typeNode)
                .ofType(domainModel.primaryClass())
                .withModifiers(Flags.PRIVATE)
                .withAnnotations(annotation)
                .withName(domainModel.primaryFieldName())
                .build();
    }

    private void handleTableNameField(JavacTreeMaker treeMaker, JavacNode typeNode) {
        JCExpression tableRef = genTypeRef(typeNode, Table.class.getName());
        JCExpression getTableRef = treeMaker.Select(tableRef, typeNode.toName("getTableName"));
        JCExpression paramRef = treeMaker.Select(treeMaker.Ident(typeNode.toName(typeNode.getName())), typeNode.toName("class"));
        JCTree.JCMethodInvocation getTableInv = treeMaker.Apply(List.nil(), getTableRef, List.of(paramRef));
        JCVariableDecl variableDecl = FieldBuilder.newField(typeNode)
                .ofType(genJavaLangTypeRef(typeNode, String.class.getSimpleName()))
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("TABLE_NAME")
                .withInit(getTableInv)
                .build();

        injectField(typeNode, variableDecl);
    }

    private List<JCExpression> createPersistenceExceptions(JavacTreeMaker treeMaker, JavacNode typeNode) {
        return List.of(treeMaker.Throw(chainDots(typeNode, splitNameOf(SQLException.class))).getExpression(),
                treeMaker.Throw(chainDots(typeNode, splitNameOf(PersistenceException.class))).getExpression());
    }

    private JCVariableDecl createParameter(JavacNode typeNode, JCExpression type, String name, JCTree.JCAnnotation... annotations) {
        return FieldBuilder.newField(typeNode)
                .ofType(type)
                .withName(name)
                .withAnnotations(annotations)
                .withModifiers(Flags.PARAMETER)
                .build();
    }

}
