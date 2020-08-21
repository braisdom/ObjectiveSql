package com.github.braisdom.jsql.ast;

public class BetweenNode {
    private SymbolNode symbolNode;
    private Inclusived begin;
    private Inclusived end;

    public SymbolNode getSymbolNode() {
        return symbolNode;
    }

    public void setSymbolNode(SymbolNode symbolNode) {
        this.symbolNode = symbolNode;
    }

    public Inclusived getBegin() {
        return begin;
    }

    public void setBegin(Inclusived begin) {
        this.begin = begin;
    }

    public Inclusived getEnd() {
        return end;
    }

    public void setEnd(Inclusived end) {
        this.end = end;
    }
}
