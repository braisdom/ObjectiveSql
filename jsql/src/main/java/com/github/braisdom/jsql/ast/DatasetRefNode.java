package com.github.braisdom.jsql.ast;

public class DatasetRefNode extends Aliasable implements Projectional, Inclusived {
    private String schemaName;
    private String refName;

    public String getSchemaName() {
        return schemaName;
    }

    public DatasetRefNode setSchemaName(String schemaName) {
        this.schemaName = schemaName;
        return this;
    }

    public String getRefName() {
        return refName;
    }

    public DatasetRefNode setRefName(String refName) {
        this.refName = refName;
        return this;
    }
}
