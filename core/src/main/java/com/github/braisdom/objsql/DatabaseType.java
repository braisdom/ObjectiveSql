/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.braisdom.objsql;

import java.util.Objects;

/**
 * The definitions of database for ObjectiveSql supporting
 */
public class DatabaseType {

    public static final String DATABASE_PRODUCT_NAME_ANY = "any";

    public static final DatabaseType SQLite = new DatabaseType("SQLite", -1);
    public static final DatabaseType MySQL = new DatabaseType("MySQL", 5);
    public static final DatabaseType MySQL8 = new DatabaseType("MySQL", 8);
    public static final DatabaseType Oracle = new DatabaseType("Oracle", 11);
    public static final DatabaseType Oracle12c = new DatabaseType("Oracle12c", 12);
    public static final DatabaseType PostgreSQL = new DatabaseType("PostgreSQL", -1);
    public static final DatabaseType MsSqlServer = new DatabaseType("Microsoft SQL Server", -1);
    public static final DatabaseType HSQLDB = new DatabaseType("HSQL Database Engine", 12);
    public static final DatabaseType Ansi = new DatabaseType(DATABASE_PRODUCT_NAME_ANY, -1);

    private String databaseProductName;
    private int majorVersion = -1;

    protected DatabaseType(String databaseProductName) {
        this(databaseProductName, -1);
    }

    protected DatabaseType(String databaseProductName, int majorVersion) {
        Objects.requireNonNull(databaseProductName, "The databaseProductName cannot be null");

        this.databaseProductName = databaseProductName;
        this.majorVersion = majorVersion;
    }

    public static DatabaseType create(String databaseProductName, int majorVersion) {
        return new DatabaseType(databaseProductName, majorVersion);
    }

    public String getDatabaseProductName() {
        return databaseProductName;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DatabaseType) {
            return equals((DatabaseType) obj);
        }
        return super.equals(obj);
    }

    public boolean equals(DatabaseType databaseType) {
        return equals(databaseType.getDatabaseProductName(),
                databaseType.getMajorVersion());
    }

    public boolean equals(String databaseProductionName) {
        return equals(databaseProductionName, -1);
    }

    public boolean equals(String databaseProductName, int majorVersion) {
        Objects.requireNonNull(databaseProductName, "The databaseProductName cannot be null");

        if (DATABASE_PRODUCT_NAME_ANY.equals(this.databaseProductName)) {
            return true;
        } else {
            if (databaseProductName.equals(this.databaseProductName)) {
                if (majorVersion == -1 || this.majorVersion == -1) {
                    return true;
                } else {
                    return majorVersion <= this.majorVersion;
                }
            } else {
                return false;
            }
        }
    }

}
