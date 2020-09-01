package com.github.braisdom.funcsql.osql;

public interface Table {

    String getTableName();

    Select createSelectStatement();

    Update createUpdateStatement();

    Delete createDeleteStatement();
}
