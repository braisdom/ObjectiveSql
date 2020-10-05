package com.github.braisdom.objsql.apt;

import com.github.braisdom.objsql.*;
import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.PrimaryKey;
import com.github.braisdom.objsql.annotations.Transient;
import com.github.braisdom.objsql.sql.AbstractTable;
import com.github.braisdom.objsql.sql.Column;
import com.github.braisdom.objsql.sql.DefaultColumn;
import com.github.braisdom.objsql.reflection.ClassUtils;
import com.github.braisdom.objsql.reflection.PropertyUtils;
import com.github.braisdom.objsql.relation.Relationship;
import com.github.braisdom.objsql.sql.Select;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import org.mangosdk.spi.ProviderFor;

import javax.annotation.processing.Processor;
import java.lang.annotation.Annotation;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@ProviderFor(Processor.class)
public class DomainModelCodeGenerator extends DomainModelProcessor {

    @Override
    public void handle(AnnotationValues annotationValues, JCTree ast, APTBuilder aptBuilder) {
        handleSetterGetter(annotationValues, aptBuilder);
        handlePrimary(annotationValues, aptBuilder);
        handleTableName(aptBuilder);
        handleCreateQueryMethod(aptBuilder);
        handleCreateSelectMethod(aptBuilder);
        handleCreatePersistenceMethod(aptBuilder);
        handleSaveMethod(aptBuilder);
        handleSave2Method(aptBuilder);
        handleCreateMethod(aptBuilder);
        handleCreate2Method(aptBuilder);
        handleCreateArrayMethod(aptBuilder);
        handleCreateArray2Method(aptBuilder);
        handleUpdateMethod(annotationValues, aptBuilder);
        handleUpdate2Method(aptBuilder);
        handleDestroyMethod(annotationValues, aptBuilder);
        handleDestroy2Method(aptBuilder);
        handleExecuteMethod(aptBuilder);
        handleQueryMethod(aptBuilder);
        handleQuery2Method(aptBuilder);
        handleQuery3Method(aptBuilder);
        handleQueryFirstMethod(aptBuilder);
        handleQueryFirst2Method(aptBuilder);
        handleQueryAllMethod(aptBuilder);
        handleCountMethod(aptBuilder);
        handleCountAllMethod(aptBuilder);
        handleValidateMethod(aptBuilder);
        handleNewInstanceFromMethod(aptBuilder);
        handleNewInstanceFrom1Method(aptBuilder);
        handleRawAttributesField(aptBuilder);
        handleInnerTableClass(aptBuilder);
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return DomainModel.class;
    }

    private void handleSetterGetter(AnnotationValues annotationValues, APTBuilder aptBuilder) {
        JCVariableDecl[] fields = aptBuilder.getFields();
        DomainModel domainModel = annotationValues.getAnnotationValue(DomainModel.class);
        aptBuilder.getTreeMaker().at(aptBuilder.get().pos);
        for (JCVariableDecl field : fields) {
            if (!aptBuilder.isStatic(field.mods)) {
                JCTree.JCMethodDecl setter = aptBuilder.newSetter(field, domainModel.fluent());
                JCTree.JCMethodDecl getter = aptBuilder.newGetter(field);

                aptBuilder.inject(setter);
                aptBuilder.inject(getter);
            }
        }
    }

    private void handlePrimary(AnnotationValues annotationValues, APTBuilder aptBuilder) {
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        DomainModel domainModel = annotationValues.getAnnotationValue(DomainModel.class);

        JCTree.JCAnnotation annotation = treeMaker.Annotation(aptBuilder.typeRef(PrimaryKey.class),
                List.of(treeMaker.Assign(treeMaker.Ident(aptBuilder.toName("name")),
                        treeMaker.Literal(domainModel.primaryColumnName()))));
        JCModifiers modifiers = treeMaker.Modifiers(Flags.PRIVATE);
        modifiers.annotations = modifiers.annotations.append(annotation);

        JCVariableDecl primaryField = treeMaker.VarDef(modifiers,
                aptBuilder.toName(domainModel.primaryFieldName()), aptBuilder.typeRef(domainModel.primaryClass()), null);
        JCMethodDecl queryByPrimaryKey = createQueryByPrimaryKeyMethod(domainModel, primaryField, aptBuilder);

        aptBuilder.inject(primaryField);
        aptBuilder.inject(queryByPrimaryKey);
        aptBuilder.inject(aptBuilder.newSetter(primaryField, domainModel.fluent()));
        aptBuilder.inject(aptBuilder.newGetter(primaryField));
    }

