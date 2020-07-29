package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import javax.lang.model.element.Element;
import java.util.ArrayList;

public class PrimaryFieldGenerator extends AbstractCodeGenerator {

    @Override
    public ImportItem[] getImportItems() {
        java.util.List<ImportItem> importItems = new ArrayList<>();

        importItems.add(new ImportItem(PrimaryKey.class.getPackage().getName(), PrimaryKey.class.getSimpleName()));

        return importItems.toArray(new ImportItem[]{});
    }

    @Override
    public JCTree.JCVariableDecl[] generateVariables(TreeMaker treeMaker, Names names,
                                                     Element element, JCTree.JCClassDecl jcClassDecl) {
        java.util.List<JCTree.JCVariableDecl> variableDecls = new ArrayList<>();
        DomainModel domainModel = element.getAnnotation(DomainModel.class);

        JCTree.JCVariableDecl primaryVariable = treeMaker.VarDef(treeMaker.Modifiers(Flags.PRIVATE),
                names.fromString(domainModel.primaryFieldName()),
                treeMaker.Ident(names.fromString(domainModel.primaryClass().getSimpleName())), null);
        primaryVariable.mods.annotations = primaryVariable.mods.annotations.append(
                treeMaker.Annotation(
                        treeMaker.Ident(names.fromString("PrimaryKey")),
                        List.of(treeMaker.Assign(treeMaker.Ident(names.fromString("version")),
                                treeMaker.Literal(domainModel.primaryColumnName())))
                )
        );

        variableDecls.add(primaryVariable);

        return variableDecls.toArray(new JCTree.JCVariableDecl[]{});
    }
}
