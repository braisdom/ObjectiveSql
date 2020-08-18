package com.github.braisdom.jsql.ast;

import java.util.ArrayList;
import java.util.List;

public class DatasetNode {
    private String name;
    private List<FormalParameter> formalParameters = new ArrayList<>();
    private List<Projectional> projectionals = new ArrayList<>();

    public void addFormalParameter(FormalParameter formalParameter) {
        formalParameters.add(formalParameter);
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

    public List<FormalParameter> getFormalParameters() {
        return formalParameters;
    }

    public List<Projectional> getProjectionals() {
        return projectionals;
    }
}
