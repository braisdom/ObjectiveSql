package com.github.braisdom.objsql.apt;

import com.github.braisdom.objsql.annotations.Transactional;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;
import org.mangosdk.spi.ProviderFor;

import javax.annotation.processing.Processor;
import java.lang.annotation.Annotation;

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
        Name realMethodName = methodDecl.name.append(aptBuilder.toName("InTransaction"));
        methodDecl.name = realMethodName;

        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        methodBuilder.setReturnType(methodDecl.restype);
        methodBuilder.addParameter(methodDecl.params);
    }
}
