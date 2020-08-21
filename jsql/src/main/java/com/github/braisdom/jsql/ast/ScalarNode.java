package com.github.braisdom.jsql.ast;

public class ScalarNode extends Aliasable implements Projectional, Expression {
    private Object value;

    public ScalarNode(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public ScalarNode setValue(Object value) {
        this.value = value;
        return this;
    }
}
