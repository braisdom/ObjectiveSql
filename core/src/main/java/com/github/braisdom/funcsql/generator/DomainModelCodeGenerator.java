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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;

import static com.github.braisdom.funcsql.generator.BlockBuilder.*;
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
                handleQueryMethod(treeMaker, typeNode),
                handleValidateMethod(treeMaker, typeNode),
                handleCountMethod(treeMaker, typeNode),
                handleCount2Method(treeMaker, typeNode)
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

    // public final void save() throws SQLException {
    private JCTree.JCMethodDecl handleSave2Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);

        // this.save(false);
        blockBuilder.appendInstanceMethodInvoke("save", treeMaker.Literal(false));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.FINAL)
                .withName("save")
                .withBody(blockBuilder.build())
                .withThrowsClauses(SQLException.class)
                .buildWith(typeNode);
    }

    // public final void save(boolean skipValidation) throws SQLException {...}
    private JCTree.JCMethodDecl handleSaveMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);
        JCVariableDecl parameter = createParameter(typeNode, treeMaker.TypeIdent(CTC_BOOLEAN), "skipValidation");

        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
        // Persistence<RelationshipTest.TestDomainModel> persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);

        // persistence.save(this, skipValidation);
        blockBuilder.appendStaticMethodInvoke("persistence", "save",
                varRef(typeNode, "this"), varRef(typeNode, "skipValidation"));
        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.FINAL)
                .withName("save")
                .withParameters(List.of(parameter))
                .withBody(blockBuilder.build())
                .withThrowsClauses(SQLException.class)
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
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);

        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
        blockBuilder.appendVar(PersistenceFactory.class, "persistenceFactory",
                Database.class, "getPersistenceFactory");

        //Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestRelativeModel.class);
        JCExpression createPersistence = staticMethodInvoke(typeNode,
                "persistenceFactory", "createPersistence", classRef(typeNode, typeNode.getName()));
        JCTree.JCTypeApply typeApply = treeMaker.TypeApply(genTypeRef(typeNode, Persistence.class.getName()),
                List.of(treeMaker.Ident(typeNode.toName(typeNode.getName()))));
        blockBuilder.appendReturn(createPersistence);

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("createPersistence")
                .withReturnType(typeApply)
                .withBody(blockBuilder.build())
                .buildWith(typeNode);
    }

    // public static final RelationshipTest.TestDomainModel create(RelationshipTest.TestDomainModel dirtyObject,
    //                          boolean skipValidation) throws SQLException {...}
    private JCTree.JCMethodDecl handleCreateMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);
        Name modelClassName = typeNode.toName(typeNode.getName());
        JCVariableDecl dirtyObjectVar = createParameter(typeNode, treeMaker.Ident(modelClassName), "dirtyObject");
        JCVariableDecl skipValidationVar = createParameter(typeNode, treeMaker.TypeIdent(CTC_BOOLEAN), "skipValidation");

        // Table.validate(dirtyObject, skipValidation);
        blockBuilder.appendStaticMethodInvoke(Table.class, "validate",
                treeMaker.Ident(typeNode.toName("dirtyObject")), treeMaker.Ident(typeNode.toName("skipValidation")));

        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
        // Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);

        // return (RelationshipTest.TestDomainModel)persistence.insert(dirtyObject, skipValidation);
        blockBuilder.appendReturn("persistence", "insert",
                varRef(typeNode, "dirtyObject"), varRef(typeNode, "skipValidation"));
        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("create")
                .withParameters(List.of(dirtyObjectVar, skipValidationVar))
                .withThrowsClauses(SQLException.class)
                .withReturnType(treeMaker.Ident(modelClassName))
                .withBody(blockBuilder.build())
                .buildWith(typeNode);
    }

    // public static final RelationshipTest.TestDomainModel create(RelationshipTest.TestDomainModel dirtyObject)
    //          throws SQLException {
    private JCTree.JCMethodDecl handleCreate2Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);
        Name modelClassName = typeNode.toName(typeNode.getName());
        JCVariableDecl dirtyObjectVar = createParameter(typeNode, treeMaker.Ident(modelClassName), "dirtyObject");

        // return create(dirtyObject, false);
        blockBuilder.appendReturn(typeNode.getName(), "create", varRef(typeNode, "dirtyObject"),
                treeMaker.Literal(false));
        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.FINAL | Flags.STATIC)
                .withName("create")
                .withParameters(List.of(dirtyObjectVar))
                .withBody(blockBuilder.build())
                .withReturnType(typeNode.getName())
                .withThrowsClauses(SQLException.class)
                .buildWith(typeNode);
    }

    // public static final int[] create(RelationshipTest.TestDomainModel[] dirtyObjects) throws SQLException {...}
    private JCTree.JCMethodDecl handleCreateArray2Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);
        Name modelClassName = typeNode.toName(typeNode.getName());
        JCVariableDecl dirtyArrayObjectVar = createParameter(typeNode, treeMaker.TypeArray(treeMaker.Ident(modelClassName)),
                "dirtyObjects");

        // return create(dirtyObjects, false);
        blockBuilder.appendReturn(typeNode.getName(), "create", varRef(typeNode, "dirtyObjects"),
                treeMaker.Literal(false));
        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.FINAL | Flags.STATIC)
                .withName("create")
                .withParameters(List.of(dirtyArrayObjectVar))
                .withBody(blockBuilder.build())
                .withReturnType(treeMaker.TypeArray(treeMaker.TypeIdent(CTC_INT)))
                .withThrowsClauses(SQLException.class)
                .buildWith(typeNode);
    }

    // public static final int[] create(RelationshipTest.TestDomainModel[] dirtyObjects, boolean skipValidation) throws SQLException {
    private JCTree.JCMethodDecl handleCreateArrayMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);
        Name modelClassName = typeNode.toName(typeNode.getName());
        JCVariableDecl dirtyArrayObjectVar =  createParameter(typeNode,
                treeMaker.TypeArray(treeMaker.Ident(modelClassName)), "dirtyObjects");
        JCVariableDecl skipValidationVar =  createParameter(typeNode,
                treeMaker.TypeIdent(CTC_BOOLEAN), "skipValidation");

        // Table.validate(dirtyObjects, skipValidation);
        blockBuilder.appendStaticMethodInvoke(Table.class, "validate",
                treeMaker.Ident(typeNode.toName("dirtyObjects")), treeMaker.Ident(typeNode.toName("skipValidation")));

        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
        // Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);

        // return persistence.insert(dirtyObjects, skipValidation);
        blockBuilder.appendReturn("persistence", "insert",
                varRef(typeNode, "dirtyObjects"), varRef(typeNode, "skipValidation"));
        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("create")
                .withParameters(List.of(dirtyArrayObjectVar, skipValidationVar))
                .withReturnType(treeMaker.TypeArray(treeMaker.TypeIdent(CTC_INT)))
                .withThrowsClauses(SQLException.class)
                .withBody(blockBuilder.build())
                .buildWith(typeNode);
    }

    // public static final int update(Object id, RelationshipTest.TestDomainModel dirtyObject, boolean skipValidation) throws SQLException {
    private JCTree.JCMethodDecl handleUpdateMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);
        Name modelClassName = typeNode.toName(typeNode.getName());
        JCVariableDecl idVar = createParameter(typeNode, genJavaLangTypeRef(typeNode, Object.class.getSimpleName()), "id");
        JCVariableDecl dirtyObjectVar = createParameter(typeNode, treeMaker.Ident(modelClassName), "dirtyObject");
        JCVariableDecl skipValidationVar = createParameter(typeNode, treeMaker.TypeIdent(CTC_BOOLEAN), "skipValidation");

        // Table.validate(dirtyObject, skipValidation);
        blockBuilder.appendStaticMethodInvoke(Table.class, "validate",
                treeMaker.Ident(typeNode.toName("dirtyObject")), treeMaker.Ident(typeNode.toName("skipValidation")));

        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
        // Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);

        // return persistence.update(id, dirtyObject, skipValidation);
        blockBuilder.appendReturn("persistence", "update",
                varRef(typeNode, "id"), varRef(typeNode, "dirtyObject"), varRef(typeNode, "skipValidation"));
        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("update")
                .withParameters(List.of(idVar, dirtyObjectVar, skipValidationVar))
                .withThrowsClauses(SQLException.class)
                .withReturnType(treeMaker.TypeIdent(CTC_INT))
                .withBody(blockBuilder.build())
                .buildWith(typeNode);
    }

    // public static final int update(Object id, RelationshipTest.TestDomainModel dirtyObject) throws SQLException {...}
    private JCTree.JCMethodDecl handleUpdate2Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);
        JCVariableDecl idVar = createParameter(typeNode,
                genJavaLangTypeRef(typeNode, Object.class.getSimpleName()), "id");
        JCVariableDecl dirtyObjectVar = createParameter(typeNode,
                treeMaker.Ident(typeNode.toName(typeNode.getName())), "dirtyObject");

        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
        // Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);

        // return persistence.update(id, dirtyObject, false);
        blockBuilder.appendReturn("persistence", "update",
                varRef(typeNode, "id"), varRef(typeNode, "dirtyObject"), treeMaker.Literal(true));
        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.FINAL | Flags.STATIC)
                .withName("update")
                .withParameters(List.of(idVar, dirtyObjectVar))
                .withBody(blockBuilder.build())
                .withReturnType(treeMaker.TypeIdent(CTC_INT))
                .withThrowsClauses(SQLException.class)
                .buildWith(typeNode);
    }

    // public static final int update(String updates, String predication) throws SQLException {...}
    private JCTree.JCMethodDecl handleUpdate3Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);
        JCVariableDecl updatesVar = createParameter(typeNode,
                genJavaLangTypeRef(typeNode, String.class.getSimpleName()), "updates");
        JCVariableDecl predicationVar = createParameter(typeNode,
                genJavaLangTypeRef(typeNode, String.class.getSimpleName()), "predication");

        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
        // Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);

        // return persistence.update(updates, predication);
        blockBuilder.appendReturn("persistence", "update",
                varRef(typeNode, "updates"), varRef(typeNode, "predication"));
        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("update")
                .withParameters(List.of(updatesVar, predicationVar))
                .withThrowsClauses(SQLException.class)
                .withReturnType(treeMaker.TypeIdent(CTC_INT))
                .withBody(blockBuilder.build())
                .buildWith(typeNode);
    }

    // public static final int destroy(Object id) throws SQLException {...}
    private JCTree.JCMethodDecl handleDestroyMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);
        JCVariableDecl idVar = createParameter(typeNode,
                genJavaLangTypeRef(typeNode, Object.class.getSimpleName()), "id");

        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
        // Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);

        // return persistence.delete(id);
        blockBuilder.appendReturn("persistence", "delete", varRef(typeNode, "id"));
        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("destroy")
                .withParameters(idVar)
                .withThrowsClauses(SQLException.class)
                .withReturnType(treeMaker.TypeIdent(CTC_INT))
                .withBody(blockBuilder.build())
                .buildWith(typeNode);
    }

    // public static final int destroy(String predication) throws SQLException {...}
    private JCTree.JCMethodDecl handleDestroy2Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);
        JCVariableDecl predicationVar = createParameter(typeNode,
                genJavaLangTypeRef(typeNode, String.class.getSimpleName()), "predication");

        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
        // Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestDomainModel.class);
        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);

        // return persistence.delete(predication);
        blockBuilder.appendReturn("persistence", "delete", varRef(typeNode, "predication"));
        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("destroy")
                .withParameters(List.of(predicationVar))
                .withThrowsClauses(SQLException.class)
                .withReturnType(treeMaker.TypeIdent(CTC_INT))
                .withBody(blockBuilder.build())
                .buildWith(typeNode);
    }

    // public static final int execute(String sql) throws SQLException {...}
    private JCTree.JCMethodDecl handleExecuteMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);
        JCVariableDecl sqlVar = createParameter(typeNode,
                genJavaLangTypeRef(typeNode, String.class.getSimpleName()), "sql");

        addPersistenceRefStatement(treeMaker, typeNode, blockBuilder);

        // return persistence.execute(sql);
        blockBuilder.appendReturn("persistence", "execute", varRef(typeNode, "sql"));
        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("execute")
                .withParameters(sqlVar)
                .withThrowsClauses(SQLException.class)
                .withReturnType(treeMaker.TypeIdent(CTC_INT))
                .withBody(blockBuilder.build())
                .buildWith(typeNode);
    }

    // public static final RelationshipTest.TestRelativeModel newInstanceFrom(Map source) {...}
    private JCTree.JCMethodDecl handleNewInstanceFromMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);
        JCVariableDecl sourceVar = createParameter(typeNode,
                genTypeRef(typeNode, Map.class.getName()), "source");

        // return newInstanceFrom(source, true);
        JCExpression createNewInstance = blockBuilder.staticMethodInvoke(typeNode, typeNode.getName(), "newInstanceFrom",
                varRef(typeNode, "source"), treeMaker.Literal(true));
        blockBuilder.appendReturn(createNewInstance);

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("newInstanceFrom")
                .withParameters(List.of(sourceVar))
                .withReturnType(treeMaker.Ident(typeNode.toName(typeNode.getName())))
                .withBody(blockBuilder.build())
                .buildWith(typeNode);
    }

    // public static final RelationshipTest.TestRelativeModel newInstanceFrom(Map source, boolean underline) {...}
    private JCTree.JCMethodDecl handleNewInstanceFrom2Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);
        JCVariableDecl sourceVar = createParameter(typeNode,
                genTypeRef(typeNode, Map.class.getName()), "source");
        JCVariableDecl underlineVar = createParameter(typeNode,
                treeMaker.TypeIdent(CTC_BOOLEAN), "underline");

        // RelationshipTest.TestRelativeModel target = ClassUtils.createNewInstance(RelationshipTest.TestRelativeModel.class);
        JCTree.JCExpression createNewInstance = staticMethodInvoke(typeNode,
                ClassUtils.class, "createNewInstance", classRef(typeNode, typeNode.getName()));
        blockBuilder.appendVar(typeNode.getName(), "target", createNewInstance);

        // PropertyUtils.populate(target, source, underline);
        blockBuilder.appendStaticMethodInvoke(PropertyUtils.class, "populate",
                varRef(typeNode, "target"), varRef(typeNode, "source"), varRef(typeNode, "underline"));

        // return target;
        blockBuilder.appendReturn("target");

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("newInstanceFrom")
                .withParameters(List.of(sourceVar, underlineVar))
                .withReturnType(treeMaker.Ident(typeNode.toName(typeNode.getName())))
                .withBody(blockBuilder.build())
                .buildWith(typeNode);
    }

    // public static final List<Row> query(String sql) throws SQLException {...}
    private JCTree.JCMethodDecl handleQueryMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        JCVariableDecl sqlVar = MethodBuilder.createParameter(typeNode, String.class, "sql");
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);

        // ConnectionFactory connectionFactory = Database.getConnectionFactory();
        JCTree.JCExpression getConnectionFactory = staticMethodInvoke(typeNode,
                Database.class, "getConnectionFactory");
        blockBuilder.appendVar(ConnectionFactory.class, "connectionFactory", getConnectionFactory);

        // Connection connection = connectionFactory.getConnection();
        JCTree.JCExpression getConnection = methodInvoke(typeNode, "connectionFactory", "getConnection");
        blockBuilder.appendVar(Connection.class, "connection", getConnection);

        // SQLExecutor sqlExecutor = Database.getSqlExecutor();
        JCTree.JCExpression sqlExecutor = staticMethodInvoke(typeNode, Database.class, "getSqlExecutor");
        blockBuilder.appendVar(SQLExecutor.class, "sqlExecutor", sqlExecutor);

        // return sqlExecutor.query(connection, sql);
        blockBuilder.appendReturn("sqlExecutor", "query",
                treeMaker.Ident(typeNode.toName("connection")), treeMaker.Ident(typeNode.toName("sql")));

        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withReturnType(java.util.List.class, Row.class)
                .withName("query")
                .withParameters(sqlVar)
                .withThrowsClauses(SQLException.class)
                .withBody(blockBuilder.build())
                .buildWith(typeNode);
    }

    // public static final RelationshipTest.TestRelativeModel newInstanceFrom(Map source, boolean underline) {...}
    private JCTree.JCMethodDecl handleValidateMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);
        String violationClassName = Validator.Violation.class.getName().replace("$", ".");

        blockBuilder.appendVar(Validator.class, "validator", Table.class, "getValidator");

        blockBuilder.appendReturn("validator", "validate",
                treeMaker.Ident(typeNode.toName("this")));
        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.FINAL)
                .withName("validate")
                .withReturnType(treeMaker.TypeArray(genTypeRef(typeNode, violationClassName)))
                .withBody(blockBuilder.build())
                .buildWith(typeNode);
    }

    // public final static int count(String predicate) throws SQLException {...}
    private JCTree.JCMethodDecl handleCountMethod(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);
        JCVariableDecl predicateVar = createParameter(typeNode,
                genTypeRef(typeNode, String.class.getName()), "predicate");
        JCExpression domainModelClassRef = treeMaker.Select(treeMaker.Ident(typeNode.toName(typeNode.getName())),
                typeNode.toName("class"));

        // return Table.count(RelationshipTest.TestRelativeModel.class, predicate);
        blockBuilder.appendReturn(Table.class, "count", domainModelClassRef, varRef(typeNode,"predicate"));
        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("count")
                .withParameters(predicateVar)
                .withThrowsClauses(SQLException.class)
                .withReturnType(treeMaker.TypeIdent(CTC_INT))
                .withBody(blockBuilder.build())
                .buildWith(typeNode);
    }

    // public static final int count() throws SQLException {...}
    private JCTree.JCMethodDecl handleCount2Method(JavacTreeMaker treeMaker, JavacNode typeNode) {
        BlockBuilder blockBuilder = BlockBuilder.newBlock(treeMaker, typeNode);

        // return count("");
        blockBuilder.appendReturn(typeNode.getName(), "count", treeMaker.Literal(""));
        return MethodBuilder.newMethod(treeMaker, typeNode)
                .withModifiers(Flags.PUBLIC | Flags.STATIC | Flags.FINAL)
                .withName("count")
                .withThrowsClauses(SQLException.class)
                .withReturnType(treeMaker.TypeIdent(CTC_INT))
                .withBody(blockBuilder.build())
                .buildWith(typeNode);
    }

    private void addPersistenceRefStatement(JavacTreeMaker treeMaker, JavacNode typeNode,
                                            BlockBuilder blockBuilder) {
        // PersistenceFactory persistenceFactory = Database.getPersistenceFactory();
        blockBuilder.appendVar(PersistenceFactory.class, "persistenceFactory",
                Database.class, "getPersistenceFactory");

        //Persistence persistence = persistenceFactory.createPersistence(RelationshipTest.TestRelativeModel.class);
        JCExpression createPersistence = staticMethodInvoke(typeNode,
                "persistenceFactory", "createPersistence", classRef(typeNode, typeNode.getName()));
        JCTree.JCTypeApply typeApply = treeMaker.TypeApply(genTypeRef(typeNode, Persistence.class.getName()),
                List.of(treeMaker.Ident(typeNode.toName(typeNode.getName()))));
        blockBuilder.appendVar(typeApply, "persistence", createPersistence);
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

    private JCVariableDecl createParameter(JavacNode typeNode, JCExpression type, String name, JCTree.JCAnnotation... annotations) {
        return FieldBuilder.newField(typeNode)
                .ofType(type)
                .withName(name)
                .withAnnotations(annotations)
                .withModifiers(Flags.PARAMETER)
                .build();
    }

}
