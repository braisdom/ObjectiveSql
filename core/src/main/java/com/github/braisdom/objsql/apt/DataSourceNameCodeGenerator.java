package com.github.braisdom.objsql.apt;

import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.annotations.DataSourceName;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import org.mangosdk.spi.ProviderFor;

import javax.annotation.processing.Processor;
import java.lang.annotation.Annotation;

@ProviderFor(Processor.class)
public class DataSourceNameCodeGenerator extends DomainModelProcessor {

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return DataSourceName.class;
    }

    @Override
    protected void handle(AnnotationValues annotationValues, JCTree ast, APTBuilder aptBuilder) {
        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        JCTree.JCMethodDecl methodDecl = (JCTree.JCMethodDecl) aptBuilder.get();

        if(ast == null || methodDecl == null)
            return;
        String dataSourceName = annotationValues.getAnnotationValue(DataSourceName.class).value();
        List<JCTree.JCStatement> originalStatement = methodDecl.body.stats;

        JCTree.JCStatement setDataSourceName = treeMaker.Exec(aptBuilder.staticMethodCall(Databases.class,
                "setCurrentDataSourceName", treeMaker.Literal(dataSourceName)));
        JCTree.JCStatement clearDataSourceName = treeMaker.Exec(aptBuilder.staticMethodCall(Databases.class,
                "clearCurrentDataSourceName"));

        JCTree.JCTry jcTry = treeMaker.Try(treeMaker.Block(0, originalStatement), List.nil(),
                treeMaker.Block(0, List.of(clearDataSourceName)));

        ListBuffer statements = new ListBuffer();
        statements.append(setDataSourceName);
        statements.append(jcTry);

        methodDecl.body.stats = statements.toList();
    }
}
