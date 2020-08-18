package com.github.braisdom.jsql.ast;

public class ImportNode {

    private String qualifiedName;

    public ImportNode(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getQualifiedName() {
        return qualifiedName;
    }
}
