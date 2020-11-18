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
package com.github.braisdom.objsql.sql;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.Databases;
import com.github.braisdom.objsql.Quoter;

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
        if (dataset.getAlias() != null) {
            return dataset.getAlias();
        }
        if (!datasets.contains(dataset)) {
            datasets.add(dataset);
        }
        return String.format("T%d", datasets.indexOf(dataset));
    }

    @Override
    public String quoteTable(String tableName) {
        Quoter quoter = Databases.getQuoter();
        return quoter.quoteTableName(databaseType.getName(), tableName);
    }

    @Override
    public String quoteColumn(String columnName) {
        Quoter quoter = Databases.getQuoter();
        return quoter.quoteColumnName(databaseType.getName(), columnName);
    }

    @Override
    public String quoteString(String stringValue) {
        return String.format("'%s'", stringValue);
    }
}
