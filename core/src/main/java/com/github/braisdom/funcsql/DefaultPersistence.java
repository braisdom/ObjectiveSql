package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.Column;
import com.github.braisdom.funcsql.reflection.ClassUtils;
import com.github.braisdom.funcsql.reflection.PropertyUtils;
import com.github.braisdom.funcsql.util.ArrayUtil;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class DefaultPersistence<T> extends AbstractPersistence<T> {

    public DefaultPersistence(Class<T> domainModelClass) {
        super(domainModelClass);
    }

    @Override
    public void save(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException {
        Object primaryValue = requirePrimaryKey(dirtyObject);
        if (primaryValue == null)
            insert(dirtyObject, skipValidation);
        else update(dirtyObject);
    }

    @Override
    public T insert(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        SQLExecutor<T> sqlExecutor = Database.getSqlExecutor();
        Connection connection = connectionFactory.getConnection();

        try {
            Field[] fields = getInsertableFields(dirtyObject.getClass());
            Map<String, ColumnTransition<T>> columnTransitionMap = instantiateColumnTransitionMap(fields);

            String[] columnNames = Arrays.stream(fields).map(f -> f.getName()).toArray(String[]::new);
            String tableName = Table.getTableName(domainModelClass);
            String sql = formatInsertSql(tableName, columnNames);

            Object[] values = Arrays.stream(fields)
                    .map(field -> {
                        ColumnTransition<T> columnTransition = columnTransitionMap.get(field.getName());
                        if (columnTransition != null) {
                            return columnTransition.sinking(dirtyObject, field, PropertyUtils.readDirectly(dirtyObject, field));
                        } else return PropertyUtils.readDirectly(dirtyObject, field);
                    })
                    .toArray(Object[]::new);

            return sqlExecutor.insert(connection, sql, domainModelClass, values);
        } finally {
            if (connection != null)
                connection.close();
        }
    }

    @Override
    public int insert(T[] dirtyObject) throws SQLException, PersistenceException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        Connection connection = connectionFactory.getConnection();
        SQLExecutor<T> sqlExecutor = Database.getSqlExecutor();

        try {
            Field[] fields = getInsertableFields(dirtyObject.getClass());
            Map<String, ColumnTransition<T>> columnTransitionMap = instantiateColumnTransitionMap(fields);

            Object[][] values = new Object[dirtyObject.length][fields.length];
            String[] columnNames = Arrays.stream(fields).map(f -> f.getName()).toArray(String[]::new);

            for (int i = 0; i < dirtyObject.length; i++) {
                for (int t = 0; t < fields.length; t++) {
                    ColumnTransition<T> columnTransition = columnTransitionMap.get(fields[t].getName());
                    if (columnTransition != null)
                        values[i][t] = columnTransition.sinking(dirtyObject[i], fields[t],
                                PropertyUtils.readDirectly(dirtyObject[i], fields[t]));
                    else
                        values[i][t] = PropertyUtils.readDirectly(dirtyObject[i], fields[t]);
                }
            }

            String tableName = Table.getTableName(domainModelClass);
            String sql = formatInsertSql(tableName, columnNames);

            return sqlExecutor.insert(connection, sql, domainModelClass, values);
        } finally {
            if (connection != null)
                connection.close();
        }
    }

    @Override
    public int update(T dirtyObject) throws SQLException, PersistenceException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        Connection connection = connectionFactory.getConnection();
        SQLExecutor<T> sqlExecutor = Database.getSqlExecutor();

        Field primaryField = Table.getPrimaryField(domainModelClass);
        Object primaryValue = requirePrimaryKey(dirtyObject);

        Field[] fields = getUpdatableFields(domainModelClass);
        Map<String, ColumnTransition<T>> columnTransitionMap = instantiateColumnTransitionMap(fields);

        Object[] values = Arrays.stream(fields)
                .map(field -> {
                    ColumnTransition<T> columnTransition = columnTransitionMap.get(field.getName());
                    if (columnTransition != null)
                        return columnTransition.sinking(dirtyObject, field, PropertyUtils.readDirectly(dirtyObject, field));
                    else return PropertyUtils.readDirectly(dirtyObject, field);
                })
                .toArray(Object[]::new);

        StringBuilder updatesSql = new StringBuilder();

        Arrays.stream(fields).forEach(field -> {
            updatesSql.append(field.getName()).append("=").append("?").append(",");
        });

        updatesSql.delete(updatesSql.length() - 1, updatesSql.length());
        String sql = formatUpdateSql(Table.getTableName(domainModelClass),
                updatesSql.toString(), String.format("%s = ?", primaryField.getName()));
        return sqlExecutor.update(connection, sql,
                ArrayUtil.appendElement(Object.class, values, primaryValue));
    }

    @Override
    public int delete(T dirtyObject) throws SQLException, PersistenceException {
        return 0;
    }

    protected Object requirePrimaryKey(T object) throws PersistenceException {
        Field primary = getPrimaryField(domainModelClass);
        return PropertyUtils.readDirectly(object, primary);
    }

    private Map<String, ColumnTransition<T>> instantiateColumnTransitionMap(Field[] fields) {
        Map<String, ColumnTransition<T>> columnTransitionMap = new HashMap<>();

        Arrays.stream(fields).forEach(field -> {
            Column column = field.getAnnotation(Column.class);
            if (column != null && !column.transition().equals(ColumnTransition.class))
                columnTransitionMap.put(field.getName(), ClassUtils.createNewInstance(column.transition()));
        });

        return columnTransitionMap;
    }
}
