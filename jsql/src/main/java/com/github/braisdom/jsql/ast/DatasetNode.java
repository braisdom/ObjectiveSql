package com.github.braisdom.jsql.ast;

import java.util.ArrayList;
import java.util.List;

public class DatasetNode {
    private String name;
    private List<FormalParameterNode> formalParameterNodes = new ArrayList<>();
    private List<Projectional> projectionals = new ArrayList<>();

    public void addFormalParameter(FormalParameterNode formalParameterNode) {
        formalParameterNodes.add(formalParameterNode);
    }

    public void addProjectional(Projectional projectional) {
        projectionals.add(projectional);
    }

    public DatasetNode setName(String name) {
        this.name = name;
        return this;
    }

    public String getName() {
        return name;
    }

    public List<FormalParameterNode> getFormalParameterNodes() {
        return formalParameterNodes;
    }

    public List<Projectional> getProjectionals() {
        return projectionals;
    }
}
