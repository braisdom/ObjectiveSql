package com.github.braisdom.funcsql.apt;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;

import java.util.ArrayList;
import java.util.Arrays;

public class MethodBuilder {

    private final TreeMaker treeMaker;
    private final APTHandler node;
    private final java.util.List<JCTree.JCVariableDecl> parameters;

    private JCTree.JCExpression returnType;
    private List<JCTree.JCExpression> throwsClauses;

    MethodBuilder(APTHandler handler) {
        this.treeMaker = handler.getTreeMaker();
        this.node = handler;

        this.parameters = new ArrayList<>();
    }

    public MethodBuilder setExceptions(Class<? extends Throwable>... exceptionClasses) {
        JCTree.JCExpression[] exceptionExpressions = Arrays.stream(exceptionClasses).map(exceptionClass ->
                treeMaker.Throw(node.typeRef(exceptionClass.getName())).getExpression())
                .toArray(JCTree.JCExpression[]::new);
        throwsClauses = List.from(exceptionExpressions);
        return this;
    }

    public MethodBuilder setReturnType(Class<?> typeClass, Class<?>... genTypeClass) {
        JCTree.JCExpression[] genTypes = Arrays.stream(genTypeClass).map(exceptionClass ->
                treeMaker.Throw(node.typeRef(exceptionClass.getName())).getExpression())
                .toArray(JCTree.JCExpression[]::new);
        returnType = treeMaker.TypeApply(node.typeRef(typeClass), List.from(genTypes));
        return this;
    }

    public MethodBuilder setReturnType(Class<?> typeClass, JCTree.JCExpression... genTypeClass) {
        returnType = treeMaker.TypeApply(node.typeRef(typeClass), List.from(genTypeClass));
        return this;
    }

    public MethodBuilder setReturnType(JCTree.JCExpression type, JCTree.JCExpression... genType) {
        returnType = treeMaker.TypeApply(type, List.from(genType));
        return this;
    }

    public MethodBuilder createParameter(String name, JCTree.JCExpression type, JCTree.JCExpression init,
                                         JCTree.JCExpression... genType) {
        treeMaker.TypeApply(type, List.from(genType));
        parameters.add(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), node.toName(name), type, init));
        return this;
    }

    public MethodBuilder createParameter(String name, JCTree.JCExpression type, JCTree.JCExpression init) {
        parameters.add(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), node.toName(name), type, init));
        return this;
    }
}
