package com.github.braisdom.jsql.ast;

public class FunctionCallNode extends Aliasable implements Projectional {
    private String externalName;
    private String functionName;
    private String expression;

    public FunctionCallNode setExternalName(String externalName) {
        this.externalName = externalName;
        return this;
    }

    public String getExternalName() {
        return externalName;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
