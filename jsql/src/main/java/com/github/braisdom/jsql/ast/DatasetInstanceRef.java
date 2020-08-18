package com.github.braisdom.jsql.ast;

public class DatasetInstanceRef extends Aliasable implements Projectional{
    private String datasetName;
    private String refName;

    public String getDatasetName() {
        return datasetName;
    }

    public DatasetInstanceRef setDatasetName(String datasetName) {
        this.datasetName = datasetName;
        return this;
    }

    public String getRefName() {
        return refName;
    }

    public DatasetInstanceRef setRefName(String refName) {
        this.refName = refName;
        return this;
    }
}
