package com.github.braisdom.funcsql;

import java.util.Map;
import java.util.Objects;

public class DefaultRow implements Row {

    private final Map<String, Object> rawRow;

    public DefaultRow(Map<String, Object> rawRow) {
        Objects.requireNonNull(rawRow, "The rawRow cannot be null");
        this.rawRow = rawRow;
    }

    @Override
    public String[] getColumns() {
        return rawRow.keySet().toArray(new String[]{});
    }

    @Override
    public int getColumnCount() {
        return rawRow.keySet().size();
    }

    @Override
    public Object getValue(String columnName) {
        return rawRow.get(columnName);
    }

    @Override
    public Boolean getBoolean(String columnName) {
        return null;
    }

    @Override
    public Long getLong(String columnName) {
        return (Long) rawRow.get(columnName);
    }

    @Override
    public Integer getInteger(String columnName) {
        return (Integer) rawRow.get(columnName);
    }

    @Override
    public String getString(String columnName) {
        return (String) rawRow.get(columnName);
    }

    @Override
    public Float getFloat(String columnName) {
        return (Float) rawRow.get(columnName);
    }

    @Override
    public Double getDouble(String columnName) {
        return (Double) rawRow.get(columnName);
    }
}
