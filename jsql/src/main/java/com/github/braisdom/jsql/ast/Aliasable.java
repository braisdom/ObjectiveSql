package com.github.braisdom.jsql.ast;

public class Aliasable {
    private String alias;

    public String getAlias() {
        return alias;
    }

    public Aliasable setAlias(String alias) {
        this.alias = alias;
        return this;
    }
}
