package com.github.braisdom.jsql.ast;

public class SymbolRefNode extends Aliasable implements Projectional{
    private String symbolName;

    public String getSymbolName() {
        return symbolName;
    }

    public SymbolRefNode setSymbolName(String symbolName) {
        this.symbolName = symbolName;
        return this;
    }
}
