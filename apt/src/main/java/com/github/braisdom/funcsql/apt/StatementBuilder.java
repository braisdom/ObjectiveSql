package com.github.braisdom.funcsql.apt;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;

public class StatementBuilder {

    private final TreeMaker treeMaker;
    private final APTHandler node;

    StatementBuilder(APTHandler node) {
        this.treeMaker = node.getTreeMaker();
        this.node = node;
    }

    public static JCTree.JCExpression staticMethodCall(APTHandler handler, Class<?> clazz,
                                                       String methodName, JCTree.JCExpression... params) {
        TreeMaker treeMaker = handler.getTreeMaker();
        return treeMaker.Apply(List.nil(), treeMaker.Select(
                handler.typeRef(clazz.getName()), handler.toName(methodName)), List.from(params));
    }

}
