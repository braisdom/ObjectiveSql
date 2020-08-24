package com.github.braisdom.jsql.runtime;

public class JSymbol implements SqlParameter {

    private final Object value;

    public JSymbol(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public String toSql() {
        return String.valueOf(value);
    }
}
