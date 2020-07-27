package com.github.braisdom.funcsql.generator;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

public class SetterGetterMethodGenerator implements CodeGenerator {

    @Override
    public JCTree.JCMethodDecl[] generateMethods(TreeMaker treeMaker, Names names,
                                                 Element element, JCTree.JCClassDecl jcClassDecl) {
        java.util.List<JCTree.JCMethodDecl> methodDecls = new ArrayList<>();

        JCVariableDecl[] variableDecls = jcClassDecl.defs.stream()
                .filter(d -> d instanceof JCVariableDecl && !((JCVariableDecl)d).getModifiers().getFlags().contains(Modifier.STATIC)
                ).toArray(JCVariableDecl[]::new);

        Arrays.stream(variableDecls).forEach(jcVariableDecl -> {
            methodDecls.add(createGetterMethod(treeMaker, names, jcVariableDecl));
            methodDecls.add(createSetterMethod(treeMaker, names, element, jcVariableDecl));
        });

        return methodDecls.toArray(new JCMethodDecl[]{});
    }

    private JCMethodDecl createGetterMethod(TreeMaker treeMaker, Names names, JCVariableDecl field) {
        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC),
                names.fromString("get" + this.toTitleCase(field.getName().toString())),
                (JCExpression) field.getType(),
                List.nil(),
                List.nil(),
                List.nil(),
                treeMaker.Block(0L, List.of(treeMaker.Return(treeMaker.Select(treeMaker.Ident(names.fromString("this")), names.fromString(field.getName().toString()))))),
                null
        );
    }

    private JCTree.JCMethodDecl createSetterMethod(TreeMaker treeMaker, Names names, Element element, JCVariableDecl field) {
        JCVariableDecl param = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), field.name, field.vartype, null);
        JCTree.JCFieldAccess thisX = treeMaker.Select(treeMaker.Ident(names.fromString("this")), field.name);
        JCAssign assign = treeMaker.Assign(thisX, treeMaker.Ident(field.name));
        ListBuffer<JCStatement> jcStatements = new ListBuffer<>();

        jcStatements.append(treeMaker.Exec(assign));
        jcStatements.append(treeMaker.Return(treeMaker.Ident(names.fromString("this"))));

        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC),
                names.fromString("set" + this.toTitleCase(field.getName().toString())),
                treeMaker.Ident(names.fromString(element.getSimpleName().toString())),
                // 泛型参数
                List.nil(),
                // 方法参数
                List.of(param),
                // throw表达式
                List.nil(),
                treeMaker.Block(0, jcStatements.toList()),
                // 默认值
                null
        );
    }

    public String toTitleCase(String str) {
        char first = str.charAt(0);
        if (first >= 'a' && first <= 'z') {
            first -= 32;
        }
        return first + str.substring(1);
    }
}