    private JCMethodDecl createQueryByPrimaryKeyMethod(DomainModel domainModel, JCVariableDecl primaryField, APTBuilder aptBuilder) {
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        statementBuilder.append(aptBuilder.newGenericsType(Query.class,
                aptBuilder.getClassName()), "query", "createQuery");
        statementBuilder.append("query", "where",
                List.of(treeMaker.Literal(String.format("%s = ?", domainModel.primaryColumnName())), aptBuilder.varRef("primaryKey")));

        methodBuilder.setReturnStatement("query", "queryFirst",
                aptBuilder.varRef("relationships"));
        return methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("primaryKey", primaryField.vartype)
                .addVarargsParameter("relationships", aptBuilder.typeRef(Relationship.class))
                .setThrowsClauses(SQLException.class)
                .setReturnType(aptBuilder.typeRef(aptBuilder.getClassName()))
                .build("queryByPrimaryKey", Flags.PUBLIC | Flags.STATIC | Flags.FINAL);
    }

    private void handleTableName(APTBuilder aptBuilder) {
        TreeMaker treeMaker = aptBuilder.getTreeMaker();

        JCModifiers modifiers = treeMaker.Modifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL);

        JCMethodInvocation methodInvocation = treeMaker.Apply(List.nil(),
                treeMaker.Select(aptBuilder.typeRef(Tables.class), aptBuilder.toName("getTableName")),
                List.of(aptBuilder.classRef(aptBuilder.getClassName())));
        JCVariableDecl tableNameField = treeMaker.VarDef(modifiers,
                aptBuilder.toName("TABLE_NAME"), aptBuilder.typeRef(String.class), methodInvocation);

