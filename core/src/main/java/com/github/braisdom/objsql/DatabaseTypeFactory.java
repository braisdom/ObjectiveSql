package com.github.braisdom.objsql;

import javax.sql.DataSource;

public interface DatabaseTypeFactory {

    DatabaseType createDatabaseType(String databaseProductionName, int majorVersion);

    DatabaseType createDatabaseType(DataSource dataSource);
}
