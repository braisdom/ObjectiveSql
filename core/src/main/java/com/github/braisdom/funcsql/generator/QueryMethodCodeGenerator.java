package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.Query;
import com.github.braisdom.funcsql.Table;
import com.github.braisdom.funcsql.annotations.Queryable;
import com.github.braisdom.funcsql.apt.*;
import com.github.braisdom.funcsql.apt.MethodBuilder;
import com.github.braisdom.funcsql.util.WordUtil;
import com.google.auto.service.AutoService;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import org.mangosdk.spi.ProviderFor;

import javax.annotation.processing.Processor;
import java.lang.annotation.Annotation;
import java.sql.SQLException;

@AutoService(Processor.class)
public class QueryMethodCodeGenerator extends DomainModelProcessor {

    @Override
    public void handle(AnnotationValues annotationValues, JCTree ast, APTUtils aptUtils) {
        JCTree.JCVariableDecl field = (JCTree.JCVariableDecl) aptUtils.get();

        TreeMaker treeMaker = aptUtils.getTreeMaker();
        String methodName = WordUtil.camelize("queryBy_" + field.getName(), true);

        MethodBuilder methodBuilder = aptUtils.createMethodBuilder();
        StatementBuilder statementBuilder = aptUtils.createBlockBuilder();

        methodBuilder.addParameter("value", field.vartype);

        statementBuilder.append(aptUtils.newGenericsType(Query.class, aptUtils.getClassName()),
                "query", aptUtils.getClassName(), "createQuery");
        statementBuilder.append(String.class, "columnName", Table.class, "getColumnName",
                aptUtils.classRef(aptUtils.getClassName()), treeMaker.Literal(field.getName().toString()));
        JCTree.JCExpression stringFormatExpression = aptUtils.staticMethodCall(String.class,
                "format", treeMaker.Literal("%s = ?"), treeMaker.Literal(field.name.toString()));
        statementBuilder.append("query", "where",
                        List.of(stringFormatExpression, aptUtils.varRef("value")));

        methodBuilder.setReturnStatement("query", "execute");
        aptUtils.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .setThrowsClauses(SQLException.class)
                .setReturnType(java.util.List.class, aptUtils.typeRef(aptUtils.getClassName()))
                .build(methodName, Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return Queryable.class;
    }
}
