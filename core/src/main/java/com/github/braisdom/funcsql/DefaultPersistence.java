package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.reflection.PropertyUtils;
import com.github.braisdom.funcsql.transition.ColumnTransitional;
import com.github.braisdom.funcsql.util.ArrayUtil;
import com.github.braisdom.funcsql.util.FunctionWithThrowable;
import com.github.braisdom.funcsql.util.StringUtil;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

public class DefaultPersistence<T> extends AbstractPersistence<T> {

    private static Logger logger = Database.getLoggerFactory().create(DefaultPersistence.class);

    public DefaultPersistence(Class<T> domainClass) {
        super(domainClass);
    }

    public DefaultPersistence(DomainModelDescriptor domainModelDescriptor) {
        super(domainModelDescriptor);
    }

    @Override
    public void save(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException {
        Objects.requireNonNull(dirtyObject, "The dirtyObject cannot be null");

        Object primaryValue = domainModelDescriptor.getPrimaryValue(dirtyObject);
        if (primaryValue == null)
            insert(dirtyObject, skipValidation);
        else update(primaryValue, dirtyObject, skipValidation);
    }

    @Override
    public T insert(T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException {
        Objects.requireNonNull(dirtyObject, "The dirtyObject cannot be null");

        return Database.execute((connection, sqlExecutor) -> {
            String[] columnNames = domainModelDescriptor.getInsertableColumns();
            String tableName = domainModelDescriptor.getTableName();
            String sql = formatInsertSql(tableName, columnNames);

            Object[] values = Arrays.stream(columnNames)
                    .map(FunctionWithThrowable.castFunctionWithThrowable(columnName -> {
                        String fieldName = domainModelDescriptor.getFieldName(columnName);
                        ColumnTransitional<T> columnTransitional = domainModelDescriptor.getColumnTransition(fieldName);
                        if (columnTransitional != null) {
                            return columnTransitional.sinking(
                                    connection.getMetaData(),
                                    dirtyObject,
                                    domainModelDescriptor,
                                    fieldName,
                                    PropertyUtils.readDirectly(dirtyObject, fieldName));
                        } else return PropertyUtils.readDirectly(dirtyObject, fieldName);
                    })).toArray(Object[]::new);

            return (T) Database.sqlBenchmarking(() ->
                    sqlExecutor.insert(connection, sql, domainModelDescriptor, values), logger, sql, values);
        });
    }

    @Override
    public int[] insert(T[] dirtyObject, boolean skipValidation) throws SQLException, PersistenceException {
        Objects.requireNonNull(dirtyObject, "The dirtyObject cannot be null");

        return Database.execute((connection, sqlExecutor) -> {
            String[] columnNames = domainModelDescriptor.getInsertableColumns();
            Object[][] values = new Object[dirtyObject.length][columnNames.length];

            for (int i = 0; i < dirtyObject.length; i++) {
                for (int t = 0; t < columnNames.length; t++) {
                    ColumnTransitional<T> columnTransitional = domainModelDescriptor.getColumnTransition(columnNames[t]);
                    String fieldName = domainModelDescriptor.getFieldName(columnNames[t]);
                    if (columnTransitional != null)
                        values[i][t] = columnTransitional.sinking(connection.getMetaData(),
                                dirtyObject[i], domainModelDescriptor, fieldName,
                                PropertyUtils.readDirectly(dirtyObject[i], fieldName));
                    else
                        values[i][t] = PropertyUtils.readDirectly(dirtyObject[i], fieldName);
                }
            }

            String tableName = domainModelDescriptor.getTableName();
            String sql = formatInsertSql(tableName, columnNames);

            return Database.sqlBenchmarking(() ->
                    sqlExecutor.insert(connection, sql, domainModelDescriptor, values), logger, sql, values);
        });
    }

    @Override
    public int update(Object id, T dirtyObject, boolean skipValidation) throws SQLException, PersistenceException {
        Objects.requireNonNull(id, "The id cannot be null");
        Objects.requireNonNull(dirtyObject, "The dirtyObject cannot be null");

        PrimaryKey primaryKey = domainModelDescriptor.getPrimaryKey();
        ensurePrimaryKeyNotNull(primaryKey);

        return Database.execute((connection, sqlExecutor) -> {
            String[] rawColumnNames = domainModelDescriptor.getUpdatableColumns();
            String[] columnNames = Arrays.stream(rawColumnNames)
                    .filter(rawColumnName -> {
                        if (domainModelDescriptor.skipNullOnUpdate()) {
                            String fieldName = domainModelDescriptor.getFieldName(rawColumnName);
                            return PropertyUtils.readDirectly(dirtyObject, fieldName) != null;
                        } else return true;
                    }).toArray(String[]::new);
            Object[] values = Arrays.stream(columnNames)
                    .map(FunctionWithThrowable.castFunctionWithThrowable(columnName -> {
                        String fieldName = domainModelDescriptor.getFieldName(columnName);
                        ColumnTransitional<T> columnTransitional = domainModelDescriptor.getColumnTransition(fieldName);
                        if (columnTransitional != null)
                            return columnTransitional.sinking(connection.getMetaData(), dirtyObject, domainModelDescriptor,
                                    fieldName, PropertyUtils.readDirectly(dirtyObject, fieldName));
                        else return PropertyUtils.readDirectly(dirtyObject, fieldName);
                    })).toArray(Object[]::new);

            StringBuilder updatesSql = new StringBuilder();
            Arrays.stream(columnNames).forEach(columnName ->
                    updatesSql.append(columnName).append("=").append("?").append(","));

            ensureNotBlank(updatesSql.toString(), "updates");
            updatesSql.delete(updatesSql.length() - 1, updatesSql.length());

            String sql = formatUpdateSql(domainModelDescriptor.getTableName(),
                    updatesSql.toString(), String.format("%s = ?", primaryKey.name()));

            return Database.sqlBenchmarking(() ->
                    sqlExecutor.update(connection, sql, ArrayUtil.appendElement(Object.class, values, id)), logger, sql, values);
        });
    }

    @Override
    public int update(String updates, String predication) throws SQLException, PersistenceException {
        Objects.requireNonNull(updates, "The updates cannot be null");
        Objects.requireNonNull(predication, "The predication cannot be null");

        ensureNotBlank(updates, "updates");
        ensureNotBlank(updates, "predication");

        return Database.execute((connection, sqlExecutor) -> {
            String sql = formatUpdateSql(domainModelDescriptor.getTableName(), updates, predication);
            return Database.sqlBenchmarking(() ->
                    sqlExecutor.execute(connection, sql), logger, sql);
        });
    }

    @Override
    public int delete(String predication) throws SQLException, PersistenceException {
        Objects.requireNonNull(predication, "The criteria cannot be null");
        ensureNotBlank(predication, "predication");

        return Database.execute((connection, sqlExecutor) -> {
            String sql = formatDeleteSql(domainModelDescriptor.getTableName(), predication);
            return Database.sqlBenchmarking(() ->
                    sqlExecutor.execute(connection, sql), logger, sql, predication);
        });
    }

    @Override
    public int delete(Object id) throws SQLException, PersistenceException {
        Objects.requireNonNull(id, "The id cannot be null");

        PrimaryKey primaryKey = domainModelDescriptor.getPrimaryKey();
        ensurePrimaryKeyNotNull(primaryKey);

        return Database.execute((connection, sqlExecutor) -> {
            Quoter quoter = Database.getQuoter();
            String sql = formatDeleteSql(domainModelDescriptor.getTableName(),
                    String.format("%s = %s", quoter.quoteColumn(primaryKey.name()), quoter.quoteValue(id)));
            return Database.sqlBenchmarking(() ->
                    sqlExecutor.execute(connection, sql), logger, sql, id);
        });
    }

    @Override
    public int execute(String sql) throws SQLException, PersistenceException {
        Objects.requireNonNull(sql, "The sql cannot be null");

        return Database.execute((connection, sqlExecutor) ->
                Database.sqlBenchmarking(() ->
                sqlExecutor.execute(connection, sql), logger, sql));
    }

    private void ensurePrimaryKeyNotNull(PrimaryKey primaryKey) throws PersistenceException {
        if (primaryKey == null)
            throw new PersistenceException(String.format("The %s has no primary key", domainModelDescriptor.getTableName()));
    }

    private void ensureNotBlank(String string, String name) throws PersistenceException {
        if (StringUtil.isBlank(string))
            throw new PersistenceException(String.format("Empty %s for %s ", name, domainModelDescriptor.getTableName()));
    }
}
