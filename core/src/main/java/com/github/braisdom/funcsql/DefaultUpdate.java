package com.github.braisdom.funcsql;

import java.sql.Connection;
import java.sql.SQLException;

public class DefaultUpdate extends AbstractUpdate {
    public DefaultUpdate(Class domainModelClass) {
        super(domainModelClass);
    }

    @Override
    public int execute() throws SQLException {
        Connection connection = Database.getConnectionFactory().getConnection();
        SQLGenerator sqlGenerator = Database.getSQLGenerator();
        String updateSQL = sqlGenerator.createUpdateSQL(Table.getTableName(domainModelClass), update, filter);

        return executeInternally(connection, updateSQL);
    }
}
