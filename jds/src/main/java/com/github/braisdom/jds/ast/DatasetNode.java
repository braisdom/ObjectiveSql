package com.github.braisdom.jds.ast;

import java.util.ArrayList;
import java.util.List;

public class DatasetNode {
    private String name;
    private List<FormalParameterNode> formalParameterNodes = new ArrayList<>();
    private List<Projectional> projectionals = new ArrayList<>();
    private List<FromNode> fromNodes = new ArrayList<>();
    private LogicExpression predicate;

    public void addFormalParameter(FormalParameterNode formalParameterNode) {
        formalParameterNodes.add(formalParameterNode);
    }

    public void addProjectional(Projectional projectional) {
        projectionals.add(projectional);
    }

    public void setName(String name) {
        this.name = name;
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

    public void addFromNode(FromNode fromNode) {
        fromNodes.add(fromNode);
    }

    public List<FromNode> getFromNodes() {
        return fromNodes;
    }

    public LogicExpression getPredicate() {
        return predicate;
    }

    public void setPredicate(LogicExpression predicate) {
        this.predicate = predicate;
    }
}
