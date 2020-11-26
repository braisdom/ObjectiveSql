package com.github.braisdom.objsql.pagination.impl;

import com.github.braisdom.objsql.DomainModelDescriptor;
import com.github.braisdom.objsql.pagination.Page;
import com.github.braisdom.objsql.pagination.PagedSQLBuilder;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.sql.SQLException;

public class OraclePagedSQLBuilder implements PagedSQLBuilder {

    @Override
    public String buildQuerySQL(Page page, String rawSQL, DomainModelDescriptor modelDescriptor) throws SQLException {
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

            subSelect.setSelectBody(originalSelect.getSelectBody());
            plainSelect.setFromItem(subSelect);
            select.setSelectBody(plainSelect);

            return select.toString();
        } catch (JSQLParserException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }
}
