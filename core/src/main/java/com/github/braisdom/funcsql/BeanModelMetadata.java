package com.github.braisdom.funcsql;

public class BeanModelMetadata implements DomainModelMetadata {

    private final Class<?> domainModelClass;

    public BeanModelMetadata(Class<?> domainModelClass) {
        this.domainModelClass = domainModelClass;
    }

    @Override
    public String getTableName() {
        return Table.getTableName(domainModelClass);
    }

    @Override
    public String[] getColumnNames() {
        return new String[0];
    }

    @Override
    public Object getValue(Object modelObject, String columnName) {
        return null;
    }

    @Override
    public void setValue(Object modelObject, String columnName, Object columnValue) {

    }

    @Override
    public ColumnTransition getColumnTransition(String columnName) {
        return null;
    }
}
