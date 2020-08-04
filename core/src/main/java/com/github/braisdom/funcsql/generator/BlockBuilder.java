package com.github.braisdom.funcsql.generator;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import lombok.javac.JavacNode;
import lombok.javac.JavacTreeMaker;

import static lombok.javac.handlers.JavacHandlerUtil.genTypeRef;

class BlockBuilder {

    private final JavacTreeMaker treeMaker;
    private final JavacNode typeNode;
    private final ListBuffer<JCTree.JCStatement> jcStatements;

    private BlockBuilder(JavacTreeMaker treeMaker, JavacNode typeNode){
        this.treeMaker = treeMaker;
        this.typeNode = typeNode;
        this.jcStatements = new ListBuffer<>();
    }

    public static BlockBuilder newBlock(JavacTreeMaker treeMaker, JavacNode typeNode) {
        return new BlockBuilder(treeMaker, typeNode);
    }

    public BlockBuilder appendVar(Class<?> typeClass, String name, String invokedClassName,
                               String staticMethodName, JCTree.JCExpression... params) {
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), typeNode.toName(name),
                genTypeRef(typeNode, typeClass.getName()),
                treeMaker.Apply(List.nil(), treeMaker.Select(
                        treeMaker.Ident(typeNode.toName(invokedClassName)), typeNode.toName(staticMethodName)), List.from(params))));
        return this;
    }

    public BlockBuilder appendVar(Class<?> typeClass, String name, Class<?> invokedClass,
                               String staticMethodName, JCTree.JCExpression... params) {
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), typeNode.toName(name),
                genTypeRef(typeNode, typeClass.getName()),
                treeMaker.Apply(List.nil(), treeMaker.Select(
                        genTypeRef(typeNode, invokedClass.getName()), typeNode.toName(staticMethodName)), List.from(params))));
        return this;
    }

    public BlockBuilder appendVar(Class<?> typeClass, String name, JCTree.JCExpression init) {
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), typeNode.toName(name),
                genTypeRef(typeNode, typeClass.getName()), init));
        return this;
    }

    public BlockBuilder appendVar(JCTree.JCTypeApply typeApply, String name, JCTree.JCExpression init) {
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), typeNode.toName(name), typeApply, init));
        return this;
    }

    public BlockBuilder appendStaticMethodInvoke(Class<?> typeClass, String name, JCTree.JCExpression... params) {
        JCTree.JCExpression methodRef = treeMaker.Select(genTypeRef(typeNode, typeClass.getName()),
                typeNode.toName(name));
        jcStatements.append(treeMaker.Exec(treeMaker.Apply(List.nil(),
                methodRef, List.from(params))));
        return this;
    }

    public BlockBuilder appendStaticMethodInvoke(String varName, String name, JCTree.JCExpression... params) {
        JCTree.JCExpression methodRef = treeMaker.Select(treeMaker.Ident(typeNode.toName(varName)),
                typeNode.toName(name));
        jcStatements.append(treeMaker.Exec(treeMaker.Apply(List.nil(),
                methodRef, List.from(params))));
        return this;
    }

    public BlockBuilder appendInstanceMethodInvoke(String name, JCTree.JCExpression... params) {
        jcStatements.append(treeMaker.Exec(treeMaker.Apply(List.nil(),
                treeMaker.Ident(typeNode.toName(name)), List.from(params))));
        return this;
    }

    public BlockBuilder appendVar(String typeClassName, String name, JCTree.JCExpression init) {
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), typeNode.toName(name),
                treeMaker.Ident(typeNode.toName(typeClassName)), init));
        return this;
    }

    public BlockBuilder appendReturn(String varName, String varMethodName, JCTree.JCExpression... params) {
        JCTree.JCExpression invokeRef = treeMaker.Select(treeMaker.Ident(typeNode.toName(varName)), typeNode.toName(varMethodName));
        JCTree.JCMethodInvocation returnInv = treeMaker.Apply(List.nil(), invokeRef, List.from(params));

        jcStatements.append(treeMaker.Return(returnInv));
        return this;
    }

    public BlockBuilder appendReturn(String varName) {
        jcStatements.append(treeMaker.Return(treeMaker.Ident(typeNode.toName(varName))));
        return this;
    }

    public BlockBuilder appendReturn(JCTree.JCExpression expression) {
        jcStatements.append(treeMaker.Return(expression));
        return this;
    }

    public JCTree.JCBlock build() {
        return treeMaker.Block(0, jcStatements.toList());
    }

    public static JCTree.JCNewClass newClass(JavacNode typeNode, Class<?> clazz, JCTree.JCExpression... params) {
        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
        return treeMaker.NewClass(null, List.nil(), genTypeRef(typeNode, clazz.getName()),
                List.from(params), null);
    }

    public static JCTree.JCExpression classRef(JavacNode typeNode, String name) {
        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
        return treeMaker.Select(treeMaker.Ident(typeNode.toName(name)), typeNode.toName("class"));
    }

    public static JCTree.JCExpression varRef(JavacNode typeNode, String name) {
        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
        return treeMaker.Ident(typeNode.toName(name));
    }

    public static JCTree.JCExpression staticMethodInvoke(JavacNode typeNode, Class<?> clazz,
                                                         String methodName, JCTree.JCExpression... params) {
        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
        return treeMaker.Apply(List.nil(), treeMaker.Select(
                genTypeRef(typeNode, clazz.getName()), typeNode.toName(methodName)), List.from(params));
    }

    public static JCTree.JCExpression staticMethodInvoke(JavacNode typeNode, String clazzName,
                                                         String methodName, JCTree.JCExpression... params) {
        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
        return treeMaker.Apply(List.nil(), treeMaker.Select(
                treeMaker.Ident(typeNode.toName(clazzName)), typeNode.toName(methodName)), List.from(params));
    }

    public static JCTree.JCExpression methodInvoke(JavacNode typeNode, String varName,
                                                         String methodName, JCTree.JCExpression... params) {
        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
        return treeMaker.Apply(List.nil(), treeMaker.Select(
                treeMaker.Ident(typeNode.toName(varName)), typeNode.toName(methodName)), List.from(params));
    }
}
