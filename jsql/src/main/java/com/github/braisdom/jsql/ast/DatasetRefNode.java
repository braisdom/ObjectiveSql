package com.github.braisdom.jsql.ast;

import java.util.ArrayList;
import java.util.List;

public class DatasetRefNode extends Aliasable implements Queryable {
    private String typeName;
    private List<String> actualParameters = new ArrayList<>();

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void addActualParameter(String actualParameterName) {
        actualParameters.add(actualParameterName);
    }

    public List<String> getActualParameters() {
        return actualParameters;
    }
}
