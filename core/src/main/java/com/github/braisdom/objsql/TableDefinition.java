package com.github.braisdom.objsql;

public interface TableDefinition {

    default String getTableName() {
        return "";
    }

    default String getSqlFileName() {
        return "";
    }

    default boolean isFluent() {
        return true;
    }

    default Class<?> getPrimaryClass() {
        return Integer.class;
    }

    default String primaryColumnName() {
        return "id";
    }

    default String primaryFieldName() {
        return "id";
    }

    default boolean skipNullValueOnUpdating() {
        return false;
    }

    default boolean allFieldsPersistent() {
        return true;
    }

    default boolean disableGeneratedId() {
        return false;
    }
}
