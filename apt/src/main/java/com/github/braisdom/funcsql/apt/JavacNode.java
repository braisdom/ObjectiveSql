package com.github.braisdom.funcsql.apt;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;

public class JavacNode {

    private final JCTree.JCClassDecl classDecl;
    private final Element element;
    private final JCTree ast;
    private final TreeMaker treeMaker;
    private final Names names;
    private final Messager messager;

    public JavacNode(JCTree.JCClassDecl classDecl, Element element, JCTree ast, TreeMaker treeMaker,
                     Names names, Messager messager) {
        this.classDecl = classDecl;
        this.element = element;
        this.ast = ast;
        this.treeMaker = treeMaker;
        this.names = names;
        this.messager = messager;
    }

    public JCTree get() {
        return ast;
    }

    public TreeMaker getTreeMaker() {
        return treeMaker;
    }

    public ElementKind getKind() {
        return this.element.getKind();
    }

    public Name toName(String name) {
        return this.names.fromString(name);
    }

    public JCTree.JCExpression typeRef(Class clazz) {
        return typeRef(clazz.getName());
    }

    public void append(JCTree.JCVariableDecl variableDecl) {
        classDecl.defs = classDecl.defs.append(variableDecl);
    }

    public JCTree.JCExpression typeRef(String complexName) {
        String[] parts = complexName.split("\\.");
        if (parts.length > 2 && parts[0].equals("java") && parts[1].equals("lang")) {
            String[] subParts = new String[parts.length - 2];
            System.arraycopy(parts, 2, subParts, 0, subParts.length);
            return javaLangTypeRef(subParts);
        }

        return chainDots(parts);
    }

    public JCTree.JCExpression javaLangTypeRef(String... simpleNames) {
        return chainDots(null, null, simpleNames);
    }

    public JCTree.JCExpression chainDots(String elem1, String elem2, String... elems) {
        return chainDots(-1, elem1, elem2, elems);
    }

    public JCTree.JCExpression chainDots(String... elems) {
        assert elems != null;

        JCTree.JCExpression e = null;
        for (String elem : elems) {
            if (e == null) e = treeMaker.Ident(toName(elem));
            else e = treeMaker.Select(e, toName(elem));
        }
        return e;
    }

    public JCTree.JCExpression chainDots(int pos, String elem1, String elem2, String... elems) {
        assert elems != null;
        TreeMaker treeMaker = getTreeMaker();
        if (pos != -1) treeMaker = treeMaker.at(pos);
        JCTree.JCExpression e = null;
        if (elem1 != null) e = treeMaker.Ident(toName(elem1));
        if (elem2 != null) e = e == null ? treeMaker.Ident(toName(elem2)) : treeMaker.Select(e, toName(elem2));
        for (int i = 0 ; i < elems.length ; i++) {
            e = e == null ? treeMaker.Ident(toName(elems[i])) : treeMaker.Select(e, toName(elems[i]));
        }

        assert e != null;

        return e;
    }
}
