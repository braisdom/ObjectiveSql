package com.github.braisdom.funcsql.generator;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import javax.lang.model.element.Element;

public class PrimaryFieldGenerator extends AbstractCodeGenerator {

    @Override
    public ImportItem[] getImportItems() {
        return new ImportItem[0];
    }

    @Override
    public JCTree.JCVariableDecl[] generateVariables(TreeMaker treeMaker, Names names, Element element, JCTree.JCClassDecl jcClassDecl) {
        return new JCTree.JCVariableDecl[0];
    }

    @Override
    public JCTree.JCMethodDecl[] generateMethods(TreeMaker treeMaker, Names names, Element element, JCTree.JCClassDecl jcClassDecl) {
        return new JCTree.JCMethodDecl[0];
    }
}
