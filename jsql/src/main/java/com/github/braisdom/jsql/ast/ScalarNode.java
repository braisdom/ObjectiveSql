package com.github.braisdom.jsql.ast;

public class ScalarNode {
    private Object value;

    public Object getValue() {
        return value;
    }

    public ScalarNode setValue(Object value) {
        this.value = value;
        return this;
    }
}
