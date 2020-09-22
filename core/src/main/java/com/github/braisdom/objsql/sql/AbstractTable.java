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

import com.github.braisdom.objsql.Tables;

import java.util.Arrays;
import java.util.Objects;

public abstract class AbstractTable extends AbstractExpression implements Dataset {

    protected final Class modelClass;

    public AbstractTable(Class modelClass) {
        Objects.requireNonNull(modelClass, "The modelClass cannot be null");
        this.modelClass = modelClass;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        String[] nameParts = Tables.getTableName(modelClass).split("\\.");
        String[] quotedNameParts = Arrays.stream(nameParts)
                .map(namePart -> expressionContext.quoteTable(namePart)).toArray(String[]::new);
        String tableAlias = expressionContext.getAlias(this, true);
        return String.format("%s AS %s", String.join("\\.", quotedNameParts),
                expressionContext.quoteColumn(tableAlias));
    }
}
