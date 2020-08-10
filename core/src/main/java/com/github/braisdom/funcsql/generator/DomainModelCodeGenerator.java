package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.*;
import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.apt.*;
import com.github.braisdom.funcsql.apt.MethodBuilder;
import com.github.braisdom.funcsql.reflection.ClassUtils;
import com.github.braisdom.funcsql.reflection.PropertyUtils;
import com.github.braisdom.funcsql.relation.Relationship;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import org.mangosdk.spi.ProviderFor;

import java.sql.SQLException;
import java.util.Map;

@ProviderFor(JavacAnnotationHandler.class)
public class DomainModelCodeGenerator extends JavacAnnotationHandler<DomainModel> {

    @Override
    public void handle(AnnotationValues annotationValues, JCTree ast, APTUtils aptUtils) {
        handleSetterGetter(annotationValues, aptUtils);
        handlePrimary(annotationValues, aptUtils);
        handleTableName(aptUtils);
        handleCreateQueryMethod(aptUtils);
        handleCreatePersistenceMethod(aptUtils);
        handleSaveMethod(aptUtils);
        handleCreateMethod(aptUtils);
        handleCreateArrayMethod(aptUtils);
        handleUpdateMethod(annotationValues, aptUtils);
        handleUpdate2Method(aptUtils);
        handleDestroyMethod(annotationValues, aptUtils);
        handleDestroy2Method(aptUtils);
        handleExecuteMethod(aptUtils);
        handleQueryMethod(aptUtils);
        handleQuery2Method(aptUtils);
        handleQuery3Method(aptUtils);
        handleQueryFirstMethod(aptUtils);
        handleQueryFirst2Method(aptUtils);
        handleCountMethod(aptUtils);
        handleValidateMethod(aptUtils);
        handleNewInstanceFromMethod(aptUtils);
    }

    private void handleSetterGetter(AnnotationValues annotationValues, APTUtils aptUtils) {
        java.util.List<JCVariableDecl> fields = aptUtils.getFields();
        DomainModel domainModel = annotationValues.getAnnotationValue(DomainModel.class);
        aptUtils.getTreeMaker().at(aptUtils.get().pos);
        for (JCVariableDecl field : fields) {
            if (!aptUtils.isStatic(field.mods)) {
                JCTree.JCMethodDecl setter = aptUtils.newSetter(field, domainModel.fluent());
                JCTree.JCMethodDecl getter = aptUtils.newGetter(field);

                aptUtils.inject(setter);
                aptUtils.inject(getter);
            }
        }
    }

    private void handlePrimary(AnnotationValues annotationValues, APTUtils aptUtils) {
        TreeMaker treeMaker = aptUtils.getTreeMaker();
        DomainModel domainModel = annotationValues.getAnnotationValue(DomainModel.class);

        JCTree.JCAnnotation annotation = treeMaker.Annotation(aptUtils.typeRef(PrimaryKey.class),
                List.of(treeMaker.Assign(treeMaker.Ident(aptUtils.toName("name")),
                        treeMaker.Literal(domainModel.primaryColumnName()))));
        JCModifiers modifiers = treeMaker.Modifiers(Flags.PRIVATE);
        modifiers.annotations = modifiers.annotations.append(annotation);

        JCVariableDecl primaryField = treeMaker.VarDef(modifiers,
                aptUtils.toName(domainModel.primaryFieldName()), aptUtils.typeRef(domainModel.primaryClass()), null);

        aptUtils.inject(primaryField);
        aptUtils.inject(aptUtils.newSetter(primaryField, domainModel.fluent()));
        aptUtils.inject(aptUtils.newGetter(primaryField));
    }

    private void handleTableName(APTUtils aptUtils) {
        TreeMaker treeMaker = aptUtils.getTreeMaker();

        JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL);

        JCMethodInvocation methodInvocation = treeMaker.Apply(List.nil(),
                treeMaker.Select(aptUtils.typeRef(Table.class), aptUtils.toName("getTableName")),
                List.of(aptUtils.classRef(aptUtils.getClassName())));
        JCVariableDecl tableNameField = treeMaker.VarDef(modifiers,
                aptUtils.toName("TABLE_NAME"), aptUtils.typeRef(String.class), methodInvocation);

