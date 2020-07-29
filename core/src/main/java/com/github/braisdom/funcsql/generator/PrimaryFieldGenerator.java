package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import javax.lang.model.element.Element;
import java.util.ArrayList;

public class PrimaryFieldGenerator extends AbstractCodeGenerator {

    @Override
    public ImportItem[] getImportItems() {
        java.util.List<ImportItem> importItems = new ArrayList<>();

        importItems.add(new ImportItem(PrimaryKey.class.getPackage().getName(), PrimaryKey.class.getSimpleName()));

        return importItems.toArray(new ImportItem[]{});

    }

    @Override
    public JCTree.JCVariableDecl[] generateVariables(TreeMaker treeMaker, Names names,
                                                     Element element, JCTree.JCClassDecl jcClassDecl) {
        return new JCTree.JCVariableDecl[0];
    }

    @Override
    public JCTree.JCMethodDecl[] generateMethods(TreeMaker treeMaker, Names names,
                                                 Element element, JCTree.JCClassDecl jcClassDecl) {
        return new JCTree.JCMethodDecl[0];
    }
}
