package com.github.braisdom.objsql;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionFactory {

    Connection getConnection() throws SQLException;
}
