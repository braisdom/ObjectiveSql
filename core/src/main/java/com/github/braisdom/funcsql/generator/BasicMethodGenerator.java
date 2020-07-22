package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.DefaultQuery;
import com.github.braisdom.funcsql.Query;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import javax.lang.model.element.Element;
import java.util.ArrayList;

public class BasicMethodGenerator extends AbstractMethodGenerator {

    private static final String METHOD_NAME_CREATE_QUERY = "createQuery";
    private static final String METHOD_NAME_CREATE_PERSISTENCE = "createPersistence";

    @Override
    public ImportItem[] getImportItems() {

        addImportItem(DefaultQuery.class.getPackage().getName(), DefaultQuery.class.getSimpleName());
        addImportItem(DefaultQuery.class.getPackage().getName(), Query.class.getSimpleName());

        return super.getImportItems();
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
                        treeMaker.Ident(names.fromString(DefaultQuery.class.getSimpleName())), //创建的类名
                        jcVariableExpressions.toList(), //参数列表
                        null //类定义，估计是用于创建匿名内部类
                ))
        );

        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC + Flags.STATIC + Flags.FINAL),
                names.fromString(METHOD_NAME_CREATE_QUERY),
                treeMaker.Ident(names.fromString(Query.class.getSimpleName())),
                List.nil(),
                List.nil(),
                List.nil(),
                treeMaker.Block(0, jcStatements.toList()),
                null
        );
    }

}
