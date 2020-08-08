package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.Query;
import com.github.braisdom.funcsql.annotations.Queryable;
import com.github.braisdom.funcsql.apt.*;
import com.github.braisdom.funcsql.apt.MethodBuilder;
import com.github.braisdom.funcsql.util.WordUtil;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import org.mangosdk.spi.ProviderFor;

import java.sql.SQLException;

@ProviderFor(JavacAnnotationHandler.class)
public class QueryMethodCodeGenerator extends JavacAnnotationHandler<Queryable> {

    @Override
    public void handle(AnnotationValues annotationValues, JCTree ast, APTHandler handler) {
        JCTree.JCVariableDecl field = (JCTree.JCVariableDecl) handler.get();
        TreeMaker treeMaker = handler.getTreeMaker();
        String fieldColumnName = WordUtil.underscore(field.getName().toString());
        String methodName = WordUtil.camelize("queryBy_" + field.getName(), true);

        MethodBuilder methodBuilder = handler.createMethodBuilder();
        StatementBuilder statementBuilder = handler.createBlockBuilder();

        methodBuilder.addParameter("value", field.vartype);

        statementBuilder.append(handler.newGenericsType(Query.class, handler.getClassName()),
                "query", handler.getClassName(), "createQuery")
                .append("query", "where",
                        List.of(treeMaker.Literal(String.format("%s = ?", fieldColumnName)), handler.varRef("value")));

        methodBuilder.setReturnStatement("query", "execute");
        handler.inject(methodBuilder
                .addStatements(statementBuilder.build())
                .setThrowsClauses(SQLException.class)
                .setReturnType(java.util.List.class, handler.typeRef(handler.getClassName()))
                .build(methodName, Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }
}
