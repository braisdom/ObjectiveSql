package com.github.braisdom.funcsql.generator;

import com.github.braisdom.funcsql.PersistenceException;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Names;

import java.sql.SQLException;
import java.util.ArrayList;

public abstract class AbstractCodeGenerator implements CodeGenerator {

    protected java.util.List<ImportItem> createPersistenceExceptionImport() {
        java.util.List<ImportItem> importItems = new ArrayList<>();

        importItems.add(new ImportItem(SQLException.class.getPackage().getName(), SQLException.class.getSimpleName()));
        importItems.add(new ImportItem(PersistenceException.class.getPackage().getName(), PersistenceException.class.getSimpleName()));

        return importItems;

    }

    protected List createPersistenceException(TreeMaker treeMaker, Names names) {
        return List.of(
                treeMaker.Throw(treeMaker.Ident(names.fromString("SQLException"))).getExpression(),
                treeMaker.Throw(treeMaker.Ident(names.fromString("PersistenceException"))).getExpression()
        );
    }
}
