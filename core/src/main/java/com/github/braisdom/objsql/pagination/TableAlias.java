package com.github.braisdom.objsql.pagination;

import net.sf.jsqlparser.expression.Alias;

public class TableAlias extends Alias {

    public TableAlias(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return String.format(" %s", getName());
    }
}
