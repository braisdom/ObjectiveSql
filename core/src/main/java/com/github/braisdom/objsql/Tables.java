/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.braisdom.objsql;

import com.github.braisdom.objsql.annotations.Column;
import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.PrimaryKey;
import com.github.braisdom.objsql.reflection.PropertyUtils;
import com.github.braisdom.objsql.util.Inflector;
import com.github.braisdom.objsql.util.StringUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.*;

/**
 * Utility methods relates to the database table.
 *
 * @author braisdom
 * @since 1.0
 */
public final class Tables {

    public static final String INVALID_PRIMARY_KEY = "invalid~id";
    public static final String DEFAULT_KEY_SUFFIX = "id";

    private static final Map<String, Field> primaryKeyFieldCache = new HashMap<>();

    private static Validator validator = bean -> {
        javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Object>> rawViolations = validator.validate(bean);

        return rawViolations.stream().map(violation ->
                new Validator.Violation(violation.getRootBeanClass(), violation.getMessage(), violation.getInvalidValue(),
                        violation.getPropertyPath().toString()))
                .toArray(Validator.Violation[]::new);
    };

    private static TableNameEncoder tableNameEncoder = new TableNameEncoder() {
        @Override
        public String getTableName(Class domainModelClass) {
            Objects.requireNonNull(domainModelClass, "The baseClass cannot be null");
            DomainModel domainModel = (DomainModel) domainModelClass.getAnnotation(DomainModel.class);
            Objects.requireNonNull(domainModel, "The baseClass must have the DomainModel annotation");

            if (!StringUtil.isBlank(domainModel.tableName())) {
                return domainModel.tableName();
            } else {
                return Inflector.getInstance().tableize(domainModelClass.getSimpleName());
            }
        }

        @Override
        public String getColumnName(Class domainModelClass, String fieldName) {
            return Inflector.getInstance().underscore(fieldName);
        }
    };

    public static final String getTableName(Class domainModelClass) {
        return tableNameEncoder.getTableName(domainModelClass);
    }

    public static final PrimaryKey getPrimaryKey(Class tableClass) {
        Field[] fields = tableClass.getDeclaredFields();
        for (Field field : fields) {
            PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
            if (primaryKey != null) {
                return primaryKey;
            }
        }
        return null;
    }

    public static Object getPrimaryValue(Object domainObject) {
        String primaryKeyFieldName = getPrimaryKeyFieldName(domainObject.getClass());
        return PropertyUtils.read(domainObject, primaryKeyFieldName);
    }

    public static String getPrimaryKeyColumnName(Class tableClass) {
        Field primaryKeyField = primaryKeyFieldCache.get(tableClass.getName());
        if (primaryKeyField == null) {
            Field[] fields = tableClass.getDeclaredFields();
            for (Field field : fields) {
                PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
                if (primaryKey != null) {
                    primaryKeyFieldCache.put(tableClass.getName(), field);
                    if (INVALID_PRIMARY_KEY.equals(primaryKey.name())) {
                        return tableNameEncoder.getColumnName(tableClass, field.getName());
                    } else {
                        return primaryKey.name();
                    }
                }
            }
            throw new IllegalStateException(String.format("Class '%s' has no @PrimaryKey", tableClass.getSimpleName()));
        } else {
            return tableNameEncoder.getColumnName(tableClass, primaryKeyField.getName());
        }
    }

    public static String getPrimaryKeyFieldName(Class tableClass) {
        Field[] fields = tableClass.getDeclaredFields();
        for (Field field : fields) {
            PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
            if (primaryKey != null) {
                return field.getName();
            }
        }
        throw new IllegalStateException(String.format("Class '%s' has no @PrimaryKey", tableClass.getSimpleName()));
    }

    public static void writePrimaryValue(Object domainObject, Object primaryValue) {
        String primaryKeyFieldName = getPrimaryKeyFieldName(domainObject.getClass());
        PropertyUtils.write(domainObject, primaryKeyFieldName, primaryValue);
    }

