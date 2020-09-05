package com.github.braisdom.objsql;

public enum DatabaseType {
    MySQL5("MySQL"),
    MySQL8("MySQL"),
    PostgreSQL("PostgreSQL"),
    Oracle("Oracle"),
    MsSqlServer("MsSqlServer"),
    MariaDB("MariaDB"),
    SQLite("SQLite"),
    H2Database("H2Database"),
    Clickhouse("Clickhouse"),
    All("All"),
    Unknown("Unknown");

    private String name;

    DatabaseType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean nameEquals(String name) {
        return this.name.equalsIgnoreCase(name);
    }
}
