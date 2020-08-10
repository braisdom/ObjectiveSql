package com.github.braisdom.funcsql.generator;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
//import lombok.javac.APTUtils;
//import lombok.javac.JavacTreeMaker;
//
//import static lombok.javac.handlers.JavacHandlerUtil.genTypeRef;

class BlockBuilder {

//    private final JavacTreeMaker treeMaker;
//    private final APTUtils typeNode;
//    private final ListBuffer<JCTree.JCStatement> jcStatements;
//
//    private StatementBuilder(JavacTreeMaker treeMaker, APTUtils typeNode){
//        this.treeMaker = treeMaker;
//        this.typeNode = typeNode;
//        this.jcStatements = new ListBuffer<>();
//    }
//
//    public static StatementBuilder newBlock(JavacTreeMaker treeMaker, APTUtils typeNode) {
//        return new StatementBuilder(treeMaker, typeNode);
//    }
//
//    public StatementBuilder inject(JCTree.JCStatement... statement) {
//        jcStatements.inject(treeMaker.Block(0, List.from(statement)));
//        return this;
//    }
//
//    public StatementBuilder appendVar(Class<?> typeClass, String name, String invokedClassName,
//                               String staticMethodName, JCTree.JCExpression... params) {
//        jcStatements.inject(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), typeNode.toName(name),
//                genTypeRef(typeNode, typeClass.getName()),
//                treeMaker.Apply(List.nil(), treeMaker.Select(
//                        treeMaker.Ident(typeNode.toName(invokedClassName)), typeNode.toName(staticMethodName)), List.from(params))));
//        return this;
//    }
//
//    public StatementBuilder appendVar(Class<?> typeClass, String name, Class<?> invokedClass,
//                               String staticMethodName, JCTree.JCExpression... params) {
//        jcStatements.inject(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), typeNode.toName(name),
//                genTypeRef(typeNode, typeClass.getName()),
//                treeMaker.Apply(List.nil(), treeMaker.Select(
//                        genTypeRef(typeNode, invokedClass.getName()), typeNode.toName(staticMethodName)), List.from(params))));
//        return this;
//    }
//
//    public StatementBuilder appendVar(Class<?> typeClass, String name, JCTree.JCExpression init) {
//        jcStatements.inject(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), typeNode.toName(name),
//                genTypeRef(typeNode, typeClass.getName()), init));
//        return this;
//    }
//
//    public StatementBuilder appendVar(JCTree.JCTypeApply typeApply, String name, JCTree.JCExpression init) {
//        jcStatements.inject(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), typeNode.toName(name), typeApply, init));
//        return this;
//    }
//
//    public StatementBuilder appendStaticMethodInvoke(Class<?> typeClass, String name, JCTree.JCExpression... params) {
//        JCTree.JCExpression methodRef = treeMaker.Select(genTypeRef(typeNode, typeClass.getName()),
//                typeNode.toName(name));
//        jcStatements.inject(treeMaker.Exec(treeMaker.Apply(List.nil(),
//                methodRef, List.from(params))));
//        return this;
//    }
//
//    public StatementBuilder appendStaticMethodInvoke(String varName, String name, JCTree.JCExpression... params) {
//        JCTree.JCExpression methodRef = treeMaker.Select(treeMaker.Ident(typeNode.toName(varName)),
//                typeNode.toName(name));
//        jcStatements.inject(treeMaker.Exec(treeMaker.Apply(List.nil(),
//                methodRef, List.from(params))));
//        return this;
//    }
//
//    public StatementBuilder appendInstanceMethodInvoke(String name, JCTree.JCExpression... params) {
//        jcStatements.inject(treeMaker.Exec(treeMaker.Apply(List.nil(),
//                treeMaker.Ident(typeNode.toName(name)), List.from(params))));
//        return this;
//    }
//
//    public StatementBuilder appendVar(String typeClassName, String name, JCTree.JCExpression init) {
//        jcStatements.inject(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), typeNode.toName(name),
//                treeMaker.Ident(typeNode.toName(typeClassName)), init));
//        return this;
//    }
//
//    public StatementBuilder appendReturn(String varName, String varMethodName, JCTree.JCExpression... params) {
//        JCTree.JCExpression invokeRef = treeMaker.Select(treeMaker.Ident(typeNode.toName(varName)), typeNode.toName(varMethodName));
//        JCTree.JCMethodInvocation returnInv = treeMaker.Apply(List.nil(), invokeRef, List.from(params));
//
//        jcStatements.inject(treeMaker.Return(returnInv));
//        return this;
//    }
//
//    public StatementBuilder appendReturn(Class<?> clazz, String methodName, JCTree.JCExpression... params) {
//        JCTree.JCExpression invokeRef = treeMaker.Select(genTypeRef(typeNode, clazz.getName()), typeNode.toName(methodName));
//        JCTree.JCMethodInvocation returnInv = treeMaker.Apply(List.nil(), invokeRef, List.from(params));
//
//        jcStatements.inject(treeMaker.Return(returnInv));
//        return this;
//    }
//
//    public StatementBuilder appendReturn(String varName) {
//        jcStatements.inject(treeMaker.Return(treeMaker.Ident(typeNode.toName(varName))));
//        return this;
//    }
//
//    public StatementBuilder appendReturn(JCTree.JCExpression expression) {
//        jcStatements.inject(treeMaker.Return(expression));
//        return this;
//    }
//
//    public JCTree.JCBlock build() {
//        return treeMaker.Block(0, jcStatements.toList());
//    }
//
//    public static JCTree.JCNewClass newClass(APTUtils typeNode, Class<?> clazz, JCTree.JCExpression... params) {
//        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
//        return treeMaker.NewClass(null, List.nil(), genTypeRef(typeNode, clazz.getName()),
//                List.from(params), null);
//    }
//
//    public static JCTree.JCExpression classRef(APTUtils typeNode, String name) {
//        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
//        return treeMaker.Select(treeMaker.Ident(typeNode.toName(name)), typeNode.toName("class"));
//    }
//
//    public static JCTree.JCExpression staticMethodInvoke(APTUtils typeNode, Class<?> clazz,
//                                                         String methodName, JCTree.JCExpression... params) {
//        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
//        return treeMaker.Apply(List.nil(), treeMaker.Select(
//                genTypeRef(typeNode, clazz.getName()), typeNode.toName(methodName)), List.from(params));
//    }
//
//    public static JCTree.JCExpression staticMethodInvoke(APTUtils typeNode, String clazzName,
//                                                         String methodName, JCTree.JCExpression... params) {
//        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
//        return treeMaker.Apply(List.nil(), treeMaker.Select(
//                treeMaker.Ident(typeNode.toName(clazzName)), typeNode.toName(methodName)), List.from(params));
//    }
//
//    public static JCTree.JCExpression methodInvoke(APTUtils typeNode, String varName,
//                                                         String methodName, JCTree.JCExpression... params) {
//        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
//        return treeMaker.Apply(List.nil(), treeMaker.Select(
//                treeMaker.Ident(typeNode.toName(varName)), typeNode.toName(methodName)), List.from(params));
//    }
//
//    public static JCTree.JCExpression varRef(APTUtils typeNode, String name) {
//        JavacTreeMaker treeMaker = typeNode.getTreeMaker();
//        return treeMaker.Ident(typeNode.toName(name));
//    }
}
