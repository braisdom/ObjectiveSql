package com.github.braisdom.objsql.pagination;

import java.util.List;

public interface PagedList extends List {

    long getTotalSize();

    int getPageIndex();

    int getPageCount();
}
