package com.github.braisdom.objsql.pagination;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.UserVariable;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.parser.SimpleNode;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.sql.SQLException;

/**
 * It builds the relevant SQL required for paging。
 */
public interface PagedSQLBuilder {

    String COUNT_ALIAS = "count_";

    /**
     * Builds counting SQL by given SQL.
     *
     * @param rawSQL The original query sql.
     *
     * @return
     * @throws SQLException
     */
    default String buildCountSQL(String rawSQL) throws SQLException {
        try {
            Statement statement = CCJSqlParserUtil.parse(rawSQL);
            Select originalSelect = (Select) statement;

            Select select = new Select();
            SubSelect subSelect = new SubSelect();
            PlainSelect plainSelect = new PlainSelect();
            SelectExpressionItem expressionItem = new SelectExpressionItem();
            Function countAllFunction = new Function();

            countAllFunction.setName("COUNT");
            countAllFunction.setParameters(new ExpressionList(new Column("*")));
            expressionItem.setExpression(countAllFunction);
            expressionItem.setAlias(new Alias(COUNT_ALIAS));
            plainSelect.addSelectItems(expressionItem);

            subSelect.setAlias(new Alias("T"));
            subSelect.setSelectBody(originalSelect.getSelectBody());
            plainSelect.setFromItem(subSelect);
            select.setSelectBody(plainSelect);

            return select.toString();
        } catch (JSQLParserException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    /**
     * Builds paged querying SQLby given SQL.
     *
     * @param rawSQL
     * @return The original query sql.
     * @throws SQLException
     */
    String buildQuerySQL(String rawSQL) throws SQLException;
}
