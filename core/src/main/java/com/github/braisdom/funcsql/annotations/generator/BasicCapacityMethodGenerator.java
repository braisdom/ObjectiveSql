package com.github.braisdom.funcsql.annotations.generator;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

public class BasicCapacityMethodGenerator extends AbstractMethodGenerator {

    @Override
    public ImportItem[] getImportItems() {
        return new ImportItem[0];
    }

    @Override
    public JCTree.JCMethodDecl[] generate(TreeMaker treeMaker, Names names) {
        return new JCTree.JCMethodDecl[0];
    }

}
