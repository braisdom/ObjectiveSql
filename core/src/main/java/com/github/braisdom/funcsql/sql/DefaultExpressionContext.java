package com.github.braisdom.funcsql.sql;

import com.github.braisdom.funcsql.DatabaseType;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DefaultExpressionContext implements ExpressionContext {

    private final DatabaseType databaseType;
    private final List<Dataset> datasets;

    public DefaultExpressionContext(DatabaseType databaseType) {
        this.databaseType = databaseType;
        this.datasets = new ArrayList<>();
    }

    @Override
    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    @Override
    public String getAlias(Dataset dataset, boolean forceCreate) {
        if (dataset.getAlias() != null)
            return dataset.getAlias();
        if (!datasets.contains(dataset))
            datasets.add(dataset);
        return String.format("T%d", datasets.indexOf(dataset));
    }

    @Override
    public String quoteTable(String tableName) {
        switch (databaseType) {
            case MariaDB:
            case MySQL:
                return String.format("`%s`", tableName);
            case PostgreSQL:
            case Oracle:
            case SQLite:
            case MsSqlServer:
                return String.format("\"%s\"", tableName);
            case All:
                return String.format("\"%s\"", tableName);
        }
        return null;
    }

    @Override
    public String quoteColumn(String columnName) {
        switch (databaseType) {
            case MariaDB:
            case MySQL:
                return String.format("`%s`", columnName);
            case PostgreSQL:
            case Oracle:
            case SQLite:
            case MsSqlServer:
                return String.format("\"%s\"", columnName);
            case All:
                return String.format("\"%s\"", columnName);
        }
        return null;
    }

    @Override
    public String quoteString(String stringValue) {
        return String.format("'%s'", stringValue);
    }

    @Override
    public String toTimestamp(Timestamp timestamp) {
        return null;
    }
}
