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
package com.github.braisdom.objsql.pagination;

import java.util.*;

public class DefaultPagedList<T> implements PagedList<T> {

    private final long totalSize;
    private final Page page;
    private final int pageCount;
    private final Collection<T> result;

    public DefaultPagedList(Collection<T> result, long totalSize,
                            Page page, int pageCount) {
        Objects.requireNonNull(result, "The result cannot be null");
        this.result = result;
        this.totalSize = totalSize;
        this.page = page;
        this.pageCount = pageCount;
    }

    public static PagedList createEmptyList(Page page) {
        return new DefaultPagedList(Collections.EMPTY_LIST, 0, page, 0);
    }

    @Override
    public long totalSize() {
        return totalSize;
    }

    @Override
    public int size() {
        return result.size();
    }

    @Override
    public Page getPage() {
        return page;
    }

    @Override
    public int getPageCount() {
        return pageCount;
    }

    @Override
    public Iterator<T> iterator() {
        return result.iterator();
    }
}
