package com.github.braisdom.funcsql.generator;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCodeGenerator implements CodeGenerator {

    private final List<ImportItem> importItems;

    public AbstractCodeGenerator() {
        importItems = new ArrayList<>();
    }

    @Override
    public ImportItem[] getImportItems() {
        return importItems.toArray(new ImportItem[]{});
    }

    protected void addImportItem(String packageName, String className) {
        importItems.add(new ImportItem(packageName, className));
    }
}
