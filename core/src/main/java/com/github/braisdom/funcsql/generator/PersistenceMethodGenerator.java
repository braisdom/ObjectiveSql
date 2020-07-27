package com.github.braisdom.funcsql.generator;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import javax.lang.model.element.Element;

public class PersistenceMethodGenerator implements CodeGenerator {

    @Override
    public ImportItem[] getImportItems() {
        return new ImportItem[0];
    }

    @Override
    public JCTree.JCMethodDecl[] generateMethods(TreeMaker treeMaker, Names names, Element element, JCTree.JCClassDecl jcClassDecl) {
        return new JCTree.JCMethodDecl[0];
    }
}
