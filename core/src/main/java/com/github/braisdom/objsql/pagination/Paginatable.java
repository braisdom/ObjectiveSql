package com.github.braisdom.objsql.pagination;

public interface Paginatable {

    String getCountSQL();

    String getQuerySQL(PaginationSQLBuilder paginationSQLBuilder);
}
