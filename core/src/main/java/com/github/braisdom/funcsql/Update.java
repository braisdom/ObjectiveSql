package com.github.braisdom.funcsql;

import java.sql.SQLException;

public interface Update {

    Update set(String set, Object... args);

    Update where(String filter, Object... args);

    int execute() throws SQLException;
}
