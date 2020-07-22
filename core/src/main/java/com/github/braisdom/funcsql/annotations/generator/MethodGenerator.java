package com.github.braisdom.funcsql.annotations.generator;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

public interface MethodGenerator {

    ImportItem[] getImportItems();

    JCTree.JCMethodDecl[] generate(TreeMaker treeMaker, Names names);
}
