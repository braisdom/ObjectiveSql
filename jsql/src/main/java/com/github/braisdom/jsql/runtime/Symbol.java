package com.github.braisdom.jsql.runtime;

public class Symbol implements SqlParameter {

    private final Object value;

    public Symbol(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public String toSql() {
        return String.valueOf(value);
    }
}
