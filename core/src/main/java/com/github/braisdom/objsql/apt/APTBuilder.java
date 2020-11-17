package com.github.braisdom.objsql.apt;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import java.util.ArrayList;

public final class APTBuilder {

    private final JCClassDecl classDecl;
    private final Element element;
    private final JCTree ast;
    private final TreeMaker treeMaker;
    private final Names names;
    private final Messager messager;

    public APTBuilder(TreeMaker treeMaker, Names names) {
        this(null, null, null, treeMaker, names, null);
    }

    public APTBuilder(JCClassDecl classDecl, Element element, JCTree ast, TreeMaker treeMaker,
                      Names names, Messager messager) {
        this.classDecl = classDecl;
        this.element = element;
        this.ast = ast;
        this.treeMaker = treeMaker;
        this.names = names;
        this.messager = messager;
    }

    public MethodBuilder createMethodBuilder() {
        return new MethodBuilder(this);
    }

    public StatementBuilder createStatementBuilder() {
        return new StatementBuilder(this);
    }

    public JCTree get() {
        return ast;
    }

    public String getClassName() {
        return classDecl.name.toString();
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

    public JCExpression typeRef(Class clazz) {
        String className = clazz.getName().replace("$", ".");
        return typeRef(className);
    }

    public void inject(JCVariableDecl variableDecl) {
        classDecl.defs = classDecl.defs.append(variableDecl);
    }

    public void inject(JCClassDecl classDecl) {
        this.classDecl.defs = this.classDecl.defs.append(classDecl);
    }

    public void inject(JCMethodDecl methodDecl) {
        if (!Utils.containsMethod(classDecl.sym, methodDecl, false)) {
            classDecl.defs = classDecl.defs.append(methodDecl);
        }
    }

    public void injectForce(JCMethodDecl methodDecl) {
        classDecl.defs = classDecl.defs.append(methodDecl);
    }

    public JCExpression typeRef(String complexName) {
        String[] parts = complexName.split("\\.");
        if (parts.length > 2 && parts[0].equals("java") && parts[1].equals("lang")) {
            String[] subParts = new String[parts.length - 2];
            System.arraycopy(parts, 2, subParts, 0, subParts.length);
            return javaLangTypeRef(subParts);
        }

        return chainDots(parts);
    }

    public JCExpression javaLangTypeRef(String... simpleNames) {
        return chainDots(null, null, simpleNames);
    }

    public JCExpression chainDots(String elem1, String elem2, String... elems) {
        return chainDots(-1, elem1, elem2, elems);
    }

    public JCExpression chainDots(String... elems) {
        assert elems != null;

        JCExpression e = null;
        for (String elem : elems) {
            if (e == null) {
                e = treeMaker.Ident(toName(elem));
            } else {
                e = treeMaker.Select(e, toName(elem));
            }
        }
        return e;
    }

    public JCExpression chainDots(int pos, String elem1, String elem2, String... elems) {
        assert elems != null;
        TreeMaker treeMaker = getTreeMaker();
        if (pos != -1) {
            treeMaker = treeMaker.at(pos);
        }
        JCExpression e = null;
        if (elem1 != null) {
            e = treeMaker.Ident(toName(elem1));
        }
        if (elem2 != null) {
            e = e == null ? treeMaker.Ident(toName(elem2)) : treeMaker.Select(e, toName(elem2));
        }
        for (int i = 0; i < elems.length; i++) {
            e = e == null ? treeMaker.Ident(toName(elems[i])) : treeMaker.Select(e, toName(elems[i]));
        }

        assert e != null;

        return e;
    }

    public JCClassDecl classDef(int modifiers, String name, Class clazz) {
        return treeMaker.ClassDef(treeMaker.Modifiers(modifiers), toName(name), List.nil(),
                typeRef(clazz), List.nil(), List.nil());
    }

    public JCMethodDecl createConstructor(int modifiers, List<JCVariableDecl> parameters,
                                          List<JCStatement> statements) {
        return treeMaker.MethodDef(
                treeMaker.Modifiers(modifiers),
                names.init,
                null,
                List.nil(),
                parameters,
                List.nil(),
                treeMaker.Block(0, statements),
                null);
    }

    public JCExpression staticMethodCall(Class<?> clazz, String methodName, JCExpression... params) {
        return treeMaker.Apply(List.nil(), treeMaker.Select(typeRef(clazz.getName()), toName(methodName)), List.from(params));
    }

    public JCMethodInvocation methodCall(String methodName, JCExpression... params) {
        return treeMaker.Apply(List.nil(), treeMaker.Ident(toName(methodName)), List.from(params));
    }

    public JCMethodInvocation methodCall(String varName, String methodName, JCExpression... params) {
        return treeMaker.Apply(List.nil(), treeMaker.Select(treeMaker.Ident(toName(varName)), toName(methodName)), List.from(params));
    }

    public JCVariableDecl newVar(int modifiers, Class<?> clazz, String name, JCExpression init) {
        return treeMaker.VarDef(treeMaker.Modifiers(modifiers), toName(name), typeRef(clazz), init);
    }

    public JCVariableDecl newVar(Class<?> clazz, String name) {
        return treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), toName(name), typeRef(clazz), null);
    }

    public JCVariableDecl newVar(JCExpression varType, String name) {
        return treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), toName(name), varType, null);
    }

    public JCExpression newGenericsType(Class typeClass, JCExpression... genericTypes) {
        return treeMaker.TypeApply(typeRef(typeClass), List.from(genericTypes));
    }

    public JCExpression newGenericsType(Class typeClass, Class<?>... genericTypeClasses) {
        ListBuffer<JCExpression> genericTypes = new ListBuffer<>();
        for (Class<?> genericTypeClass : genericTypeClasses) {
            genericTypes.append(typeRef(genericTypeClass));
        }
        return treeMaker.TypeApply(typeRef(typeClass), genericTypes.toList());
    }

    public JCExpression newGenericsType(Class typeClass, String classSimpleName) {
        return treeMaker.TypeApply(typeRef(typeClass), List.of(treeMaker.Ident(toName(classSimpleName))));
    }

    public JCExpression newArrayType(String typeName) {
        return treeMaker.TypeArray(treeMaker.Ident(toName(typeName)));
    }

    public JCExpression newArrayType(Class typeClass) {
        return treeMaker.TypeArray(typeRef(typeClass));
    }

    public JCExpression newArrayType(JCExpression type) {
        return treeMaker.TypeArray(type);
    }

    public JCExpression newArray(Class type) {
        return treeMaker.NewArray(typeRef(type), List.of(treeMaker.Literal(0)), null);
    }

    public JCExpression varRef(String name) {
        return treeMaker.Ident(toName(name));
    }

    public JCExpression classRef(String name) {
        return treeMaker.Select(treeMaker.Ident(toName(name)), toName("class"));
    }

    public JCExpression classRef(Class<?> clazz) {
        return treeMaker.Select(typeRef(clazz), toName("class"));
    }

    public static boolean isBoolean(JCExpression varType) {
        return varType != null && varType.toString().equalsIgnoreCase("boolean");
    }

    public boolean isStatic(JCModifiers modifiers) {
        return (modifiers.flags & Flags.STATIC) != 0;
    }


    public JCVariableDecl[] getFields() {
        java.util.List<JCVariableDecl> fields = new ArrayList();
        List<JCTree> members = classDecl.defs;
        for (JCTree member : members) {
            if (member instanceof JCVariableDecl) {
                fields.add((JCVariableDecl) member);
            }
        }

        return fields.toArray(new JCVariableDecl[]{});
    }

    public JCMethodDecl newGetter(JCVariableDecl field) {
        String fieldName = field.name.toString();
        String getterName;
        if (isBoolean(field.vartype)) {
            getterName = Utils.camelize(String.format("%s_%s", "is", fieldName), true);
        } else {
            getterName = Utils.camelize(String.format("%s_%s", "get", fieldName), true);
        }

        JCStatement returnStatement = treeMaker.Return(treeMaker.Select(varRef("this"), toName(fieldName)));

        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC | Flags.FINAL),
                toName(getterName), field.vartype, List.nil(), List.nil(),
                List.nil(), treeMaker.Block(0, List.of(returnStatement)), null);
    }

    public JCMethodDecl newSetter(JCVariableDecl field, boolean returnThis) {
        String fieldName = field.name.toString();
        String setterName = Utils.camelize(String.format("%s_%s", "set", fieldName), true);

        ListBuffer<JCStatement> statements = new ListBuffer<JCStatement>();
        JCExpression fieldRef = treeMaker.Select(varRef("this"), toName(fieldName));
        JCAssign assign = treeMaker.Assign(fieldRef, treeMaker.Ident(field.name));
        JCVariableDecl parameter = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), toName(fieldName), field.vartype, null);
        JCExpression returnType = treeMaker.TypeIdent(TypeTag.VOID);

        statements.append(treeMaker.Exec(assign));
        if (returnThis) {
            returnType = typeRef(classDecl.name.toString());
            statements.append(treeMaker.Return(varRef("this")));
        }

        return treeMaker.MethodDef(treeMaker.Modifiers(Flags.PUBLIC | Flags.FINAL),
                toName(setterName), returnType, List.<JCTypeParameter>nil(), List.of(parameter),
                List.nil(), treeMaker.Block(0, statements.toList()), null);
    }

}
