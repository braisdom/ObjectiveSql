package com.github.braisdom.funcsql.generator;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import javax.lang.model.element.Element;

public interface MethodGenerator extends ClassImportable {

    JCTree.JCMethodDecl[] generate(TreeMaker treeMaker, Names names,
                                   Element element, JCTree.JCClassDecl jcClassDecl);
}
