package com.github.braisdom.objsql.apt;

import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.RollbackCauseException;
import com.github.braisdom.objsql.annotations.Transactional;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import org.mangosdk.spi.ProviderFor;

import javax.annotation.processing.Processor;
import java.lang.annotation.Annotation;
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

        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        String originalMethodName = methodDecl.name.toString();
        methodDecl.name = methodDecl.name.append(aptBuilder.toName("InTransaction"));

        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();
        if(methodDecl.restype.type.getTag().equals(TypeTag.VOID)) {
            JCTree.JCExpression expression = createLambda(methodDecl, aptBuilder);
            statementBuilder.append(treeMaker.Exec(expression));
        } else {
            JCTree.JCExpression expression = createLambda(methodDecl, aptBuilder);
            statementBuilder.append(treeMaker.Return(expression));
        }


        methodBuilder.setReturnType(methodDecl.restype);
        methodBuilder.addStatements(statementBuilder.build());
        methodBuilder.setThrowsClauses(SQLException.class, RollbackCauseException.class);
        aptBuilder.injectForce(methodBuilder.build(originalMethodName, (int) methodDecl.getModifiers().flags));
    }

    private JCTree.JCExpression createLambda(JCTree.JCMethodDecl methodDecl, APTBuilder aptBuilder) {
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        ListBuffer lambdaStatement = new ListBuffer();
        JCTree.JCExpression[] expressions = methodDecl.params.stream().map(param -> aptBuilder.varRef(param.name.toString()))
                .toArray(JCTree.JCExpression[]::new);

        JCTree.JCExpression invokeMethodRef = treeMaker.Ident(methodDecl.name);
//        lambdaStatement.append(treeMaker.Apply(List.nil(), invokeMethodRef, List.from(expressions)));

        if(methodDecl.restype.type.getTag().equals(TypeTag.VOID))
            lambdaStatement.append(treeMaker.Return(treeMaker.Literal(TypeTag.BOT, null)));

        JCTree.JCLambda lambda = treeMaker.Lambda(List.nil(), treeMaker.Block(0, lambdaStatement.toList()));
        JCTree.JCExpression methodRef = treeMaker.Select(aptBuilder.typeRef(Databases.class),
                aptBuilder.toName("executeTransactionally"));
        return treeMaker.Apply(List.nil(), methodRef, List.of(lambda));
    }
}
