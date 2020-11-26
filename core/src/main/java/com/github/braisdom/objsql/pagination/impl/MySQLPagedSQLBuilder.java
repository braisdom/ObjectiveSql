package com.github.braisdom.objsql.pagination.impl;

import com.github.braisdom.objsql.pagination.Page;
import com.github.braisdom.objsql.pagination.PagedSQLBuilder;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Limit;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import java.sql.SQLException;

public class MySQLPagedSQLBuilder implements PagedSQLBuilder {

    @Override
    public String buildQuerySQL(Page page, String rawSQL) throws SQLException {
        try {
            Statement statement = CCJSqlParserUtil.parse(rawSQL);
            Select originalSelect = (Select) statement;

            PlainSelect plainSelect = (PlainSelect) originalSelect.getSelectBody();
            Limit limit = new Limit();

            limit.setOffset(new LongValue(page.getOffset()));
            limit.setRowCount(new LongValue(page.getPageSize()));

            plainSelect.setLimit(limit);
            return originalSelect.toString();
        } catch (JSQLParserException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }
}
