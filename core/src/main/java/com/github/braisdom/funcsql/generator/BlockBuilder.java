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

    public BlockBuilder newVar(Class<?> typeClass, String name, Class<?> invokedClass,
                               String staticMethodName, JCTree.JCExpression... params) {
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), typeNode.toName(name),
                genTypeRef(typeNode, typeClass.getName()),
                treeMaker.Apply(List.nil(), treeMaker.Select(
                        genTypeRef(typeNode, invokedClass.getName()), typeNode.toName(staticMethodName)), List.from(params))));
        return this;
    }

    public BlockBuilder newVar(Class<?> typeClass, String name, JCTree.JCExpression init) {
        jcStatements.append(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), typeNode.toName(name),
                genTypeRef(typeNode, typeClass.getName()), init));
        return this;
    }

    public List<JCTree.JCStatement> build() {
        return jcStatements.toList();
    }
}
