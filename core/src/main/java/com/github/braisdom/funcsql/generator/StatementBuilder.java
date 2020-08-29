package com.github.braisdom.funcsql.generator;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;

public class StatementBuilder {

    private final TreeMaker treeMaker;
    private final APTBuilder aptBuilder;

    private final ListBuffer<JCTree.JCStatement> jcStatements;

    StatementBuilder(APTBuilder aptBuilder) {
        this.treeMaker = aptBuilder.getTreeMaker();
        this.aptBuilder = aptBuilder;

        this.jcStatements = new ListBuffer<>();
    }

    public StatementBuilder append(JCExpression varType, String varName, Class invokedClass,
                                   String method, List<JCExpression> params) {
        JCExpression methodInvocation = treeMaker.Apply(List.nil(), treeMaker.Select(aptBuilder.typeRef(invokedClass),
                aptBuilder.toName(method)), params);
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), aptBuilder.toName(varName),
                varType, methodInvocation));
        return this;
    }

    public StatementBuilder append(String varName, String methodName, JCExpression... params) {
        JCExpression methodRef = treeMaker.Select(aptBuilder.varRef(varName),
                aptBuilder.toName(methodName));
        jcStatements.append(treeMaker.Exec(treeMaker.Apply(List.nil(), methodRef, List.from(params))));
        return this;
    }

    public StatementBuilder append(Class clazz, String methodName, JCExpression... params) {
        JCExpression methodRef = treeMaker.Select(aptBuilder.typeRef(clazz),
                aptBuilder.toName(methodName));
        jcStatements.append(treeMaker.Exec(treeMaker.Apply(List.nil(), methodRef, List.from(params))));
        return this;
    }

    public StatementBuilder append(Class typeClass, String name, Class invokedClass,
                                   String method, JCExpression... params) {
        append(aptBuilder.typeRef(typeClass), name, invokedClass.getName(), method, params);
        return this;
    }

    public StatementBuilder append(JCExpression varType, String name, Class invokedClass,
                                   String method, JCExpression... params) {
        append(varType, name, invokedClass.getName(), method, params);
        return this;
    }

    public StatementBuilder append(JCExpression varType, String name, String invokedClassName,
                                   String method, JCExpression... params) {
        JCExpression methodInvoke = treeMaker.Apply(List.nil(), treeMaker.Select(aptBuilder.typeRef(invokedClassName),
                aptBuilder.toName(method)), List.from(params));
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), aptBuilder.toName(name),
                varType, methodInvoke));
        return this;
    }

    public StatementBuilder append(JCExpression varType, String name, String instanceMethodName, JCExpression... params) {
        JCExpression methodInvoke = treeMaker.Apply(List.nil(), aptBuilder.varRef(instanceMethodName), List.from(params));
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), aptBuilder.toName(name),
                varType, methodInvoke));
        return this;
    }

    public StatementBuilder append(String varName, String methodName, List<JCExpression> params) {
        jcStatements.append(treeMaker.Exec(treeMaker.Apply(List.nil(),treeMaker.Select(aptBuilder.varRef(varName),
                aptBuilder.toName(methodName)), params)));
        return this;
    }

    public StatementBuilder append(JCExpression varType, String name, JCExpression init) {
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), aptBuilder.toName(name),
                varType, init));
        return this;
    }

    public List<JCTree.JCStatement> build() {
        return jcStatements.toList();
    }

}
