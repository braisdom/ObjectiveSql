package com.github.braisdom.objsql.pagination;

import java.util.List;

public interface PagedList<T> extends List<T> {

    long getTotalSize();

    int getPageIndex();

    int getPageCount();
}
