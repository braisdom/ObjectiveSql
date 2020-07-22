package com.github.braisdom.funcsql.annotations.generator;

public interface ClassImportable {

    class ImportItem {

        private String packageName;
        private String className;

        public ImportItem(String packageName, String className) {
            this.packageName = packageName;
            this.className = className;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getClassName() {
            return className;
        }
    }

    ImportItem[] getImportItems();
}
