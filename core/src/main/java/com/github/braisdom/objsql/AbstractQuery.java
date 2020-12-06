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

/**
 * The class provides default implementations of structure of SQL
 * @param <T>
 */
public abstract class AbstractQuery<T> implements Query<T> {

    protected final DomainModelDescriptor<T> domainModelDescriptor;

    protected long rowCount = -1;
    protected long offset = -1;
    protected boolean fetchNext = true;

    protected String projections;
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
        this.projections = String.join(", ", columns);
        return this;
    }

    @Override
    public Query offset(long offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public Query fetch(long rowCount) {
        this.rowCount = rowCount;
        return this;
    }

    @Override
    public Query fetch(long rowCount, boolean fetchNext) {
        this.rowCount = rowCount;
        this.fetchNext = fetchNext;
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
    
    protected String getTableName(Class tableClass) {
        return Tables.getTableName(tableClass);
    }
}