        aptBuilder.inject(tableNameField);
    }

    private void handleCreateQueryMethod(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        statementBuilder.append(aptBuilder.typeRef(QueryFactory.class), "queryFactory", Databases.class,
                "getQueryFactory", List.nil());

        methodBuilder.setReturnStatement("queryFactory", "createQuery", aptBuilder.classRef(aptBuilder.getClassName()));

        aptBuilder.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .setReturnType(Query.class, aptBuilder.typeRef(aptBuilder.getClassName()))
                .build("createQuery", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleCreateSelectMethod(APTBuilder aptBuilder) {
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        methodBuilder.setReturnStatement(treeMaker.NewClass(null, List.nil(),
                aptBuilder.typeRef(Select.class), List.of(aptBuilder.methodCall("asTable")), null));

        aptBuilder.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .setReturnType(Select.class, aptBuilder.typeRef(aptBuilder.getClassName()))
                .build("createSelect", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleCreatePersistenceMethod(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        statementBuilder.append(aptBuilder.typeRef(PersistenceFactory.class), "persistenceFactory", Databases.class,
                "getPersistenceFactory", List.nil());

        methodBuilder.setReturnStatement("persistenceFactory", "createPersistence",
                aptBuilder.classRef(aptBuilder.getClassName()));

        aptBuilder.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .setReturnType(Persistence.class, aptBuilder.typeRef(aptBuilder.getClassName()))
                .build("createPersistence", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleSave2Method(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        TreeMaker treeMaker = aptBuilder.getTreeMaker();

        methodBuilder.setReturnStatement("this", "save",
                aptBuilder.varRef("skipValidation"), treeMaker.Literal(false));
        aptBuilder.inject(methodBuilder
                .addParameter("skipValidation", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .setReturnType(aptBuilder.typeRef(aptBuilder.getClassName()))
                .setThrowsClauses(SQLException.class)
                .build("save", Flags.PUBLIC | Flags.FINAL));
    }

    private void handleSaveMethod(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        statementBuilder.append(aptBuilder.typeRef(PersistenceFactory.class),
                "persistenceFactory", Databases.class,
                "getPersistenceFactory", List.nil());

        statementBuilder.append(aptBuilder.newGenericsType(Persistence.class, aptBuilder.getClassName()),
                "persistence", "persistenceFactory", "createPersistence",
                treeMaker.NewClass(null, List.nil(),
                        aptBuilder.typeRef(BeanModelDescriptor.class),
                        List.of(aptBuilder.classRef(aptBuilder.getClassName()),
                                aptBuilder.varRef("skipPrimaryKeyOnInserting")), null));

        methodBuilder.setReturnStatement("persistence", "save",
                aptBuilder.varRef("this"), aptBuilder.varRef("skipValidation"));
        aptBuilder.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("skipValidation", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .addParameter("skipPrimaryKeyOnInserting", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .setReturnType(aptBuilder.typeRef(aptBuilder.getClassName()))
                .setThrowsClauses(SQLException.class)
                .build("save", Flags.PUBLIC | Flags.FINAL));
    }

    private void handleCreateMethod(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        statementBuilder.append(aptBuilder.typeRef(PersistenceFactory.class),
                "persistenceFactory", Databases.class,
                "getPersistenceFactory", List.nil());

        statementBuilder.append(aptBuilder.newGenericsType(Persistence.class, aptBuilder.getClassName()),
                "persistence", "persistenceFactory", "createPersistence",
                treeMaker.NewClass(null, List.nil(),
                        aptBuilder.typeRef(BeanModelDescriptor.class),
                        List.of(aptBuilder.classRef(aptBuilder.getClassName()),
                                aptBuilder.varRef("skipPrimaryKeyOnInserting")), null));

        methodBuilder.setReturnStatement("persistence", "insert",
                aptBuilder.varRef("dirtyObject"), aptBuilder.varRef("skipValidation"));

        aptBuilder.inject(methodBuilder
                .setReturnType(aptBuilder.typeRef(aptBuilder.getClassName()))
                .addStatements(statementBuilder.build())
                .addParameter("dirtyObject", aptBuilder.typeRef(aptBuilder.getClassName()))
                .addParameter("skipValidation", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .addParameter("skipPrimaryKeyOnInserting", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .setThrowsClauses(SQLException.class)
                .build("create", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleCreate2Method(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        methodBuilder.setReturnStatement(aptBuilder.getClassName(), "create",
                aptBuilder.varRef("dirtyObject"), aptBuilder.varRef("skipValidation"),
                treeMaker.Literal(false));

        aptBuilder.inject(methodBuilder
                .setReturnType(aptBuilder.typeRef(aptBuilder.getClassName()))
                .addStatements(statementBuilder.build())
                .addParameter("dirtyObject", aptBuilder.typeRef(aptBuilder.getClassName()))
                .addParameter("skipValidation", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .setThrowsClauses(SQLException.class)
                .build("create", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleCreateArrayMethod(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        statementBuilder.append(aptBuilder.typeRef(PersistenceFactory.class),
                "persistenceFactory", Databases.class,
                "getPersistenceFactory", List.nil());

        statementBuilder.append(aptBuilder.newGenericsType(Persistence.class, aptBuilder.getClassName()),
                "persistence", "persistenceFactory", "createPersistence",
                treeMaker.NewClass(null, List.nil(),
                        aptBuilder.typeRef(BeanModelDescriptor.class),
                        List.of(aptBuilder.classRef(aptBuilder.getClassName()),
                                aptBuilder.varRef("skipPrimaryKeyOnInserting")), null));

        methodBuilder.setReturnStatement("persistence", "insert",
                aptBuilder.varRef("dirtyObjects"), aptBuilder.varRef("skipValidation"));

        aptBuilder.inject(methodBuilder
                .setReturnType(aptBuilder.newArrayType(treeMaker.TypeIdent(TypeTag.INT)))
                .addStatements(statementBuilder.build())
                .addParameter("dirtyObjects", aptBuilder.newArrayType(aptBuilder.getClassName()))
                .addParameter("skipValidation", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .addParameter("skipPrimaryKeyOnInserting", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .setThrowsClauses(SQLException.class)
                .build("create", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleCreateArray2Method(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        methodBuilder.setReturnStatement(aptBuilder.getClassName(), "create",
                aptBuilder.varRef("dirtyObjects"), aptBuilder.varRef("skipValidation"),
                treeMaker.Literal(false));

        aptBuilder.inject(methodBuilder
                .setReturnType(aptBuilder.newArrayType(treeMaker.TypeIdent(TypeTag.INT)))
                .addStatements(statementBuilder.build())
                .addParameter("dirtyObjects", aptBuilder.newArrayType(aptBuilder.getClassName()))
                .addParameter("skipValidation", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .setThrowsClauses(SQLException.class)
                .build("create", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleUpdateMethod(AnnotationValues annotationValues, APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();
        DomainModel domainModel = annotationValues.getAnnotationValue(DomainModel.class);

        statementBuilder.append(aptBuilder.newGenericsType(Persistence.class, aptBuilder.getClassName()), "persistence",
                "createPersistence");

        methodBuilder.setReturnStatement("persistence", "update",
                aptBuilder.varRef("id"), aptBuilder.varRef("dirtyObject"), aptBuilder.varRef("skipValidation"));

        aptBuilder.inject(methodBuilder
                .setReturnType(aptBuilder.typeRef(aptBuilder.getClassName()))
                .addStatements(statementBuilder.build())
                .addParameter("id", aptBuilder.typeRef(domainModel.primaryClass()))
                .addParameter("dirtyObject", aptBuilder.typeRef(aptBuilder.getClassName()))
                .addParameter("skipValidation", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .setThrowsClauses(SQLException.class)
                .build("update", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleUpdate2Method(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        statementBuilder.append(aptBuilder.newGenericsType(Persistence.class, aptBuilder.getClassName()), "persistence",
                "createPersistence");

        methodBuilder.setReturnStatement("persistence", "update",
                aptBuilder.varRef("updates"), aptBuilder.varRef("predicates"));

        aptBuilder.inject(methodBuilder
                .setReturnType(treeMaker.TypeIdent(TypeTag.INT))
                .addStatements(statementBuilder.build())
                .addParameter("updates", aptBuilder.typeRef(String.class))
                .addParameter("predicates", aptBuilder.typeRef(String.class))
                .setThrowsClauses(SQLException.class)
                .build("update", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleDestroyMethod(AnnotationValues annotationValues, APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();
        DomainModel domainModel = annotationValues.getAnnotationValue(DomainModel.class);

        statementBuilder.append(aptBuilder.newGenericsType(Persistence.class, aptBuilder.getClassName()), "persistence",
                "createPersistence");

        methodBuilder.setReturnStatement("persistence", "delete",
                aptBuilder.varRef("id"));

        aptBuilder.inject(methodBuilder
                .setReturnType(treeMaker.TypeIdent(TypeTag.INT))
                .addStatements(statementBuilder.build())
                .addParameter("id", aptBuilder.typeRef(domainModel.primaryClass()))
                .setThrowsClauses(SQLException.class)
                .build("destroy", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleDestroy2Method(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        statementBuilder.append(aptBuilder.newGenericsType(Persistence.class, aptBuilder.getClassName()), "persistence",
                "createPersistence");

        methodBuilder.setReturnStatement("persistence", "delete",
                aptBuilder.varRef("predicate"));

        aptBuilder.inject(methodBuilder
                .setReturnType(treeMaker.TypeIdent(TypeTag.INT))
                .addStatements(statementBuilder.build())
                .addParameter("predicate", aptBuilder.typeRef(String.class))
                .setThrowsClauses(SQLException.class)
                .build("destroy", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleExecuteMethod(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();

        methodBuilder.setReturnStatement(Tables.class, "execute",
                aptBuilder.classRef(aptBuilder.getClassName()), aptBuilder.varRef("sql"), aptBuilder.varRef("params"));

        aptBuilder.inject(methodBuilder
                .setReturnType(aptBuilder.getTreeMaker().TypeIdent(TypeTag.INT))
                .addParameter("sql", aptBuilder.typeRef(String.class))
                .addVarargsParameter("params", aptBuilder.typeRef(Object.class))
                .setThrowsClauses(SQLException.class)
                .build("execute", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleQueryMethod(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        statementBuilder.append(aptBuilder.newGenericsType(Query.class, aptBuilder.getClassName()), "query",
                "createQuery");
        statementBuilder.append("query", "where",
                List.of(aptBuilder.varRef("predicate"), aptBuilder.varRef("params")));

        methodBuilder.setReturnStatement("query", "execute");
        aptBuilder.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("predicate", aptBuilder.typeRef(String.class))
                .addVarargsParameter("params", aptBuilder.typeRef(Object.class))
                .setThrowsClauses(SQLException.class)
                .setReturnType(java.util.List.class, aptBuilder.typeRef(aptBuilder.getClassName()))
                .build("query", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleQuery2Method(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        statementBuilder.append(aptBuilder.newGenericsType(Query.class, aptBuilder.getClassName()), "query",
                "createQuery");
        statementBuilder.append("query", "where",
                List.of(aptBuilder.varRef("predicate"), aptBuilder.varRef("params")));

        methodBuilder.setReturnStatement("query", "execute", aptBuilder.varRef("relations"));
        aptBuilder.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("predicate", aptBuilder.typeRef(String.class))
                .addArrayParameter("relations", Relationship.class)
                .addVarargsParameter("params", aptBuilder.typeRef(Object.class))
                .setThrowsClauses(SQLException.class)
                .setReturnType(java.util.List.class, aptBuilder.typeRef(aptBuilder.getClassName()))
                .build("query", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleQuery3Method(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        methodBuilder.setReturnStatement(Tables.class, "query", aptBuilder.classRef(aptBuilder.getClassName()),
                aptBuilder.varRef("sql"), aptBuilder.varRef("params"));
        aptBuilder.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("sql", aptBuilder.typeRef(String.class))
                .addVarargsParameter("params", aptBuilder.typeRef(Object.class))
                .setThrowsClauses(SQLException.class)
                .setReturnType(java.util.List.class, aptBuilder.typeRef(aptBuilder.getClassName()))
                .build("queryBySql", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleQueryFirstMethod(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        statementBuilder.append(aptBuilder.newGenericsType(Query.class, aptBuilder.getClassName()), "query",
                "createQuery");
        statementBuilder.append("query", "where",
                List.of(aptBuilder.varRef("predicate"), aptBuilder.varRef("params")));

        methodBuilder.setReturnStatement("query", "queryFirst", aptBuilder.varRef("relations"));
        aptBuilder.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("predicate", aptBuilder.typeRef(String.class))
                .addArrayParameter("relations", Relationship.class)
                .addVarargsParameter("params", aptBuilder.typeRef(Object.class))
                .setThrowsClauses(SQLException.class)
                .setReturnType(aptBuilder.typeRef(aptBuilder.getClassName()))
                .build("queryFirst", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleQueryFirst2Method(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        statementBuilder.append(aptBuilder.newGenericsType(Query.class, aptBuilder.getClassName()), "query",
                "createQuery");
        statementBuilder.append("query", "where",
                List.of(aptBuilder.varRef("predicate"), aptBuilder.varRef("params")));

        methodBuilder.setReturnStatement("query", "queryFirst");
        aptBuilder.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("predicate", aptBuilder.typeRef(String.class))
                .addVarargsParameter("params", aptBuilder.typeRef(Object.class))
                .setThrowsClauses(SQLException.class)
                .setReturnType(aptBuilder.typeRef(aptBuilder.getClassName()))
                .build("queryFirst", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleQueryAllMethod(APTBuilder aptBuilder) {
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        statementBuilder.append(aptBuilder.newGenericsType(Query.class, aptBuilder.getClassName()), "query",
                "createQuery");
        statementBuilder.append("query", "where",
                List.of(treeMaker.Literal("")));

        methodBuilder.setReturnStatement("query", "execute", aptBuilder.varRef("relations"));
        aptBuilder.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addVarargsParameter("relations", aptBuilder.typeRef(Relationship.class))
                .setThrowsClauses(SQLException.class)
                .setReturnType(java.util.List.class, aptBuilder.typeRef(aptBuilder.getClassName()))
                .build("queryAll", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleCountMethod(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();

        methodBuilder.setReturnStatement(Tables.class, "count", aptBuilder.classRef(aptBuilder.getClassName()),
                aptBuilder.varRef("predicate"), aptBuilder.varRef("params"));

        aptBuilder.inject(methodBuilder
                .addParameter("predicate", aptBuilder.typeRef(String.class))
                .addVarargsParameter("params", aptBuilder.typeRef(Object.class))
                .setThrowsClauses(SQLException.class)
                .setReturnType(aptBuilder.getTreeMaker().TypeIdent(TypeTag.LONG))
                .build("count", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleCountAllMethod(APTBuilder aptBuilder) {
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();

        methodBuilder.setReturnStatement(Tables.class, "count", aptBuilder.classRef(aptBuilder.getClassName()),
                treeMaker.Literal(""));

        aptBuilder.inject(methodBuilder
                .setThrowsClauses(SQLException.class)
                .setReturnType(aptBuilder.getTreeMaker().TypeIdent(TypeTag.LONG))
                .build("countAll", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleValidateMethod(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();

        methodBuilder.setReturnStatement(Tables.class, "validate", aptBuilder.varRef("this"));

        aptBuilder.inject(methodBuilder
                .setReturnType(aptBuilder.newArrayType(Validator.Violation.class))
                .build("validate", Flags.PUBLIC | Flags.FINAL));
    }

    private void handleNewInstanceFromMethod(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        JCExpression createInstance = treeMaker.TypeCast(aptBuilder.typeRef(aptBuilder.getClassName()),
                treeMaker.Apply(List.nil(), treeMaker.Select(aptBuilder.typeRef(ClassUtils.class),
                        aptBuilder.toName("createNewInstance")), List.of(aptBuilder.classRef(aptBuilder.getClassName()))));
        statementBuilder.append(aptBuilder.typeRef(aptBuilder.getClassName()), "bean", createInstance);

        statementBuilder.append(PropertyUtils.class, "populate", aptBuilder.varRef("bean"),
                aptBuilder.varRef("properties"), aptBuilder.varRef("underLine"));

        methodBuilder.setReturnStatement(aptBuilder.varRef("bean"));
        aptBuilder.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("properties", Map.class)
                .addParameter("underLine", treeMaker.TypeIdent(TypeTag.BOOLEAN))
                .setReturnType(aptBuilder.typeRef(aptBuilder.getClassName()))
                .build("newInstanceFrom", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleNewInstanceFrom1Method(APTBuilder aptBuilder) {
        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        methodBuilder.setReturnStatement(aptBuilder.getClassName(), "newInstanceFrom",
                aptBuilder.varRef("properties"), treeMaker.Literal(false));
        aptBuilder.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .addParameter("properties", Map.class)
                .setReturnType(aptBuilder.typeRef(aptBuilder.getClassName()))
                .build("newInstanceFrom", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    private void handleRawAttributesField(APTBuilder aptBuilder) {
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        JCExpression rawAttributesType = treeMaker.TypeApply(aptBuilder.typeRef(Map.class),
                List.of(aptBuilder.typeRef(String.class), aptBuilder.typeRef(Object.class)));
        JCExpression rawAttributesInit = treeMaker.NewClass(null, List.nil(), aptBuilder.typeRef(HashMap.class.getName()),
                List.nil(), null);
        JCModifiers modifiers = treeMaker.Modifiers(Flags.PRIVATE | Flags.FINAL);
        modifiers.annotations = modifiers.annotations.append(treeMaker.Annotation(aptBuilder.typeRef(Transient.class), List.nil()));

        aptBuilder.inject(treeMaker.VarDef(modifiers, aptBuilder.toName("rawAttributes"), rawAttributesType, rawAttributesInit));

        MethodBuilder getRawAttributeMethodBuilder = aptBuilder.createMethodBuilder();
        JCReturn getRawAttributeReturn = treeMaker.Return(aptBuilder
                .methodCall("rawAttributes", "get", aptBuilder.varRef("name")));
        aptBuilder.inject(getRawAttributeMethodBuilder
                .addStatement(getRawAttributeReturn)
                .addParameter("name", String.class)
                .setReturnType(aptBuilder.typeRef(Object.class))
                .build("getRawAttribute", Flags.PUBLIC | Flags.FINAL));

        MethodBuilder setRawAttributeMethodBuilder = aptBuilder.createMethodBuilder();
        JCExpression setRawAttributeExpression = aptBuilder.methodCall("rawAttributes", "put",
                aptBuilder.varRef("name"), aptBuilder.varRef("value"));
        aptBuilder.inject(setRawAttributeMethodBuilder
                .addStatement(treeMaker.Exec(setRawAttributeExpression))
                .addParameter("name", String.class)
                .addParameter("value", Object.class)
                .build("setRawAttribute", Flags.PUBLIC | Flags.FINAL));

        MethodBuilder getRawAttributesMethodBuilder = aptBuilder.createMethodBuilder();
        JCReturn getRawAttributesReturn = treeMaker.Return(aptBuilder.varRef("rawAttributes"));
        aptBuilder.inject(getRawAttributesMethodBuilder
                .addStatement(getRawAttributesReturn)
                .setReturnType(aptBuilder.newGenericsType(Map.class, String.class, Object.class))
                .build("getRawAttributes", Flags.PUBLIC | Flags.FINAL));
    }

    private void handleInnerTableClass(APTBuilder aptBuilder) {
        JCClassDecl classDecl = aptBuilder.classDef(Flags.PUBLIC | Flags.FINAL | Flags.STATIC,
                "Table", AbstractTable.class);
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        StatementBuilder constructorStatement = aptBuilder.createStatementBuilder();
        MethodBuilder asTableMethod = aptBuilder.createMethodBuilder();

        constructorStatement.append("super", aptBuilder.classRef(aptBuilder.getClassName()));
        JCMethodDecl constructor = aptBuilder.createConstructor(Flags.PRIVATE, List.nil(), constructorStatement.build());
        classDecl.defs = classDecl.defs.append(constructor);

        asTableMethod.setReturnType(aptBuilder.typeRef(aptBuilder.getClassName() + ".Table"));
        asTableMethod.setReturnStatement(treeMaker.NewClass(null, List.nil(), aptBuilder.typeRef("Table"),
                List.nil(), null));

        JCVariableDecl[] fields = aptBuilder.getFields();
        for (JCVariableDecl field : fields) {
            if (!aptBuilder.isStatic(field.mods)) {
                JCExpression init = aptBuilder.staticMethodCall(DefaultColumn.class, "create",
                        aptBuilder.classRef(aptBuilder.getClassName()),
                        aptBuilder.varRef("this"), treeMaker.Literal(field.name.toString()));
                JCVariableDecl var = aptBuilder.newVar(Flags.PUBLIC | Flags.FINAL,
                        Column.class, field.name.toString(), init);

                classDecl.defs = classDecl.defs.append(var);
            }
        }

        aptBuilder.inject(asTableMethod.build("asTable", Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
        aptBuilder.inject(classDecl);
    }
}
