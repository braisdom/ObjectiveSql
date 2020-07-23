package com.github.braisdom.funcsql.generator;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import javax.lang.model.element.Element;

public class PersistenceMethodGenerator extends AbstractCodeGenerator implements CodeGenerator{

    public PersistenceMethodGenerator() {
    }

    @Override
    public JCTree.JCMethodDecl[] generateMethods(TreeMaker treeMaker, Names names, Element element, JCTree.JCClassDecl jcClassDecl) {
        return super.generateMethods(treeMaker, names, element, jcClassDecl);
    }
}
