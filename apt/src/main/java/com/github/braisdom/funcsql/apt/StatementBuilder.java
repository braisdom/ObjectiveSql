package com.github.braisdom.funcsql.apt;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

public class StatementBuilder {

    private final TreeMaker treeMaker;
    private final APTHandler node;

    private final ListBuffer<JCTree.JCStatement> jcStatements;

    StatementBuilder(APTHandler node) {
        this.treeMaker = node.getTreeMaker();
        this.node = node;

        this.jcStatements = new ListBuffer<>();
    }

}
