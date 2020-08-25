package com.github.braisdom.jsql.ast;

public class FormalParameterNode {
    private String type;
    private String name;

    public FormalParameterNode(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
