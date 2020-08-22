package com.github.braisdom.jsql.ast;

public class BetweenNode {
    private boolean negated;
    private SymbolNode symbolNode;
    private Operand lower;
    private Operand upper;

    public interface Operand {}

    public boolean isNegated() {
        return negated;
    }

    public void setNegated(boolean negated) {
        this.negated = negated;
    }

    public SymbolNode getSymbolNode() {
        return symbolNode;
    }

    public void setSymbolNode(SymbolNode symbolNode) {
        this.symbolNode = symbolNode;
    }

    public Operand getLower() {
        return lower;
    }

    public void setLower(Operand lower) {
        this.lower = lower;
    }

    public Operand getUpper() {
        return upper;
    }

    public void setUpper(Operand upper) {
        this.upper = upper;
    }
}
