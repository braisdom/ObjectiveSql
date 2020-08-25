package com.github.braisdom.jsql.ast;

public abstract class Aliasable {
    private String alias;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }
}
