package com.github.braisdom.objsql.apt;

import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.RollbackCauseException;
import com.github.braisdom.objsql.ValidationException;
import com.github.braisdom.objsql.annotations.Transactional;
import com.github.braisdom.objsql.jdbc.DbUtils;
import com.github.braisdom.objsql.util.ArrayUtil;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import org.mangosdk.spi.ProviderFor;

import javax.annotation.processing.Processor;
import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.SQLException;

@ProviderFor(Processor.class)
public class TransactionalCodeGenerator extends DomainModelProcessor {

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return Transactional.class;
    }

    @Override
    protected void handle(AnnotationValues annotationValues, JCTree ast, APTBuilder aptBuilder) {
        JCTree.JCMethodDecl methodDecl = (JCTree.JCMethodDecl) aptBuilder.get();
        if(ast == null || methodDecl == null)
            return;

        String originalMethodName = methodDecl.name.toString();
        methodDecl.name = methodDecl.name.append(aptBuilder.toName("InTransaction"));

        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();

        for(JCTree.JCExpression throwExpression : methodDecl.getThrows())
            methodBuilder.addThrowsClauses(throwExpression);

        methodBuilder.addParameter(methodDecl.params.toArray(new JCTree.JCVariableDecl[0]));
        methodBuilder.setReturnType(methodDecl.restype);
        methodBuilder.addStatements(createBody(methodDecl, aptBuilder));

        aptBuilder.injectForce(methodBuilder.build(originalMethodName, (int) methodDecl.getModifiers().flags));
    }

    private List<JCTree.JCStatement> createBody(JCTree.JCMethodDecl methodDecl, APTBuilder aptBuilder) {
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        List<JCTree.JCExpression> exceptions = methodDecl.getThrows();
        ListBuffer<JCTree.JCCatch> catchStatement = new ListBuffer<>();
        StatementBuilder bodyStatement = aptBuilder.createStatementBuilder();
        StatementBuilder tryStatement = aptBuilder.createStatementBuilder();

        bodyStatement.append(aptBuilder.typeRef(Connection.class), "connection", treeMaker.Literal(TypeTag.BOT, null));

        JCTree.JCExpression[] originalParams = methodDecl.params.stream().map(param -> aptBuilder.varRef(param.name.toString()))
                .toArray(JCTree.JCExpression[]::new);
        JCTree.JCExpression invokeMethodRef = treeMaker.Ident(methodDecl.name);
        JCTree.JCMethodInvocation originalMethodInvocation = treeMaker.Apply(List.nil(), invokeMethodRef, List.from(originalParams));

        JCTree.JCExpression getDataSourceNameCall = aptBuilder.staticMethodCall(Databases.class, "getCurrentDataSourceName");
        JCTree.JCVariableDecl dataSourceNameVar = aptBuilder.newVar(Flags.FINAL,
                String.class, "dataSourceName", getDataSourceNameCall);
        tryStatement.append(dataSourceNameVar);

        // connection = com.github.braisdom.objsql.Databases.getConnectionFactory.getConnection();
        JCTree.JCExpression getConnectionCall = treeMaker.Select(treeMaker.Apply(List.nil(), treeMaker.Select(aptBuilder.typeRef(Databases.class),
                aptBuilder.toName("getConnectionFactory")), List.nil()),
                aptBuilder.toName("getConnection"));
        tryStatement.append(treeMaker.Exec(treeMaker.Assign(aptBuilder.varRef("connection"),
                treeMaker.Apply(List.nil(), getConnectionCall, List.of(aptBuilder.varRef("dataSourceName"))))));

        // connection.setAutoCommit(false);
        tryStatement.append(treeMaker.Exec(treeMaker.Apply(List.nil(),
                treeMaker.Select(aptBuilder.varRef("connection"), aptBuilder.toName("setAutoCommit")),
                List.of(treeMaker.Literal(false)))));

        // Databases.getConnectionThreadLocal().set(connection);
        JCTree.JCExpression threadLocalSetCall = treeMaker.Select(treeMaker.Apply(List.nil(), treeMaker.Select(aptBuilder.typeRef(Databases.class),
                aptBuilder.toName("getConnectionThreadLocal")), List.nil()), aptBuilder.toName("set"));
        tryStatement.append(treeMaker.Exec(treeMaker.Apply(List.nil(), threadLocalSetCall,
                List.of(aptBuilder.varRef("connection")))));

        if(methodDecl.restype.type.getTag().equals(TypeTag.VOID)) {
            tryStatement.append(treeMaker.Exec(originalMethodInvocation));
            tryStatement.append(treeMaker.Exec(treeMaker.Apply(List.nil(),
                    treeMaker.Select(aptBuilder.varRef("connection"), aptBuilder.toName("commit")),
                    List.nil())));
        } else {
            tryStatement.append(methodDecl.restype, "res", originalMethodInvocation);
            tryStatement.append(treeMaker.Exec(treeMaker.Apply(List.nil(),
                    treeMaker.Select(aptBuilder.varRef("connection"), aptBuilder.toName("commit")),
                    List.nil())));
            tryStatement.append(treeMaker.Return(aptBuilder.varRef("res")));
        }

        for(JCTree.JCExpression exception : exceptions) {
            ListBuffer catchBodyStatement = new ListBuffer();

            // DbUtils.rollbackAndCloseQuietly(connection);
            catchBodyStatement.append(treeMaker.Exec(
                    treeMaker.Apply(List.nil(), treeMaker.Select(aptBuilder.typeRef(DbUtils.class),
                            aptBuilder.toName("rollback")), List.of(aptBuilder.varRef("connection")))));
            catchBodyStatement.append(treeMaker.Throw(aptBuilder.varRef("ex")));
            catchStatement.append(treeMaker.Catch(aptBuilder.newVar(exception, "ex"),
                    treeMaker.Block(0, catchBodyStatement.toList())));
        }

        JCTree.JCExpression threadLocalRemoveCall = treeMaker.Select(treeMaker.Apply(List.nil(),
                treeMaker.Select(aptBuilder.typeRef(Databases.class),
                aptBuilder.toName("getConnectionThreadLocal")), List.nil()), aptBuilder.toName("remove"));
        JCTree.JCStatement finallyStatement = treeMaker.Exec(treeMaker.Apply(List.nil(), threadLocalRemoveCall, List.nil()));

        JCTree.JCStatement closeQuietlyStatement = treeMaker.Exec(treeMaker.Apply(List.nil(),
                treeMaker.Select(aptBuilder.typeRef(DbUtils.class),
                        aptBuilder.toName("closeQuietly")), List.of(aptBuilder.varRef("connection"))));


        JCTree.JCTry jcTry = treeMaker.Try(treeMaker.Block(0, tryStatement.build()), catchStatement.toList(),
                treeMaker.Block(0, List.of(finallyStatement, closeQuietlyStatement)));

        bodyStatement.append(jcTry);

        return bodyStatement.build();
    }
}
