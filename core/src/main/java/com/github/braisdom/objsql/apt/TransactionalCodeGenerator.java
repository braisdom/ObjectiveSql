package com.github.braisdom.objsql.apt;

import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.RollbackCauseException;
import com.github.braisdom.objsql.ValidationException;
import com.github.braisdom.objsql.annotations.Transactional;
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

        methodBuilder.addParameter(methodDecl.params.toArray(JCTree.JCVariableDecl[]::new));
        methodBuilder.setReturnType(methodDecl.restype);
        methodBuilder.addStatements(createBody(methodDecl, aptBuilder));
        methodBuilder.setThrowsClauses(SQLException.class, RollbackCauseException.class);
        aptBuilder.injectForce(methodBuilder.build(originalMethodName, (int) methodDecl.getModifiers().flags));
    }

    private List<JCTree.JCStatement> createBody(JCTree.JCMethodDecl methodDecl, APTBuilder aptBuilder) {
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        List<JCTree.JCExpression> exceptions = methodDecl.getThrows();
        ListBuffer<JCTree.JCCatch> catchStatement = new ListBuffer<>();
        StatementBuilder bodyStatement = aptBuilder.createStatementBuilder();
        StatementBuilder tryStatement = aptBuilder.createStatementBuilder();

        bodyStatement.append(aptBuilder.classRef(Connection.class), "connection", treeMaker.Literal(TypeTag.BOT, null));

        JCTree.JCExpression[] originalParams = methodDecl.params.stream().map(param -> aptBuilder.varRef(param.name.toString()))
                .toArray(JCTree.JCExpression[]::new);
        JCTree.JCExpression invokeMethodRef = treeMaker.Ident(methodDecl.name);
        JCTree.JCMethodInvocation originalInvocation = treeMaker.Apply(List.nil(), invokeMethodRef, List.from(originalParams));

        if(methodDecl.restype.type.getTag().equals(TypeTag.VOID)) {
            tryStatement.append(treeMaker.Exec(originalInvocation));
        } else {
            tryStatement.append(methodDecl.restype, "res", invokeMethodRef);
            tryStatement.append(treeMaker.Return(aptBuilder.varRef("res")));
        }

        for(JCTree.JCExpression exception : exceptions) {

            treeMaker.Throw(aptBuilder.varRef("ex"));
            catchStatement.append(treeMaker.Catch(aptBuilder.newVar(exception, "ex"),
                    treeMaker.Block(0, List.of(treeMaker.Return(aptBuilder.methodCall("ex", "getViolations"))))));
        }

        JCTree.JCCatch jcCatch = treeMaker.Catch(aptBuilder.newVar(ValidationException.class, "ex"),
                treeMaker.Block(0, List.of(treeMaker.Return(aptBuilder.methodCall("ex", "getViolations")))));

        JCTree.JCTry jcTry = treeMaker.Try(treeMaker.Block(0, tryStatement.build()), List.of(jcCatch),
                treeMaker.Block(0, List.nil()));

        bodyStatement.append(jcTry);

        JCTree.JCLambda lambda = treeMaker.Lambda(List.nil(), treeMaker.Block(0, bodyStatement.build()));
        JCTree.JCExpression methodRef = treeMaker.Select(aptBuilder.typeRef(Databases.class),
                aptBuilder.toName("executeTransactionally"));
        return bodyStatement.build();
    }
}
