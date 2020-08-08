package com.github.braisdom.funcsql.apt;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

public class StatementBuilder {

    private final TreeMaker treeMaker;
    private final APTHandler handler;

    private final ListBuffer<JCTree.JCStatement> jcStatements;

    StatementBuilder(APTHandler handler) {
        this.treeMaker = handler.getTreeMaker();
        this.handler = handler;

        this.jcStatements = new ListBuffer<>();
    }

    public StatementBuilder append(JCExpression varType, String varName, Class invokedClass,
                                String method, List<JCExpression> params) {
        JCExpression methodInvocation = treeMaker.Apply(List.nil(), treeMaker.Select(handler.typeRef(invokedClass),
                handler.toName(method)), params);
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), handler.toName(varName),
                varType, methodInvocation));
        return this;
    }

    public StatementBuilder append(JCExpression varType, String name, String invokedClassName,
                                String method, JCExpression... params) {
        JCExpression methodInvoke = treeMaker.Apply(List.nil(), treeMaker.Select(handler.typeRef(invokedClassName),
                handler.toName(method)), List.from(params));
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), handler.toName(name),
                varType, methodInvoke));
        return this;
    }

    public StatementBuilder append(String varName, String methodName, List<JCExpression> params) {
        jcStatements.append(treeMaker.Exec(treeMaker.Apply(List.nil(),treeMaker.Select(handler.varRef(varName),
                handler.toName(methodName)), params)));
        return this;
    }

    public StatementBuilder append(JCExpression varType, String name, JCExpression init) {
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), handler.toName(name),
                varType, init));
        return this;
    }

    public List<JCTree.JCStatement> build() {
        return jcStatements.toList();
    }

}
