package com.github.braisdom.funcsql.annotations.generator;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGenerator implements ClassAccessible {

    private final List<ImportItem> importItems;

    public AbstractGenerator() {
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
