package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.*;
import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.annotations.Transient;
import com.github.braisdom.funcsql.apt.*;
import com.github.braisdom.funcsql.apt.MethodBuilder;
import com.github.braisdom.funcsql.reflection.ClassUtils;
import com.github.braisdom.funcsql.reflection.PropertyUtils;
import com.github.braisdom.funcsql.relation.Relationship;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import org.mangosdk.spi.ProviderFor;

import java.sql.SQLException;
import java.util.HashMap;
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
        handleRawAttributesField(aptUtils);
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

        methodBuilder.setReturnStatement(Table.class, "execute",
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
        TreeMaker treeMaker = aptUtils.getTreeMaker();

        JCTree.JCExpression methodRef = treeMaker.Select(aptUtils.typeRef(Table.class),
                aptUtils.toName("validate"));
        JCReturn jcReturn = treeMaker.Return(treeMaker.Apply(List.nil(), methodRef, List.of(aptUtils.varRef("this"),
                aptUtils.getTreeMaker().Literal(true))));
        JCCatch jcCatch = treeMaker.Catch(aptUtils.newVar(ValidationException.class, "ex"),
                treeMaker.Block(0, List.of(treeMaker.Return(aptUtils.methodCall("ex", "getViolations")))));

        JCTry jcTry = treeMaker.Try(treeMaker.Block(0, List.of(jcReturn)), List.of(jcCatch),
                treeMaker.Block(0, List.nil()));

        aptUtils.inject(methodBuilder
                .setReturnType(aptUtils.newArrayType(Validator.Violation.class))
                .addStatement(jcTry)
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

    private void handleRawAttributesField(APTUtils aptUtils) {
        TreeMaker treeMaker = aptUtils.getTreeMaker();
        JCExpression rawAttributesType = treeMaker.TypeApply(aptUtils.typeRef(Map.class),
                List.of(aptUtils.typeRef(String.class), aptUtils.typeRef(Object.class)));
        JCExpression rawAttributesInit = treeMaker.NewClass(null, List.nil(), aptUtils.typeRef(HashMap.class.getName()),
                List.nil(), null);
        JCModifiers modifiers = treeMaker.Modifiers(Flags.PRIVATE | Flags.FINAL);
        modifiers.annotations = modifiers.annotations.append(treeMaker.Annotation(aptUtils.typeRef(Transient.class), List.nil()));

        aptUtils.inject(treeMaker.VarDef(modifiers, aptUtils.toName("rawAttributes"), rawAttributesType, rawAttributesInit));

        MethodBuilder getRawAttributeMethodBuilder = aptUtils.createMethodBuilder();
        JCReturn getRawAttributeReturn = treeMaker.Return(aptUtils
                .methodCall("rawAttributes", "get", aptUtils.varRef("name")));
        aptUtils.inject(getRawAttributeMethodBuilder
                .addStatement(getRawAttributeReturn)
                .addParameter("name", String.class)
                .setReturnType(aptUtils.typeRef(Object.class))
                .build("getRawAttribute", Flags.PUBLIC | Flags.FINAL));

        MethodBuilder setRawAttributeMethodBuilder = aptUtils.createMethodBuilder();
        JCExpression setRawAttributeExpression = aptUtils.methodCall("rawAttributes", "put",
                aptUtils.varRef("name"), aptUtils.varRef("value"));
        aptUtils.inject(setRawAttributeMethodBuilder
                .addStatement(treeMaker.Exec(setRawAttributeExpression))
                .addParameter("name", String.class)
                .addParameter("value", Object.class)
                .build("setRawAttribute", Flags.PUBLIC | Flags.FINAL));

        MethodBuilder getRawAttributesMethodBuilder = aptUtils.createMethodBuilder();
        JCReturn getRawAttributesReturn = treeMaker.Return(aptUtils.varRef("rawAttributes"));
        aptUtils.inject(getRawAttributesMethodBuilder
                .addStatement(getRawAttributesReturn)
                .setReturnType(aptUtils.newGenericsType(Map.class, String.class, Object.class))
                .build("getRawAttributes", Flags.PUBLIC | Flags.FINAL));
    }
}