    public static final Field getPrimaryField(Class tableClass) {
        Field[] fields = tableClass.getDeclaredFields();
        for (Field field : fields) {
            PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
            if (primaryKey != null) {
                return field;
            }
        }
        throw new IllegalStateException(String.format("Class '%s' has no @PrimaryKey", tableClass.getSimpleName()));
    }

    public static final String getColumnName(Class tableClass, String fieldName) {
        try {
            Field field = tableClass.getDeclaredField(fieldName);
            Column column = field.getDeclaredAnnotation(Column.class);

            if (column != null) {
                if (!StringUtil.isEmpty(column.name())) {
                    return column.name();
                }
            }

            return tableNameEncoder.getColumnName(tableClass, field.getName());
        } catch (NoSuchFieldException ex) {
            throw new IllegalStateException(ex.getMessage(), ex);
        }
    }

    public static Validator getValidator() {
        return validator;
    }

    public static final void installValidator(Validator validator) {
        Objects.requireNonNull(validator, "The validator cannot be null");

        Tables.validator = validator;
    }

    public static final void installTableNameEncoder(TableNameEncoder tableNameEncoder) {
        Objects.requireNonNull(tableNameEncoder, "The tableNameEncoder cannot be null");

        Tables.tableNameEncoder = tableNameEncoder;
    }

    public static final Validator.Violation[] validate(Object bean) {
        Validator validator = getValidator();
        Validator.Violation[] violations = validator.validate(bean);
        return violations;
    }

    public static final Validator.Violation[] validate(Object[] beans) {
        Validator validator = getValidator();
        List<Validator.Violation> violationList = new ArrayList<>();
        for (Object bean : beans) {
            Validator.Violation[] violations = validator.validate(bean);
            if (violations.length > 0) {
                violationList.addAll(Arrays.asList(violations));
            }
        }
        return violationList.toArray(violationList.toArray(new Validator.Violation[]{}));
    }

    public static final <T> List<T> query(Class<T> domainModelClass, String sql, Object... params) throws SQLException {
        return query(new BeanModelDescriptor<>(domainModelClass), sql, params);
    }

    public static final <T> List<T> query(DomainModelDescriptor<T> domainModelDescriptor, String sql, Object... params) throws SQLException {
        String dataSourceName = domainModelDescriptor.getDataSourceName();
        return (List<T>) Databases.execute(dataSourceName, (connection, sqlExecutor) ->
                sqlExecutor.query(connection, sql, domainModelDescriptor, params));
    }

    public static final int execute(Class<?> domainModelClass, String sql, Object... params) throws SQLException {
        return Tables.execute(new BeanModelDescriptor(domainModelClass), sql, params);
    }

    public static final <T> int execute(DomainModelDescriptor<T> domainModelDescriptor, String sql, Object... params) throws SQLException {
        return Databases.execute(domainModelDescriptor.getDataSourceName(), (connection, sqlExecutor) ->
                sqlExecutor.execute(connection, sql, params));
    }

    public static final Long count(Class<?> domainModelClass, String predicate, Object... params) throws SQLException {
        Query<?> query = Databases.getQueryFactory().createQuery(domainModelClass);
        String countAlias = "count_rows";
        List rows = query.select("COUNT(*) AS " + countAlias).where(predicate, params).execute();

        if (rows.size() > 0) {
            Map<String, Object> countRowsMap = PropertyUtils.getRawAttributes(rows.get(0));
            Object count = countRowsMap.get(countRowsMap.keySet().toArray()[0]);
            if (count == null) {
                return 0L;
            } else if (count instanceof Long) {
                return (Long) count;
            } else if (count instanceof Integer) {
                return Long.valueOf((Integer) count);
            } else if (count instanceof BigDecimal) {
                return ((BigDecimal) count).longValue();
            } else {
                return 0L;
            }
        } else {
            return 0L;
        }
    }

    public static final String encodeDefaultKey(String name) {
        return String.format("%s_%s", name, DEFAULT_KEY_SUFFIX);
    }
}
