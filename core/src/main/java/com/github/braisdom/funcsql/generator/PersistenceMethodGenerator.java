package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.*;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.ListBuffer;
import com.sun.tools.javac.util.Names;

import javax.lang.model.element.Element;
import java.sql.SQLException;
import java.util.ArrayList;

public class PersistenceMethodGenerator implements CodeGenerator {

    @Override
    public ImportItem[] getImportItems() {
        java.util.List<ImportItem> importItems = new ArrayList<>();

        importItems.add(new ImportItem(SQLException.class.getPackage().getName(), SQLException.class.getSimpleName()));
        importItems.add(new ImportItem(PersistenceException.class.getPackage().getName(), PersistenceException.class.getSimpleName()));

        return importItems.toArray(new ImportItem[]{});
    }

    @Override
    public JCTree.JCMethodDecl[] generateMethods(TreeMaker treeMaker, Names names, Element element, JCTree.JCClassDecl jcClassDecl) {
        java.util.List<JCTree.JCMethodDecl> methodDecls = new ArrayList<>();

        methodDecls.add(createSaveMethod(treeMaker, names, element));

        return methodDecls.toArray(new JCTree.JCMethodDecl[]{});
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
                treeMaker.Return(
                        treeMaker.TypeCast(
                                treeMaker.Ident(names.fromString(element.getSimpleName().toString())),
                                treeMaker.Apply(
                                        List.nil(),
                                        treeMaker.Select(
                                                treeMaker.Ident(names.fromString("persistence")),
                                                names.fromString("insert")
                                        )
                                        , List.of(
                                                treeMaker.Ident(names.fromString("this")),
                                                treeMaker.Ident(names.fromString("skipValidation"))
                                        )
                                )))
        );

        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC + Flags.FINAL),
                names.fromString("save"),
                treeMaker.Ident(names.fromString(element.getSimpleName().toString())),
                List.nil(),
                List.of(param),
                List.of(
                        treeMaker.Throw(treeMaker.Ident(names.fromString("SQLException"))).getExpression(),
                        treeMaker.Throw(treeMaker.Ident(names.fromString("PersistenceException"))).getExpression()
                        ),
                treeMaker.Block(0, jcStatements.toList()),
                null
        );
    }
}
