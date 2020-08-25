package com.github.braisdom.jds.ast;

import java.util.ArrayList;
import java.util.List;

public class SqlNode {

    private final List<ImportNode> importNodes = new ArrayList<>();
    private final List<DatasetNode> datasetNodes = new ArrayList<>();
    private LogicExpression logicExpression;

    public SqlNode() {
    }

    public void addImportNode(ImportNode enumNode) {
        importNodes.add(enumNode);
    }

    public void addDatasetNode(DatasetNode datasetNode) {
        datasetNodes.add(datasetNode);
    }

    public List<ImportNode> getImportNodes() {
        return importNodes;
    }

    public List<DatasetNode> getDatasetNodes() {
        return datasetNodes;
    }

    public LogicExpression getLogicExpression() {
        return logicExpression;
    }

    public void setLogicExpression(LogicExpression logicExpression) {
        this.logicExpression = logicExpression;
    }
}
