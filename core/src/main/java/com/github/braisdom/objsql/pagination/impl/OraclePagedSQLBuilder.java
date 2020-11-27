package com.github.braisdom.objsql.pagination.impl;

import com.github.braisdom.objsql.DomainModelDescriptor;
import com.github.braisdom.objsql.pagination.Page;
import com.github.braisdom.objsql.pagination.PagedSQLBuilder;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
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
            Select innerSelect = createInnerSelect(originalSelect, page);

            plainSelect.addSelectItems(new SelectExpressionItem(new Column("*")));

            subSelect.setSelectBody(innerSelect.getSelectBody());
            plainSelect.setFromItem(subSelect);

            GreaterThanEquals greaterThanEquals = new GreaterThanEquals();
            greaterThanEquals.setLeftExpression(new Column("OFFSET_ROW_COUNT"));
            greaterThanEquals.setRightExpression(new LongValue(page.getOffset()));

            plainSelect.setWhere(greaterThanEquals);

            select.setSelectBody(plainSelect);

            return select.toString();
        } catch (JSQLParserException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    @Override
    public String getCountAlias() {
        return COUNT_ALIAS.toUpperCase();
    }

    private Select createInnerSelect(Select originalSelect, Page page) {
        Alias originalSelectAlias = new TableAlias("T");
        Select select = new Select();
        SubSelect subSelect = new SubSelect();
        PlainSelect plainSelect = new PlainSelect();
        SelectExpressionItem originalColumnExpr = new SelectExpressionItem();
        SelectExpressionItem rownumColumnExpr = new SelectExpressionItem();

        originalColumnExpr.setExpression(new Column(new Table(originalSelectAlias.getName()), "*"));
        rownumColumnExpr.setExpression(new Column("ROWNUM"));
        rownumColumnExpr.setAlias(new Alias("OFFSET_ROW_COUNT"));

        plainSelect.addSelectItems(originalColumnExpr, rownumColumnExpr);

        MinorThanEquals minorThanEquals = new MinorThanEquals();
        minorThanEquals.setLeftExpression(new Column("ROWNUM"));
        minorThanEquals.setRightExpression(new LongValue(page.getOffset() + page.getPageSize()));

        plainSelect.setWhere(minorThanEquals);

        subSelect.setSelectBody(originalSelect.getSelectBody());
        subSelect.setAlias(originalSelectAlias);
        plainSelect.setFromItem(subSelect);
        select.setSelectBody(plainSelect);

        return select;
    }
}
