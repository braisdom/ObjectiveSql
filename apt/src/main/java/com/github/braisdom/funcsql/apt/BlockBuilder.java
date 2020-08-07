package com.github.braisdom.funcsql.apt;

import com.sun.tools.javac.tree.TreeMaker;

public class BlockBuilder {

    private final TreeMaker treeMaker;
    private final APTHandler node;

    BlockBuilder(APTHandler node) {
        this.treeMaker = node.getTreeMaker();
        this.node = node;
    }
}
