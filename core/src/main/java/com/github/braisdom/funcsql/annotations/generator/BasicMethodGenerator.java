package com.github.braisdom.funcsql.annotations.generator;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import javax.lang.model.element.Element;
import java.util.ArrayList;

public class BasicMethodGenerator extends AbstractMethodGenerator {

    @Override
    public ImportItem[] getImportItems() {
        return new ImportItem[0];
    }

    @Override
    public JCTree.JCMethodDecl[] generate(TreeMaker treeMaker, Names names, Element element) {
        java.util.List<JCTree.JCMethodDecl> methodDecls = new ArrayList<>();

        methodDecls.add(createQueryMethod(treeMaker, names, element));

        return methodDecls.toArray(new JCTree.JCMethodDecl[]{});
    }

    private JCTree.JCMethodDecl createQueryMethod(TreeMaker treeMaker, Names names, Element element) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        ListBuffer<JCTree.JCExpression> jcVariableExpressions = new ListBuffer<>();

        jcVariableExpressions.append(
                treeMaker.Select(
                        treeMaker.Ident(names.fromString(element.getSimpleName().toString())),
                        names.fromString("class")
                )
        );

        jcStatements.append(
                treeMaker.Return(treeMaker.NewClass(
                        null,
                        List.nil(), //泛型参数列表
                        treeMaker.Ident(names.fromString("DefaultQuery")), //创建的类名
                        jcVariableExpressions.toList(), //参数列表
                        null //类定义，估计是用于创建匿名内部类
                ))
        );

        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC + Flags.STATIC + Flags.FINAL),
                names.fromString("createQuery"),
                treeMaker.Ident(names.fromString("Query")),
                List.nil(),
                List.nil(),
                List.nil(),
                treeMaker.Block(0, jcStatements.toList()),
                null
        );
    }

}
