package com.github.braisdom.funcsql.generator;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Names;

import javax.lang.model.element.Element;

public interface CodeGenerator {

    class ImportItem {

        private String packageName;
        private String className;

        public ImportItem(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getClassName() {
            return className;
        }
    }

    ImportItem[] getImportItems();

    JCTree.JCVariableDecl[] generateVariables(TreeMaker treeMaker, Names names,
                                          Element element, JCTree.JCClassDecl jcClassDecl);

    JCTree.JCMethodDecl[] generateMethods(TreeMaker treeMaker, Names names,
                                          Element element, JCTree.JCClassDecl jcClassDecl);
}
