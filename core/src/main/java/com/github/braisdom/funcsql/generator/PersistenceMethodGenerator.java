package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.Persistence;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import javax.lang.model.element.Element;
import java.util.ArrayList;

public class PersistenceMethodGenerator extends AbstractCodeGenerator {

    @Override
    public ImportItem[] getImportItems() {
        java.util.List<ImportItem> importItems = new ArrayList<>();

        importItems.addAll(createPersistenceExceptionImport());

        return importItems.toArray(new ImportItem[]{});
    }

    @Override
    public JCTree.JCMethodDecl[] generateMethods(TreeMaker treeMaker, Names names, Element element, JCTree.JCClassDecl jcClassDecl) {
        java.util.List<JCTree.JCMethodDecl> methodDecls = new ArrayList<>();

        methodDecls.add(createSaveMethod(treeMaker, names, element));
        methodDecls.add(createSaveMethod2(treeMaker, names, element));

        return methodDecls.toArray(new JCTree.JCMethodDecl[]{});
    }

    private JCTree.JCMethodDecl createSaveMethod2(TreeMaker treeMaker, Names names, Element element) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();

        jcStatements.append(
                treeMaker.Exec(treeMaker.Apply(
                        List.nil(),
                        treeMaker.Select(
                                treeMaker.Ident(names.fromString("this")),
                                names.fromString("save")
                        )
                        , List.of(
                                treeMaker.Select(
                                        treeMaker.Ident(names.fromString("Boolean")),
                                        names.fromString("FALSE")
                                )
                        )
                ))
        );

        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC + Flags.FINAL),
                names.fromString("save"),
                treeMaker.Type(new Type.JCVoidType()),
                List.nil(),
                List.nil(),
                List.of(
                        treeMaker.Throw(treeMaker.Ident(names.fromString("SQLException"))).getExpression(),
                        treeMaker.Throw(treeMaker.Ident(names.fromString("PersistenceException"))).getExpression()
                ),
                treeMaker.Block(0, jcStatements.toList()),
                null
        );
    }

    private JCTree.JCMethodDecl createSaveMethod(TreeMaker treeMaker, Names names, Element element) {
        ListBuffer<JCTree.JCStatement> jcStatements = new ListBuffer<>();
        JCTree.JCVariableDecl param = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER),
                names.fromString("skipValidation"), treeMaker.Ident(names.fromString("Boolean")), null);

        jcStatements.append(
                treeMaker.VarDef(
                        treeMaker.Modifiers(Flags.PARAMETER),
                        names.fromString("persistence"),
                        treeMaker.Ident(names.fromString(Persistence.class.getSimpleName())),
                        treeMaker.Apply(
                                List.nil(),
                                treeMaker.Select(
                                        treeMaker.Ident(names.fromString("this")),
                                        names.fromString("createPersistence")
                                ), List.nil()
                        )
                )
        );

        jcStatements.append(
                treeMaker.Exec(
                        treeMaker.Apply(
                                List.nil(),
                                treeMaker.Select(
                                        treeMaker.Ident(names.fromString("persistence")),
                                        names.fromString("save")
                                )
                                , List.of(
                                        treeMaker.Ident(names.fromString("this")),
                                        treeMaker.Ident(names.fromString("skipValidation"))
                                )
                        ))
        );

        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC + Flags.FINAL),
                names.fromString("save"),
                treeMaker.Type(new Type.JCVoidType()),
                List.nil(),
                List.of(param),
                createPersistenceException(treeMaker, names),
                treeMaker.Block(0, jcStatements.toList()),
                null
        );
    }
}
