package com.github.braisdom.funcsql;

import java.sql.Connection;
import java.sql.SQLException;

public class DefaultDelete extends AbstractDelete {
    public DefaultDelete(Class domainModelClass) {
        super(domainModelClass);
    }

    @Override
    public int execute() throws SQLException {
        Connection connection = Database.getConnectionFactory().getConnection();
        SQLGenerator sqlGenerator = Database.getSQLGenerator();
        String deleteSQL = sqlGenerator.createDeleteSQL(Table.getTableName(domainModelClass), filter);

        return executeInternally(connection, deleteSQL);
    }
}
