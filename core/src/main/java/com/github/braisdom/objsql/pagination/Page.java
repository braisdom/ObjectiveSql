package com.github.braisdom.objsql.pagination;

public class Page {
    private int page;
    private int pageSize;

    public Page(int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
    }

    public Page of(int page, int pageSize) {
        return new Page(page, pageSize);
    }
}
