package com.github.braisdom.funcsql;

import java.sql.SQLException;

public interface Delete {

    Delete where(String filter, Object... args);

    int execute() throws SQLException;
}
