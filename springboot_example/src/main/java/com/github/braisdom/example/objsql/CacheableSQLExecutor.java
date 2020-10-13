package com.github.braisdom.example.objsql;

import com.github.braisdom.objsql.SQLExecutor;
import com.github.braisdom.objsql.TableRowAdapter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CacheableSQLExecutor<T> implements SQLExecutor<T> {

    @Override
    public List<T> query(Connection connection, String s,
                         TableRowAdapter tableRowAdapter, Object... objects) throws SQLException {
        return null;
    }

}
