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

    public FunctionCallNode setFunctionName(String functionName) {
        this.functionName = functionName;
        return this;
    }

    public String getExpression() {
        return expression;
    }

    public FunctionCallNode setExpression(String expression) {
        this.expression = expression;
        return this;
    }
}
