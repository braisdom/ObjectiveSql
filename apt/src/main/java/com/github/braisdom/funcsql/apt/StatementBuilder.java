package com.github.braisdom.funcsql.apt;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

public class StatementBuilder {

    private final TreeMaker treeMaker;
    private final APTUtils aptUtils;

    private final ListBuffer<JCTree.JCStatement> jcStatements;

    StatementBuilder(APTUtils aptUtils) {
        this.treeMaker = aptUtils.getTreeMaker();
        this.aptUtils = aptUtils;

        this.jcStatements = new ListBuffer<>();
    }

    public StatementBuilder append(JCExpression varType, String varName, Class invokedClass,
                                String method, List<JCExpression> params) {
        JCExpression methodInvocation = treeMaker.Apply(List.nil(), treeMaker.Select(aptUtils.typeRef(invokedClass),
                aptUtils.toName(method)), params);
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), aptUtils.toName(varName),
                varType, methodInvocation));
        return this;
    }

    public StatementBuilder append(String varName, String methodName, JCExpression... params) {
        JCTree.JCExpression methodRef = treeMaker.Select(aptUtils.varRef(varName),
                aptUtils.toName(methodName));
        jcStatements.append(treeMaker.Exec(treeMaker.Apply(List.nil(), methodRef, List.from(params))));
        return this;
    }

    public StatementBuilder append(Class clazz, String methodName, JCExpression... params) {
        JCTree.JCExpression methodRef = treeMaker.Select(aptUtils.typeRef(clazz),
                aptUtils.toName(methodName));
        jcStatements.append(treeMaker.Exec(treeMaker.Apply(List.nil(), methodRef, List.from(params))));
        return this;
    }

    public StatementBuilder append(Class typeClass, String name, Class invokedClass,
                                   String method, JCExpression... params) {
        append(aptUtils.typeRef(typeClass), name, invokedClass.getName(), method, params);
        return this;
    }

    public StatementBuilder append(JCExpression varType, String name, Class invokedClass,
                                   String method, JCExpression... params) {
        append(varType, name, invokedClass.getName(), method, params);
        return this;
    }

    public StatementBuilder append(JCExpression varType, String name, String invokedClassName,
                                    String method, JCExpression... params) {
        JCExpression methodInvoke = treeMaker.Apply(List.nil(), treeMaker.Select(aptUtils.typeRef(invokedClassName),
                aptUtils.toName(method)), List.from(params));
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), aptUtils.toName(name),
                varType, methodInvoke));
        return this;
    }

    public StatementBuilder append(JCExpression varType, String name, String instanceMethodName, JCExpression... params) {
        JCExpression methodInvoke = treeMaker.Apply(List.nil(), aptUtils.varRef(instanceMethodName), List.from(params));
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), aptUtils.toName(name),
                varType, methodInvoke));
        return this;
    }

    public StatementBuilder append(String varName, String methodName, List<JCExpression> params) {
        jcStatements.append(treeMaker.Exec(treeMaker.Apply(List.nil(),treeMaker.Select(aptUtils.varRef(varName),
                aptUtils.toName(methodName)), params)));
        return this;
    }

    public StatementBuilder append(JCExpression varType, String name, JCExpression init) {
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), aptUtils.toName(name),
                varType, init));
        return this;
    }

    public List<JCTree.JCStatement> build() {
        return jcStatements.toList();
    }

}