        aptUtils.inject(tableNameField);
    }

    private void handleCreateQueryMethod(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();

        statementBuilder.append(aptUtils.typeRef(QueryFactory.class), "queryFactory", Database.class,
                "getQueryFactory", List.nil());

        methodBuilder.setReturnStatement("queryFactory", "createQuery", aptUtils.classRef(aptUtils.getClassName()));

        aptUtils.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .setReturnType(Query.class, aptUtils.typeRef(aptUtils.getClassName()))
                .build("createQuery", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleCreatePersistenceMethod(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();

        statementBuilder.append(aptUtils.typeRef(PersistenceFactory.class), "persistenceFactory", Database.class,
                "getPersistenceFactory", List.nil());

        methodBuilder.setReturnStatement("persistenceFactory", "createPersistence",
                aptUtils.classRef(aptUtils.getClassName()));

        aptUtils.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .setReturnType(Persistence.class, aptUtils.typeRef(aptUtils.getClassName()))
                .build("createPersistence", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleSaveMethod(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        TreeMaker treeMaker = aptUtils.getTreeMaker();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();

        statementBuilder.append(aptUtils.newGenericsType(Persistence.class, aptUtils.getClassName()), "persistence",
                "createPersistence");

        statementBuilder.append("persistence", "save",
                aptUtils.varRef("this"), aptUtils.varRef("skipValidation"));

        aptUtils.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("skipValidation", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .setThrowsClauses(SQLException.class)
                .build("save", Flags.PUBLIC | Flags.FINAL));
    }

    private void handleCreateMethod(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        TreeMaker treeMaker = aptUtils.getTreeMaker();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();

        statementBuilder.append(aptUtils.newGenericsType(Persistence.class, aptUtils.getClassName()), "persistence",
                "createPersistence");

        methodBuilder.setReturnStatement("persistence", "insert",
                aptUtils.varRef("dirtyObject"), aptUtils.varRef("skipValidation"));

        aptUtils.inject(methodBuilder
                .setReturnType(aptUtils.typeRef(aptUtils.getClassName()))
                .addStatements(statementBuilder.build())
                .addParameter("dirtyObject", aptUtils.typeRef(aptUtils.getClassName()))
                .addParameter("skipValidation", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .setThrowsClauses(SQLException.class)
                .build("create", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleCreateArrayMethod(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        TreeMaker treeMaker = aptUtils.getTreeMaker();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();

        statementBuilder.append(aptUtils.newGenericsType(Persistence.class, aptUtils.getClassName()), "persistence",
                "createPersistence");

        methodBuilder.setReturnStatement("persistence", "insert",
                aptUtils.varRef("dirtyObjects"), aptUtils.varRef("skipValidation"));

        aptUtils.inject(methodBuilder
                .setReturnType(aptUtils.newArrayType(treeMaker.TypeIdent(TypeTag.INT)))
                .addStatements(statementBuilder.build())
                .addParameter("dirtyObjects", aptUtils.newArrayType(aptUtils.getClassName()))
                .addParameter("skipValidation", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .setThrowsClauses(SQLException.class)
                .build("create", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleUpdateMethod(AnnotationValues annotationValues, APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        TreeMaker treeMaker = aptUtils.getTreeMaker();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();
        DomainModel domainModel = annotationValues.getAnnotationValue(DomainModel.class);

        statementBuilder.append(aptUtils.newGenericsType(Persistence.class, aptUtils.getClassName()), "persistence",
                "createPersistence");

        methodBuilder.setReturnStatement("persistence", "update",
                aptUtils.varRef("id"), aptUtils.varRef("dirtyObject"), aptUtils.varRef("skipValidation"));

        aptUtils.inject(methodBuilder
                .setReturnType(treeMaker.TypeIdent(TypeTag.INT))
                .addStatements(statementBuilder.build())
                .addParameter("id", aptUtils.typeRef(domainModel.primaryClass()))
                .addParameter("dirtyObject", aptUtils.typeRef(aptUtils.getClassName()))
                .addParameter("skipValidation", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .setThrowsClauses(SQLException.class)
                .build("update", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleUpdate2Method(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        TreeMaker treeMaker = aptUtils.getTreeMaker();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();

        statementBuilder.append(aptUtils.newGenericsType(Persistence.class, aptUtils.getClassName()), "persistence",
                "createPersistence");

        methodBuilder.setReturnStatement("persistence", "update",
                aptUtils.varRef("updates"), aptUtils.varRef("predicates"));

        aptUtils.inject(methodBuilder
                .setReturnType(treeMaker.TypeIdent(TypeTag.INT))
                .addStatements(statementBuilder.build())
                .addParameter("updates", aptUtils.typeRef(String.class))
                .addParameter("predicates", aptUtils.typeRef(String.class))
                .setThrowsClauses(SQLException.class)
                .build("update", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleDestroyMethod(AnnotationValues annotationValues, APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        TreeMaker treeMaker = aptUtils.getTreeMaker();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();
        DomainModel domainModel = annotationValues.getAnnotationValue(DomainModel.class);

        statementBuilder.append(aptUtils.newGenericsType(Persistence.class, aptUtils.getClassName()), "persistence",
                "createPersistence");

        methodBuilder.setReturnStatement("persistence", "delete",
                aptUtils.varRef("id"));

        aptUtils.inject(methodBuilder
                .setReturnType(treeMaker.TypeIdent(TypeTag.INT))
                .addStatements(statementBuilder.build())
                .addParameter("id", aptUtils.typeRef(domainModel.primaryClass()))
                .setThrowsClauses(SQLException.class)
                .build("destroy", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleDestroy2Method(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        TreeMaker treeMaker = aptUtils.getTreeMaker();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();

        statementBuilder.append(aptUtils.newGenericsType(Persistence.class, aptUtils.getClassName()), "persistence",
                "createPersistence");

        methodBuilder.setReturnStatement("persistence", "delete",
                aptUtils.varRef("predicate"));

        aptUtils.inject(methodBuilder
                .setReturnType(treeMaker.TypeIdent(TypeTag.INT))
                .addStatements(statementBuilder.build())
                .addParameter("predicate", aptUtils.typeRef(String.class))
                .setThrowsClauses(SQLException.class)
                .build("destroy", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleExecuteMethod(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();

        methodBuilder.setReturnStatement(Table.class, "execute", aptUtils.classRef(aptUtils.getClassName()),
                aptUtils.varRef("sql"), aptUtils.varRef("params"));

        aptUtils.inject(methodBuilder
                .setReturnType(aptUtils.getTreeMaker().TypeIdent(TypeTag.INT))
                .addParameter("sql", aptUtils.typeRef(String.class))
                .addVarargsParameter("params", aptUtils.typeRef(Object.class))
                .setThrowsClauses(SQLException.class)
                .build("execute", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleQueryMethod(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();

        statementBuilder.append(aptUtils.newGenericsType(Query.class, aptUtils.getClassName()), "query",
                "createQuery");
        statementBuilder.append("query", "where",
                List.of(aptUtils.varRef("predicate"), aptUtils.varRef("params")));

        methodBuilder.setReturnStatement("query", "execute");
        aptUtils.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("predicate", aptUtils.typeRef(String.class))
                .addVarargsParameter("params", aptUtils.typeRef(Object.class))
                .setThrowsClauses(SQLException.class)
                .setReturnType(java.util.List.class, aptUtils.typeRef(aptUtils.getClassName()))
                .build("query", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleQuery2Method(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();

        statementBuilder.append(aptUtils.newGenericsType(Query.class, aptUtils.getClassName()), "query",
                "createQuery");
        statementBuilder.append("query", "where",
                List.of(aptUtils.varRef("predicate"), aptUtils.varRef("params")));

        methodBuilder.setReturnStatement("query", "execute", aptUtils.varRef("relations"));
        aptUtils.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("predicate", aptUtils.typeRef(String.class))
                .addArrayParameter("relations", Relationship.class)
                .addVarargsParameter("params", aptUtils.typeRef(Object.class))
                .setThrowsClauses(SQLException.class)
                .setReturnType(java.util.List.class, aptUtils.typeRef(aptUtils.getClassName()))
                .build("query", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleQuery3Method(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();

        methodBuilder.setReturnStatement(Table.class, "query", aptUtils.classRef(aptUtils.getClassName()),
                aptUtils.varRef("sql"), aptUtils.varRef("params"));
        aptUtils.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("sql", aptUtils.typeRef(String.class))
                .addVarargsParameter("params", aptUtils.typeRef(Object.class))
                .setThrowsClauses(SQLException.class)
                .setReturnType(java.util.List.class, aptUtils.typeRef(aptUtils.getClassName()))
                .build("queryBySql", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleQueryFirstMethod(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();

        statementBuilder.append(aptUtils.newGenericsType(Query.class, aptUtils.getClassName()), "query",
                "createQuery");
        statementBuilder.append("query", "where",
                List.of(aptUtils.varRef("predicate"), aptUtils.varRef("params")));

        methodBuilder.setReturnStatement("query", "queryFirst", aptUtils.varRef("relations"));
        aptUtils.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("predicate", aptUtils.typeRef(String.class))
                .addArrayParameter("relations", Relationship.class)
                .addVarargsParameter("params", aptUtils.typeRef(Object.class))
                .setThrowsClauses(SQLException.class)
                .setReturnType(aptUtils.typeRef(aptUtils.getClassName()))
                .build("queryFirst", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleQueryFirst2Method(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();

        statementBuilder.append(aptUtils.newGenericsType(Query.class, aptUtils.getClassName()), "query",
                "createQuery");
        statementBuilder.append("query", "where",
                List.of(aptUtils.varRef("predicate"), aptUtils.varRef("params")));

        methodBuilder.setReturnStatement("query", "queryFirst");
        aptUtils.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("predicate", aptUtils.typeRef(String.class))
                .addVarargsParameter("params", aptUtils.typeRef(Object.class))
                .setThrowsClauses(SQLException.class)
                .setReturnType(aptUtils.typeRef(aptUtils.getClassName()))
                .build("queryFirst", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleCountMethod(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();

        methodBuilder.setReturnStatement(Table.class, "count", aptUtils.classRef(aptUtils.getClassName()),
                aptUtils.varRef("sql"), aptUtils.varRef("params"));

        aptUtils.inject(methodBuilder
                .addParameter("sql", aptUtils.typeRef(String.class))
                .addVarargsParameter("params", aptUtils.typeRef(Object.class))
                .setThrowsClauses(SQLException.class)
                .setReturnType(aptUtils.getTreeMaker().TypeIdent(TypeTag.INT))
                .build("count", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleValidateMethod(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();

        methodBuilder.setReturnStatement(Table.class, "validate",
                aptUtils.varRef("this"), aptUtils.getTreeMaker().Literal(false));

        aptUtils.inject(methodBuilder
                .setThrowsClauses(ValidationException.class)
                .setReturnType(aptUtils.newArrayType(Validator.Violation.class))
                .build("validate", Flags.PUBLIC | Flags.FINAL));
    }

    private void handleNewInstanceFromMethod(APTUtils aptUtils) {
        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        TreeMaker treeMaker = aptUtils.getTreeMaker();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();

        JCExpression createInstance = treeMaker.TypeCast(aptUtils.typeRef(aptUtils.getClassName()),
                treeMaker.Apply(List.nil(), treeMaker.Select(aptUtils.typeRef(ClassUtils.class),
                        aptUtils.toName("createNewInstance")), List.of(aptUtils.classRef(aptUtils.getClassName()))));
        statementBuilder.append(aptUtils.typeRef(aptUtils.getClassName()), "bean", createInstance);
        statementBuilder.append(PropertyUtils.class, "populate", aptUtils.varRef("bean"),
                aptUtils.varRef("properties"), aptUtils.varRef("underLine"));

        methodBuilder.setReturnStatement(aptUtils.varRef("bean"));

        aptUtils.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("properties", Map.class)
                .addParameter("underLine", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .setReturnType(aptUtils.typeRef(aptUtils.getClassName()))
                .build("newInstanceFrom", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

//    @Override
//    public void handle(AnnotationValues<DomainModel> annotationValues, JCAnnotation jcAnnotation, APTUtils javacNode) {
//        APTUtils typeNode = javacNode.up();
//        JCTree.JCClassDecl classDecl = (JCTree.JCClassDecl) typeNode.get();
//        HandleGetter handleGetter = new HandleGetter();
//        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
//        DomainModel domainModel = annotationValues.getInstance();
//
//        if (!domainModel.disableGeneratedId()) {
//            JCVariableDecl idFieldDecl = createIdField(treeMaker, typeNode, domainModel);
//            APTUtils fieldNode = new APTUtils(javacNode.getAst(), idFieldDecl, null, AST.Kind.FIELD) {
//                @Override
//                public APTUtils up() {
//                    return typeNode;
//                }
//            };
//
//            injectField(typeNode, idFieldDecl);
////            handleGetter.generateGetterForField(fieldNode, null, AccessLevel.PUBLIC, false);
////            HandleSetter.createSetter(Flags.PUBLIC, fieldNode, treeMaker, toSetterName(fieldNode), domainModel.fluent(),
////                    typeNode, List.nil(), List.nil());
//        }
//
//        handleFieldSG(treeMaker, domainModel, typeNode, handleGetter);
//        handleTableNameField(treeMaker, typeNode);
//        handleRawAttributesField(treeMaker, typeNode);
//
//        JCMethodDecl[] methodDeclArray = new JCMethodDecl[]{
//                handleCreatePersistenceMethod(treeMaker, typeNode),
//                handleCreateQueryMethod(treeMaker, typeNode),
//                handleSaveMethod(treeMaker, typeNode),
//                handleSave2Method(treeMaker, typeNode),
//                handleCreateMethod(treeMaker, typeNode),
//                handleCreate2Method(treeMaker, typeNode),
//                handleCreateArrayMethod(treeMaker, typeNode),
//                handleCreateArray2Method(treeMaker, typeNode),
//                handleUpdateMethod(treeMaker, typeNode),
//                handleUpdate2Method(treeMaker, typeNode),
//                handleUpdate3Method(treeMaker, typeNode),
//                handleDestroyMethod(treeMaker, typeNode),
//                handleDestroy2Method(treeMaker, typeNode),
//                handleExecuteMethod(treeMaker, typeNode),
//                handleNewInstanceFromMethod(treeMaker, typeNode),
//                handleNewInstanceFrom2Method(treeMaker, typeNode),
//                handleQueryMethod(treeMaker, typeNode),
//                handleValidateMethod(treeMaker, typeNode),
//                handleCountMethod(treeMaker, typeNode),
//                handleFindFirstMethod(treeMaker, typeNode)
//        };
//
//        Arrays.stream(methodDeclArray).forEach(methodDecl -> {
//            if (!JCTreeUtil.containsMethod(classDecl.sym, methodDecl, false))
//                injectMethod(typeNode, methodDecl);
//        });
//        System.out.println();
//    }
//
//    private void handleFieldSG(JavacTreeMaker treeMaker, DomainModel domainModel, APTUtils typeNode, HandleGetter handleGetter) {
//        for (APTUtils field : typeNode.down()) {
////            if (handleGetter.fieldQualifiesForGetterGeneration(field))
////                handleGetter.generateGetterForField(field, null, AccessLevel.PUBLIC, false);
////
////            if (field.getKind() != AST.Kind.FIELD) continue;
////            JCVariableDecl fieldDecl = (JCVariableDecl) field.get();
////            //Skip fields that start with $
////            if (fieldDecl.name.toString().startsWith("$")) continue;
////            //Skip static fields.
////            if ((fieldDecl.mods.flags & Flags.STATIC) != 0) continue;
////            //Skip final fields.
////            if ((fieldDecl.mods.flags & Flags.FINAL) != 0) continue;
//
////            injectMethod(typeNode, HandleSetter.createSetter(Flags.PUBLIC, field, treeMaker, toSetterName(field), domainModel.fluent(),
////                    typeNode, List.nil(), List.nil()));
//        }
//    }
//
//    // public final void save() throws SQLException {
//    private JCTree.JCMethodDecl handleSave2Method(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//
//        // this.save(false);
//        blockBuilder.appendInstanceMethodInvoke("save", treeMaker.Literal(false));
//
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.FINAL)
//                .withName("save")
//                .withBody(blockBuilder.build())
//                .withThrowsClauses(SQLException.class)
//                .buildWith(typeNode);
//    }
//
//    // public final void save(boolean skipValidation) throws SQLException {...}
//    private JCTree.JCMethodDecl handleSaveMethod(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        JCVariableDecl parameter = createParameter(typeNode, treeMaker.TypeIdent(CTC_BOOLEAN), "skipValidation");
//
//        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
//        // Persistence<RelationshipTest.TestDomainModel> persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
//        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);
//
//        // persistence.save(this, skipValidation);
//        blockBuilder.appendStaticMethodInvoke("persistence", "save",
//                varRef(typeNode, "this"), varRef(typeNode, "skipValidation"));
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.FINAL)
//                .withName("save")
//                .withParameters(List.of(parameter))
//                .withBody(blockBuilder.build())
//                .withThrowsClauses(SQLException.class)
//                .buildWith(typeNode);
//    }
//
//    private JCTree.JCMethodDecl handleCreateQueryMethod(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
//        Name modelClassName = typeNode.toName(typeNode.getName());
//        Name queryFactoryName = typeNode.toName("queryFactory");
//        JCExpression queryFactoryRef = chainDots(typeNode, splitNameOf(QueryFactory.class));
//        JCExpression getQueryFactoryInv = treeMaker.Select(chainDots(typeNode, splitNameOf(Database.class)),
//                typeNode.toName("getQueryFactory"));
//
//        JCExpression createQueryFactoryInv = treeMaker.Select(
//                treeMaker.Ident(typeNode.toName(typeNode.getName())), typeNode.toName("class"));
//        JCExpression createQueryInv = treeMaker.Select(
//                treeMaker.Ident(typeNode.toName("queryFactory")), typeNode.toName("createQuery"));
//
//        jcStatements.inject(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), queryFactoryName,
//                queryFactoryRef, treeMaker.Apply(List.nil(), getQueryFactoryInv, List.nil())));
//        jcStatements.inject(treeMaker.Return(treeMaker.Apply(List.nil(), createQueryInv, List.of(createQueryFactoryInv))));
//
//        JCExpression returnType = treeMaker.TypeApply(genTypeRef(typeNode, Query.class.getName()),
//                List.of(treeMaker.Ident(modelClassName)));
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withName("createQuery")
//                .withReturnType(returnType)
//                .withBody(treeMaker.Block(0, jcStatements.toList()))
//                .buildWith(typeNode);
//    }
//
//    private JCTree.JCMethodDecl handleCreatePersistenceMethod(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//
//        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
//        blockBuilder.appendVar(PersistenceFactory.class, "persistenceFactory",
//                Database.class, "getPersistenceFactory");
//
//        //Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestRelativeModel.class);
//        JCExpression createPersistence = staticMethodInvoke(typeNode,
//                "persistenceFactory", "createPersistence", classRef(typeNode, typeNode.getName()));
//        JCTree.JCTypeApply typeApply = treeMaker.TypeApply(genTypeRef(typeNode, Persistence.class.getName()),
//                List.of(treeMaker.Ident(typeNode.toName(typeNode.getName()))));
//        blockBuilder.appendReturn(createPersistence);
//
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withName("createPersistence")
//                .withReturnType(typeApply)
//                .withBody(blockBuilder.build())
//                .buildWith(typeNode);
//    }
//
//    // public static final RelationshipTest.TestDomainModel create(RelationshipTest.TestDomainModel dirtyObject,
//    //                          boolean skipValidation) throws SQLException {...}
//    private JCTree.JCMethodDecl handleCreateMethod(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        Name modelClassName = typeNode.toName(typeNode.getName());
//        JCVariableDecl dirtyObjectVar = createParameter(typeNode, treeMaker.Ident(modelClassName), "dirtyObject");
//        JCVariableDecl skipValidationVar = createParameter(typeNode, treeMaker.TypeIdent(CTC_BOOLEAN), "skipValidation");
//
//        // Table.validate(dirtyObject, skipValidation);
//        blockBuilder.appendStaticMethodInvoke(Table.class, "validate",
//                treeMaker.Ident(typeNode.toName("dirtyObject")), treeMaker.Ident(typeNode.toName("skipValidation")));
//
//        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
//        // Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
//        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);
//
//        // return (RelationshipTest.TestDomainModel)persistence.insert(dirtyObject, skipValidation);
//        blockBuilder.appendReturn("persistence", "insert",
//                varRef(typeNode, "dirtyObject"), varRef(typeNode, "skipValidation"));
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withName("create")
//                .withParameters(List.of(dirtyObjectVar, skipValidationVar))
//                .withThrowsClauses(SQLException.class)
//                .withReturnType(treeMaker.Ident(modelClassName))
//                .withBody(blockBuilder.build())
//                .buildWith(typeNode);
//    }
//
//    // public static final RelationshipTest.TestDomainModel create(RelationshipTest.TestDomainModel dirtyObject)
//    //          throws SQLException {
//    private JCTree.JCMethodDecl handleCreate2Method(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        Name modelClassName = typeNode.toName(typeNode.getName());
//        JCVariableDecl dirtyObjectVar = createParameter(typeNode, treeMaker.Ident(modelClassName), "dirtyObject");
//
//        // return create(dirtyObject, false);
//        blockBuilder.appendReturn(typeNode.getName(), "create", varRef(typeNode, "dirtyObject"),
//                treeMaker.Literal(false));
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.FINAL | Flags.STATIC)
//                .withName("create")
//                .withParameters(List.of(dirtyObjectVar))
//                .withBody(blockBuilder.build())
//                .withReturnType(typeNode.getName())
//                .withThrowsClauses(SQLException.class)
//                .buildWith(typeNode);
//    }
//
//    // public static final int[] create(RelationshipTest.TestDomainModel[] dirtyObjects) throws SQLException {...}
//    private JCTree.JCMethodDecl handleCreateArray2Method(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        Name modelClassName = typeNode.toName(typeNode.getName());
//        JCVariableDecl dirtyArrayObjectVar = createParameter(typeNode, treeMaker.TypeArray(treeMaker.Ident(modelClassName)),
//                "dirtyObjects");
//
//        // return create(dirtyObjects, false);
//        blockBuilder.appendReturn(typeNode.getName(), "create", varRef(typeNode, "dirtyObjects"),
//                treeMaker.Literal(false));
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.FINAL | Flags.STATIC)
//                .withName("create")
//                .withParameters(List.of(dirtyArrayObjectVar))
//                .withBody(blockBuilder.build())
//                .withReturnType(treeMaker.TypeArray(treeMaker.TypeIdent(CTC_INT)))
//                .withThrowsClauses(SQLException.class)
//                .buildWith(typeNode);
//    }
//
//    // public static final int[] create(RelationshipTest.TestDomainModel[] dirtyObjects, boolean skipValidation) throws SQLException {
//    private JCTree.JCMethodDecl handleCreateArrayMethod(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        Name modelClassName = typeNode.toName(typeNode.getName());
//        JCVariableDecl dirtyArrayObjectVar =  createParameter(typeNode,
//                treeMaker.TypeArray(treeMaker.Ident(modelClassName)), "dirtyObjects");
//        JCVariableDecl skipValidationVar =  createParameter(typeNode,
//                treeMaker.TypeIdent(CTC_BOOLEAN), "skipValidation");
//
//        // Table.validate(dirtyObjects, skipValidation);
//        blockBuilder.appendStaticMethodInvoke(Table.class, "validate",
//                treeMaker.Ident(typeNode.toName("dirtyObjects")), treeMaker.Ident(typeNode.toName("skipValidation")));
//
//        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
//        // Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
//        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);
//
//        // return persistence.insert(dirtyObjects, skipValidation);
//        blockBuilder.appendReturn("persistence", "insert",
//                varRef(typeNode, "dirtyObjects"), varRef(typeNode, "skipValidation"));
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withName("create")
//                .withParameters(List.of(dirtyArrayObjectVar, skipValidationVar))
//                .withReturnType(treeMaker.TypeArray(treeMaker.TypeIdent(CTC_INT)))
//                .withThrowsClauses(SQLException.class)
//                .withBody(blockBuilder.build())
//                .buildWith(typeNode);
//    }
//
//    // public static final int update(Object id, RelationshipTest.TestDomainModel dirtyObject, boolean skipValidation) throws SQLException {
//    private JCTree.JCMethodDecl handleUpdateMethod(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        Name modelClassName = typeNode.toName(typeNode.getName());
//        JCVariableDecl idVar = createParameter(typeNode, genJavaLangTypeRef(typeNode, Object.class.getSimpleName()), "id");
//        JCVariableDecl dirtyObjectVar = createParameter(typeNode, treeMaker.Ident(modelClassName), "dirtyObject");
//        JCVariableDecl skipValidationVar = createParameter(typeNode, treeMaker.TypeIdent(CTC_BOOLEAN), "skipValidation");
//
//        // Table.validate(dirtyObject, skipValidation);
//        blockBuilder.appendStaticMethodInvoke(Table.class, "validate",
//                treeMaker.Ident(typeNode.toName("dirtyObject")), treeMaker.Ident(typeNode.toName("skipValidation")));
//
//        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
//        // Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
//        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);
//
//        // return persistence.update(id, dirtyObject, skipValidation);
//        blockBuilder.appendReturn("persistence", "update",
//                varRef(typeNode, "id"), varRef(typeNode, "dirtyObject"), varRef(typeNode, "skipValidation"));
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withName("update")
//                .withParameters(List.of(idVar, dirtyObjectVar, skipValidationVar))
//                .withThrowsClauses(SQLException.class)
//                .withReturnType(treeMaker.TypeIdent(CTC_INT))
//                .withBody(blockBuilder.build())
//                .buildWith(typeNode);
//    }
//
//    // public static final int update(Object id, RelationshipTest.TestDomainModel dirtyObject) throws SQLException {...}
//    private JCTree.JCMethodDecl handleUpdate2Method(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        JCVariableDecl idVar = createParameter(typeNode,
//                genJavaLangTypeRef(typeNode, Object.class.getSimpleName()), "id");
//        JCVariableDecl dirtyObjectVar = createParameter(typeNode,
//                treeMaker.Ident(typeNode.toName(typeNode.getName())), "dirtyObject");
//
//        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
//        // Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
//        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);
//
//        // return persistence.update(id, dirtyObject, false);
//        blockBuilder.appendReturn("persistence", "update",
//                varRef(typeNode, "id"), varRef(typeNode, "dirtyObject"), treeMaker.Literal(true));
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.FINAL | Flags.STATIC)
//                .withName("update")
//                .withParameters(List.of(idVar, dirtyObjectVar))
//                .withBody(blockBuilder.build())
//                .withReturnType(treeMaker.TypeIdent(CTC_INT))
//                .withThrowsClauses(SQLException.class)
//                .buildWith(typeNode);
//    }
//
//    // public static final int update(String updates, String predication) throws SQLException {...}
//    private JCTree.JCMethodDecl handleUpdate3Method(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        JCVariableDecl updatesVar = createParameter(typeNode,
//                genJavaLangTypeRef(typeNode, String.class.getSimpleName()), "updates");
//        JCVariableDecl predicationVar = createParameter(typeNode,
//                genJavaLangTypeRef(typeNode, String.class.getSimpleName()), "predication");
//
//        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
//        // Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
//        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);
//
//        // return persistence.update(updates, predication);
//        blockBuilder.appendReturn("persistence", "update",
//                varRef(typeNode, "updates"), varRef(typeNode, "predication"));
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withName("update")
//                .withParameters(List.of(updatesVar, predicationVar))
//                .withThrowsClauses(SQLException.class)
//                .withReturnType(treeMaker.TypeIdent(CTC_INT))
//                .withBody(blockBuilder.build())
//                .buildWith(typeNode);
//    }
//
//    // public static final int destroy(Object id) throws SQLException {...}
//    private JCTree.JCMethodDecl handleDestroyMethod(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        JCVariableDecl idVar = createParameter(typeNode,
//                genJavaLangTypeRef(typeNode, Object.class.getSimpleName()), "id");
//
//        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
//        // Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
//        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);
//
//        // return persistence.delete(id);
//        blockBuilder.appendReturn("persistence", "delete", varRef(typeNode, "id"));
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withName("destroy")
//                .withParameters(idVar)
//                .withThrowsClauses(SQLException.class)
//                .withReturnType(treeMaker.TypeIdent(CTC_INT))
//                .withBody(blockBuilder.build())
//                .buildWith(typeNode);
//    }
//
//    // public static final int destroy(String predication) throws SQLException {...}
//    private JCTree.JCMethodDecl handleDestroy2Method(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        JCVariableDecl predicationVar = createParameter(typeNode,
//                genJavaLangTypeRef(typeNode, String.class.getSimpleName()), "predication");
//
//        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
//        // Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
//        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);
//
//        // return persistence.delete(predication);
//        blockBuilder.appendReturn("persistence", "delete", varRef(typeNode, "predication"));
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withName("destroy")
//                .withParameters(List.of(predicationVar))
//                .withThrowsClauses(SQLException.class)
//                .withReturnType(treeMaker.TypeIdent(CTC_INT))
//                .withBody(blockBuilder.build())
//                .buildWith(typeNode);
//    }
//
//    // public static final int execute(String sql) throws SQLException {...}
//    private JCTree.JCMethodDecl handleExecuteMethod(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        JCVariableDecl sqlVar = createParameter(typeNode,
//                genJavaLangTypeRef(typeNode, String.class.getSimpleName()), "sql");
//
//        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);
//
//        // return persistence.execute(sql);
//        blockBuilder.appendReturn("persistence", "execute", varRef(typeNode, "sql"));
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withName("execute")
//                .withParameters(sqlVar)
//                .withThrowsClauses(SQLException.class)
//                .withReturnType(treeMaker.TypeIdent(CTC_INT))
//                .withBody(blockBuilder.build())
//                .buildWith(typeNode);
//    }
//
//    // public static final RelationshipTest.TestRelativeModel newInstanceFrom(Map source) {...}
//    private JCTree.JCMethodDecl handleNewInstanceFromMethod(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        JCVariableDecl sourceVar = createParameter(typeNode,
//                genTypeRef(typeNode, Map.class.getName()), "source");
//
//        // return newInstanceFrom(source, true);
//        JCExpression createNewInstance = blockBuilder.staticMethodInvoke(typeNode, typeNode.getName(), "newInstanceFrom",
//                varRef(typeNode, "source"), treeMaker.Literal(true));
//        blockBuilder.appendReturn(createNewInstance);
//
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withName("newInstanceFrom")
//                .withParameters(List.of(sourceVar))
//                .withReturnType(treeMaker.Ident(typeNode.toName(typeNode.getName())))
//                .withBody(blockBuilder.build())
//                .buildWith(typeNode);
//    }
//
//    // public static final RelationshipTest.TestRelativeModel newInstanceFrom(Map source, boolean underline) {...}
//    private JCTree.JCMethodDecl handleNewInstanceFrom2Method(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        JCVariableDecl sourceVar = createParameter(typeNode,
//                genTypeRef(typeNode, Map.class.getName()), "source");
//        JCVariableDecl underlineVar = createParameter(typeNode,
//                treeMaker.TypeIdent(CTC_BOOLEAN), "underline");
//
//        // RelationshipTest.TestRelativeModel target = ClassUtils.createNewInstance(RelationshipTest.TestRelativeModel.class);
//        JCTree.JCExpression createNewInstance = staticMethodInvoke(typeNode,
//                ClassUtils.class, "createNewInstance", classRef(typeNode, typeNode.getName()));
//        blockBuilder.appendVar(typeNode.getName(), "target", createNewInstance);
//
//        // PropertyUtils.populate(target, source, underline);
//        blockBuilder.appendStaticMethodInvoke(PropertyUtils.class, "populate",
//                varRef(typeNode, "target"), varRef(typeNode, "source"), varRef(typeNode, "underline"));
//
//        // return target;
//        blockBuilder.appendReturn("target");
//
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withName("newInstanceFrom")
//                .withParameters(List.of(sourceVar, underlineVar))
//                .withReturnType(treeMaker.Ident(typeNode.toName(typeNode.getName())))
//                .withBody(blockBuilder.build())
//                .buildWith(typeNode);
//    }
//
//    // public static final List<Row> query(String sql, Object... params) throws SQLException {...}
//    private JCTree.JCMethodDecl handleQueryMethod(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        JCVariableDecl sqlVar = MethodBuilder.createParameter(typeNode, String.class, "sql");
//        JCVariableDecl paramsVar = createParameter(typeNode, Flags.PARAMETER | Flags.VARARGS,
//                treeMaker.TypeArray(genTypeRef(typeNode, Object.class.getName())), "params");
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//
//        JCExpression domainModelClassRef = treeMaker.Select(treeMaker.Ident(typeNode.toName(typeNode.getName())),
//                typeNode.toName("class"));
//        // return sqlExecutor.query(connection, sql, params);
//        blockBuilder.appendReturn(Table.class, "query", domainModelClassRef,
//                varRef(typeNode, "sql"), varRef(typeNode, "params"));
//
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withReturnType(java.util.List.class, treeMaker.Ident(typeNode.toName(typeNode.getName())))
//                .withName("query")
//                .withParameters(sqlVar, paramsVar)
//                .withThrowsClauses(SQLException.class)
//                .withBody(blockBuilder.build())
//                .buildWith(typeNode);
//    }
//
//    // public static final RelationshipTest.TestRelativeModel newInstanceFrom(Map source, boolean underline) {...}
//    private JCTree.JCMethodDecl handleValidateMethod(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        String violationClassName = Validator.Violation.class.getName().replace("$", ".");
//
//        blockBuilder.appendVar(Validator.class, "validator", Table.class, "getValidator");
//
//        blockBuilder.appendReturn("validator", "validate",
//                treeMaker.Ident(typeNode.toName("this")));
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.FINAL)
//                .withName("validate")
//                .withReturnType(treeMaker.TypeArray(genTypeRef(typeNode, violationClassName)))
//                .withBody(blockBuilder.build())
//                .buildWith(typeNode);
//    }
//
//    // public static final int count(String predicate, Object... params) throws SQLException {...}
//    private JCTree.JCMethodDecl handleCountMethod(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        JCVariableDecl predicateVar = createParameter(typeNode,
//                genTypeRef(typeNode, String.class.getName()), "predicate");
//        JCVariableDecl paramsVar = createParameter(typeNode, Flags.PARAMETER | Flags.VARARGS,
//                treeMaker.TypeArray(genTypeRef(typeNode, Object.class.getName())), "params");
//        JCExpression domainModelClassRef = treeMaker.Select(treeMaker.Ident(typeNode.toName(typeNode.getName())),
//                typeNode.toName("class"));
//
//        // return Table.count(RelationshipTest.TestDomainModel.class, predicate, params);
//        blockBuilder.appendReturn(Table.class, "count", domainModelClassRef,
//                varRef(typeNode,"predicate"), varRef(typeNode, "params"));
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withName("count")
//                .withParameters(predicateVar, paramsVar)
//                .withThrowsClauses(SQLException.class)
//                .withReturnType(treeMaker.TypeIdent(CTC_INT))
//                .withBody(blockBuilder.build())
//                .buildWith(typeNode);
//    }
//
//    // public final static int count(String predicate) throws SQLException {...}
//    private JCTree.JCMethodDecl handleFindFirstMethod(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        StatementBuilder blockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        JCVariableDecl predicateVar = createParameter(typeNode,
//                genTypeRef(typeNode, String.class.getName()), "predicate");
//        JCVariableDecl paramsVar = createParameter(typeNode, Flags.PARAMETER | Flags.VARARGS,
//                treeMaker.TypeArray(genTypeRef(typeNode, Object.class.getName())), "params");
//        // Query<RelationshipTest.TestDomainModel> query = createQuery();
//        // query.where(predicate, params);
//        blockBuilder.appendVar(treeMaker.TypeApply(genTypeRef(typeNode, Query.class.getName()),
//                List.of(treeMaker.Ident(typeNode.toName(typeNode.getName())))), "query",
//                treeMaker.Apply(List.nil(), treeMaker.Ident(typeNode.toName("createQuery")), List.nil()));
//        blockBuilder.inject(treeMaker.Exec(treeMaker.Apply(List.nil(), treeMaker.Select(varRef(typeNode, "query"),
//                typeNode.toName("where")), List.of(varRef(typeNode, "predicate"), varRef(typeNode, "params")))));
//
//        // return (RelationshipTest.TestDomainModel)query.queryFirst(new Relationship[0]);
//        blockBuilder.appendReturn("query", "queryFirst");
//        return MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withName("queryFirst")
//                .withParameters(predicateVar, paramsVar)
//                .withThrowsClauses(SQLException.class)
//                .withReturnType(treeMaker.Ident(typeNode.toName(typeNode.getName())))
//                .withBody(blockBuilder.build())
//                .buildWith(typeNode);
//    }
//
//    private void addPersistenceRefStatement(JavacTreeMaker treeMaker, APTUtils typeNode,
//                                            StatementBuilder blockBuilder) {
//        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
//        blockBuilder.appendVar(PersistenceFactory.class, "persistenceFactory",
//                Database.class, "getPersistenceFactory");
//
//        //Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestRelativeModel.class);
//        JCExpression createPersistence = staticMethodInvoke(typeNode,
//                "persistenceFactory", "createPersistence", classRef(typeNode, typeNode.getName()));
//        JCTree.JCTypeApply typeApply = treeMaker.TypeApply(genTypeRef(typeNode, Persistence.class.getName()),
//                List.of(treeMaker.Ident(typeNode.toName(typeNode.getName()))));
//        blockBuilder.appendVar(typeApply, "persistence", createPersistence);
//    }
//
//    private JCVariableDecl createIdField(JavacTreeMaker treeMaker, APTUtils typeNode, DomainModel domainModel) {
//        JCAnnotation annotation = treeMaker.Annotation(chainDots(typeNode, splitNameOf(PrimaryKey.class)),
//                List.of(treeMaker.Assign(treeMaker.Ident(typeNode.toName("name")),
//                        treeMaker.Literal(domainModel.primaryColumnName()))));
//
//        return FieldBuilder.newField(typeNode)
//                .ofType(domainModel.primaryClass())
//                .withModifiers(Flags.PRIVATE)
//                .withAnnotations(annotation)
//                .withName(domainModel.primaryFieldName())
//                .build();
//    }
//
//    // public static final String TABLE_NAME = Table.getTableName(Domains.OrderLine.class);
//    private void handleTableNameField(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        JCExpression tableRef = genTypeRef(typeNode, Table.class.getName());
//        JCExpression getTableRef = treeMaker.Select(tableRef, typeNode.toName("getTableName"));
//        JCExpression paramRef = treeMaker.Select(treeMaker.Ident(typeNode.toName(typeNode.getName())), typeNode.toName("class"));
//        JCTree.JCMethodInvocation getTableInv = treeMaker.Apply(List.nil(), getTableRef, List.of(paramRef));
//        JCVariableDecl variableDecl = FieldBuilder.newField(typeNode)
//                .ofType(genJavaLangTypeRef(typeNode, String.class.getSimpleName()))
//                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
//                .withName("TABLE_NAME")
//                .withInit(getTableInv)
//                .build();
//
//        injectField(typeNode, variableDecl);
//    }
//
//    // @Volatile private final Map<String, Object> rawAttributes = new HashMap();
//    private void handleRawAttributesField(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        JCExpression rawAttributesType = treeMaker.TypeApply(genTypeRef(typeNode, Map.class.getName()),
//                List.of(genTypeRef(typeNode, String.class.getName()), genTypeRef(typeNode, Object.class.getName())));
//        JCExpression rawAttributesInit = treeMaker.NewClass(null, List.nil(), genTypeRef(typeNode, HashMap.class.getName()),
//                List.nil(), null);
//        injectField(typeNode, FieldBuilder.newField(typeNode)
//                .ofType(rawAttributesType)
//                .withModifiers(Flags.PRIVATE | Flags.FINAL)
//                .withAnnotations(treeMaker.Annotation(genTypeRef(typeNode, Volatile.class.getName()), List.nil()))
//                .withName("rawAttributes")
//                .withInit(rawAttributesInit)
//                .build());
//
//        // return Collections.unmodifiableMap(this.rawAttributes);
//        StatementBuilder getRawAttributesBlockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        getRawAttributesBlockBuilder.appendReturn(Collections.class, "unmodifiableMap",
//                treeMaker.Select(varRef(typeNode, "this"),
//                        typeNode.toName("rawAttributes")));
//        injectMethod(typeNode, MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.FINAL)
//                .withName("getRawAttributes")
//                .withReturnType(rawAttributesType)
//                .withBody(getRawAttributesBlockBuilder.build())
//                .buildWith(typeNode));
//
//        // return this.rawAttributes.get(name);
//        JCVariableDecl nameVar = MethodBuilder.createParameter(typeNode, String.class, "name");
//        StatementBuilder getRawAttributeBlockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        getRawAttributeBlockBuilder.appendReturn("rawAttributes", "get", varRef(typeNode, "name"));
//        injectMethod(typeNode, MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.FINAL)
//                .withName("getRawAttribute")
//                .withParameters(nameVar)
//                .withReturnType(genTypeRef(typeNode, Object.class.getName()))
//                .withBody(getRawAttributeBlockBuilder.build())
//                .buildWith(typeNode));
//
//        // this.rawAttributes.put(name, value);
//        JCVariableDecl valueVar = MethodBuilder.createParameter(typeNode, Object.class, "value");
//        StatementBuilder setRawAttributeBlockBuilder = StatementBuilder.newBlock(treeMaker, typeNode);
//        JCTree.JCExpression putRef = treeMaker.Select(treeMaker.Ident(typeNode.toName("rawAttributes")),
//                typeNode.toName("put"));
//        setRawAttributeBlockBuilder.inject(treeMaker.Exec(treeMaker.Apply(List.nil(), putRef,
//                List.of(varRef(typeNode, "name"), varRef(typeNode, "value")))));
//        injectMethod(typeNode, MethodBuilder.newMethod(treeMaker, typeNode)
//                .withModifiers(Flags.PUBLIC | Flags.FINAL)
//                .withName("setRawAttribute")
//                .withParameters(nameVar, valueVar)
//                .withBody(setRawAttributeBlockBuilder.build())
//                .buildWith(typeNode));
//    }
//
//    private JCVariableDecl createParameter(APTUtils typeNode, JCExpression type, String name, JCTree.JCAnnotation... annotations) {
//        return FieldBuilder.newField(typeNode)
//                .ofType(type)
//                .withName(name)
//                .withAnnotations(annotations)
//                .withModifiers(Flags.PARAMETER)
//                .build();
//    }
//
//    private JCVariableDecl createParameter(APTUtils typeNode, long flags, JCExpression type, String name, JCTree.JCAnnotation... annotations) {
//        return FieldBuilder.newField(typeNode)
//                .ofType(type)
//                .withName(name)
//                .withAnnotations(annotations)
//                .withModifiers(flags)
//                .build();
//    }

}
