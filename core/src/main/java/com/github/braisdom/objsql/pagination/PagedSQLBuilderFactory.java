package com.github.braisdom.objsql.pagination;

import com.github.braisdom.objsql.DatabaseType;

public interface PagedSQLBuilderFactory {

    PagedSQLBuilder createPagedSQLBuilder(DatabaseType databaseType);
}
