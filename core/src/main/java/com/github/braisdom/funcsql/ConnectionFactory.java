package com.github.braisdom.funcsql;

import java.sql.Connection;

public interface ConnectionFactory {

    Connection getConnection();
}
