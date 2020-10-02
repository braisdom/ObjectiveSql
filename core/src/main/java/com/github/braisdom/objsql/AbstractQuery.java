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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * The class provides the default implementations of structure of SQL
 * @param <T>
 */
public abstract class AbstractQuery<T> implements Query<T> {

    protected final DomainModelDescriptor<T> domainModelDescriptor;

    protected int limit = -1;
    protected int offset = -1;

    protected String projection;
    protected String filter;
    protected Object[] params;
    protected String orderBy;
    protected String groupBy;
    protected String having;

    public AbstractQuery(Class<T> domainModelClass) {
        this(new BeanModelDescriptor<>(domainModelClass));
    }

    public AbstractQuery(DomainModelDescriptor<T> domainModelDescriptor) {
        this.domainModelDescriptor = domainModelDescriptor;
    }

    @Override
    public Query where(String filter, Object... params) {
        this.filter = filter;
        this.params = params;
        return this;
    }

    @Override
    public Query select(String... columns) {
        this.projection = String.join(", ", columns);
        return this;
    }

    @Override
    public Query limit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public Query orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    @Override
    public Query groupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    @Override
    public Query having(String having) {
        this.having = having;
        return this;
    }

    @Override
    public Query offset(int offset) {
        this.offset = offset;
        return this;
    }

    protected String getTableName(Class tableClass) {
        return Tables.getTableName(tableClass);
    }
}
