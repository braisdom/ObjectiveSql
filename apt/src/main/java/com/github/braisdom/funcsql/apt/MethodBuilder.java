package com.github.braisdom.funcsql.apt;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;

import java.util.Arrays;

public class MethodBuilder {

    private final TreeMaker treeMaker;
    private final APTHandler handler;
    private final ListBuffer<JCTree.JCVariableDecl> parameters;
    private final ListBuffer<JCTree.JCStatement> statements;

    private JCTree.JCExpression returnType;
    private ListBuffer<JCTree.JCExpression> throwsClauses;

    MethodBuilder(APTHandler handler) {
        this.treeMaker = handler.getTreeMaker();
        this.handler = handler;

        this.parameters = new ListBuffer<>();
        this.statements = new ListBuffer<>();
        this.throwsClauses = new ListBuffer<>();
    }

    public MethodBuilder setThrowsClauses(Class<? extends Throwable>... throwsClauseArray) {
        for (Class<? extends Throwable> throwsClause : throwsClauseArray)
            throwsClauses.append(handler.typeRef(throwsClause.getName()));
        return this;
    }

    public MethodBuilder setReturnType(Class<?> typeClass, Class<?>... genTypeClass) {
        JCTree.JCExpression[] genTypes = Arrays.stream(genTypeClass).map(exceptionClass ->
                treeMaker.Throw(handler.typeRef(exceptionClass.getName())).getExpression())
                .toArray(JCTree.JCExpression[]::new);
        returnType = treeMaker.TypeApply(handler.typeRef(typeClass), List.from(genTypes));
        return this;
    }

    public MethodBuilder setReturnType(Class<?> typeClass, JCTree.JCExpression... genTypeClass) {
        returnType = treeMaker.TypeApply(handler.typeRef(typeClass), List.from(genTypeClass));
        return this;
    }

    public MethodBuilder setReturnType(JCTree.JCExpression type, JCTree.JCExpression... genType) {
        returnType = treeMaker.TypeApply(type, List.from(genType));
        return this;
    }

    public MethodBuilder addStatement(JCTree.JCStatement statement) {
        statements.append(statement);
        return this;
    }

    public MethodBuilder addParameter(String name, JCTree.JCExpression type, JCTree.JCExpression... genTypes) {
        addParameter(name, type, List.from(genTypes));
        return this;
    }

    public MethodBuilder addParameter(String name, JCTree.JCExpression type, List<JCTree.JCExpression> genTypes) {
        if (genTypes.size() > 0) {
            JCTree.JCTypeApply typeApply = treeMaker.TypeApply(type, genTypes);
            addParameter(name, typeApply);
        } else addParameter(name, type);
        return this;
    }

    public MethodBuilder addParameter(String name, Class<?> clazz) {
        addParameter(handler.toName(name), clazz);
        return this;
    }

    public MethodBuilder addParameter(Name name, Class<?> clazz) {
        addParameter(name, handler.typeRef(clazz));
        return this;
    }

    public MethodBuilder addParameter(String name, JCTree.JCExpression type) {
        addParameter(handler.toName(name), type);
        return this;
    }

    public MethodBuilder addParameter(Name name, JCTree.JCExpression type) {
        parameters.add(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), name, type, null));
        return this;
    }

    public MethodBuilder addVarargsParameter(String name, Class typeClass) {
        addVarargsParameter(handler.toName(name), handler.typeRef(typeClass));
        return this;
    }

    public MethodBuilder addVarargsParameter(String name, JCTree.JCExpression type) {
        addVarargsParameter(handler.toName(name), type);
        return this;
    }

    public MethodBuilder addVarargsParameter(Name name, JCTree.JCExpression type) {
        parameters.add(treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER | Flags.VARARGS), name, type, null));
        return this;
    }

    public JCTree.JCMethodDecl build(String name, int modifiers) {
        if (returnType == null)
            returnType = treeMaker.TypeIdent(TypeTag.VOID);

        return treeMaker.MethodDef(treeMaker.Modifiers(modifiers),
                handler.toName(name),
                returnType,
                List.<JCTree.JCTypeParameter>nil(),
                parameters.toList(),
                throwsClauses.toList(),
                treeMaker.Block(0, statements.toList()), null);
    }
}
