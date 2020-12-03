package com.github.braisdom.objsql.apt;

import com.github.braisdom.objsql.Query;
import com.github.braisdom.objsql.Tables;
import com.github.braisdom.objsql.annotations.Queryable;
import com.github.braisdom.objsql.relation.Relationship;
import com.github.braisdom.objsql.util.WordUtil;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import org.mangosdk.spi.ProviderFor;

import javax.annotation.processing.Processor;
import java.lang.annotation.Annotation;
import java.sql.SQLException;

@ProviderFor(Processor.class)
public class QueryMethodCodeGenerator extends DomainModelProcessor {

    @Override
    public void handle(AnnotationValues annotationValues, JCTree ast, APTBuilder aptBuilder) {
        JCTree.JCVariableDecl field = (JCTree.JCVariableDecl) aptBuilder.get();
        if(ast == null || field == null) {
            return;
        }

        TreeMaker treeMaker = aptBuilder.getTreeMaker();
        boolean returnsMany = annotationValues.getAnnotationValue(Queryable.class).many();
        String methodName = WordUtil.camelize("queryBy_" + field.getName(), true);

        MethodBuilder methodBuilder = aptBuilder.createMethodBuilder();
        StatementBuilder statementBuilder = aptBuilder.createStatementBuilder();

        statementBuilder.append(aptBuilder.newGenericsType(Query.class, aptBuilder.getClassName()),
                "query", aptBuilder.getClassName(), "createQuery");
        statementBuilder.append(String.class, "columnName", Tables.class, "getColumnName",
                aptBuilder.classRef(aptBuilder.getClassName()), treeMaker.Literal(field.getName().toString()));
        JCTree.JCExpression stringFormatExpression = aptBuilder.staticMethodCall(String.class,
                "format", treeMaker.Literal("%s = ?"), treeMaker.Literal(field.name.toString()));
        statementBuilder.append("query", "where",
                        List.of(stringFormatExpression, aptBuilder.varRef("value")));

        if(returnsMany) {
            methodBuilder.setReturnType(java.util.List.class, aptBuilder.typeRef(aptBuilder.getClassName()));
            methodBuilder.setReturnStatement("query", "execute",
                    aptBuilder.varRef("relations"));
        } else {
            methodBuilder.setReturnType(aptBuilder.typeRef(aptBuilder.getClassName()));
            methodBuilder.setReturnStatement("query", "queryFirst",
                    aptBuilder.varRef("relations"));
        }

        aptBuilder.inject(methodBuilder
                .addParameter("value", field.vartype)
                .addVarargsParameter("relations", Relationship.class)
                .addStatements(statementBuilder.build())
                .setThrowsClauses(SQLException.class)
                .build(methodName, Flags.PUBLIC | Flags.STATIC | Flags.FINAL));
    }

    @Override
    protected Class<? extends Annotation> getAnnotationClass() {
        return Queryable.class;
    }
}
