package com.github.braisdom.funcsql.generator;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCodeGenerator implements CodeGenerator {

    private final List<ImportItem> importItems;

    public AbstractCodeGenerator() {
        importItems = new ArrayList<>();
    }

    @Override
    public ImportItem[] getImportItems() {
        return importItems.toArray(new ImportItem[]{});
    }

    protected void addImportItem(String packageName, String className) {
        importItems.add(new ImportItem(packageName, className));
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
