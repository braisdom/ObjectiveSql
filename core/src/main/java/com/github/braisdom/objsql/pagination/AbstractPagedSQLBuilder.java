package com.github.braisdom.objsql.pagination;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;

import java.sql.SQLException;

public abstract class AbstractPagedSQLBuilder implements PagedSQLBuilder {

    @Override
    public String buildCountSQL(String rawSQL) throws SQLException {
        try {
            Statement statement = CCJSqlParserUtil.parse(rawSQL);
            return buildCountSQL(statement);
        } catch (JSQLParserException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    @Override
    public String buildQuerySQL(String rawSQL) throws SQLException {
        try {
            Statement statement = CCJSqlParserUtil.parse(rawSQL);
            return buildQuerySQL(statement);
        } catch (JSQLParserException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    protected abstract String buildCountSQL(Statement statement);

    protected abstract String buildQuerySQL(Statement statement);
}
