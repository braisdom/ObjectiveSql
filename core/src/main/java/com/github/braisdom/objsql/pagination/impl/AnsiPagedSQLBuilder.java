package com.github.braisdom.objsql.pagination.impl;

import com.github.braisdom.objsql.pagination.AbstractPagedSQLBuilder;
import net.sf.jsqlparser.statement.Statement;

public class AnsiPagedSQLBuilder extends AbstractPagedSQLBuilder {

    @Override
    protected String buildCountSQL(Statement statement) {

        return null;
    }

    @Override
    protected String buildQuerySQL(Statement statement) {
        return null;
    }
}
