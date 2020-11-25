package com.github.braisdom.objsql.pagination;

import java.sql.SQLException;

public interface PagedSQLBuilder {

    String buildCountSQL(String rawSQL) throws SQLException;

    String buildQuerySQL(String rawSQL) throws SQLException;
}
