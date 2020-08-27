package com.github.braisdom.jds.ast;

import java.util.ArrayList;
import java.util.List;

public class DatasetNode {
    private String name;
    private List<FormalParameterNode> formalParameterNodes = new ArrayList<>();
    private List<Projectional> projectionals = new ArrayList<>();
    private FromNode fromNode;
    private LogicExpressionNode predicate;

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

    public FromNode getFromNode() {
        return fromNode;
    }

    public void setFromNode(FromNode fromNode) {
        this.fromNode = fromNode;
    }

    public LogicExpressionNode getPredicate() {
        return predicate;
    }

    public void setPredicate(LogicExpressionNode predicate) {
        this.predicate = predicate;
    }
}
