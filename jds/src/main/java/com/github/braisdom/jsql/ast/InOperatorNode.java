package com.github.braisdom.jsql.ast;

import java.util.ArrayList;
import java.util.List;

public class InOperatorNode {
    private boolean negated;
    private SymbolNode symbolNode;
    private List<Operand> items = new ArrayList<>();

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

    public List<Operand> getItems() {
        return items;
    }

    public void addItem(Operand item) {
        items.add(item);
    }
}
