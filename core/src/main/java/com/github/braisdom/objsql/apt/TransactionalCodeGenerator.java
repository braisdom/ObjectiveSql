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
        List<JCTree.JCStatement> originalStatements = methodDecl.body.getStatements();
        Name realMethodName = methodDecl.name.append(aptBuilder.toName("InTransaction"));
        methodDecl.name = realMethodName;

        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();
        if(methodDecl.restype.type.getTag().equals(TypeTag.VOID)) {
            JCTree.JCExpression expression = createLambda(methodDecl, aptBuilder, statementBuilder);
            statementBuilder.append(treeMaker.Exec(expression));
        } else {
            JCTree.JCExpression expression = createLambda(methodDecl, aptBuilder, statementBuilder);
            statementBuilder.append(treeMaker.Return(expression));
        }

        methodBuilder.setReturnType(methodDecl.restype);
        methodBuilder.addParameter(methodDecl.params);
        methodBuilder.addStatements(statementBuilder.build());
        methodBuilder.setThrowsClauses(SQLException.class, RollbackCauseException.class);
        aptBuilder.injectForce(methodBuilder.build(originalMethodName, (int) methodDecl.getModifiers().flags));
    }

    private JCTree.JCExpression createLambda(JCTree.JCMethodDecl methodDecl, APTBuilder aptBuilder,
                                     StatementBuilder statementBuilder) {
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        List<JCTree.JCStatement> originalStatements = methodDecl.body.getStatements();
        ListBuffer lambdaStatement = new ListBuffer();
        JCTree.JCExpression methodRef = treeMaker.Select(aptBuilder.typeRef(Databases.class),
                aptBuilder.toName("executeTransactionally"));

        for(JCTree.JCStatement statement:methodDecl.body.getStatements())
            lambdaStatement.append(statement);

        if(!(originalStatements.last() instanceof JCTree.JCReturn))
            lambdaStatement.append(treeMaker.Return(treeMaker.Literal(TypeTag.BOT, null)));

        JCTree.JCLambda lambda = treeMaker.Lambda(List.nil(), treeMaker.Block(0, lambdaStatement.toList()));
        return treeMaker.Apply(List.nil(), methodRef, List.of(lambda));
    }
}
