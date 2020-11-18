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
import com.github.braisdom.objsql.util.StringUtil;
import com.github.braisdom.objsql.util.WordUtil;

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

    public static final String DEFAULT_PRIMARY_KEY = "id";
    public static final String DEFAULT_KEY_SUFFIX = "id";

    private static Validator validator = bean -> {
        javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Object>> rawViolations = validator.validate(bean);

        return rawViolations.stream().map(violation ->
                new Validator.Violation(violation.getRootBeanClass(), violation.getMessage(), violation.getInvalidValue(),
                        violation.getPropertyPath().toString()))
                .toArray(Validator.Violation[]::new);
    };

    public static final String getTableName(Class baseClass) {
        Objects.requireNonNull(baseClass, "The baseClass cannot be null");
        DomainModel domainModel = (DomainModel) baseClass.getAnnotation(DomainModel.class);

        Objects.requireNonNull(domainModel, "The baseClass must have the DomainModel annotation");

        if (!StringUtil.isBlank(domainModel.tableName())) {
            return domainModel.tableName();
        } else {
            return WordUtil.tableize(baseClass.getSimpleName());
        }
    }

    public static final String getDataSourceName(Class baseClass) {
        Objects.requireNonNull(baseClass, "The baseClass cannot be null");
        DomainModel domainModel = (DomainModel) baseClass.getAnnotation(DomainModel.class);

        Objects.requireNonNull(domainModel, "The baseClass must have the DomainModel annotation");

        if (!StringUtil.isBlank(domainModel.dataSource())) {
            return domainModel.dataSource();
        } else {
            return ConnectionFactory.DEFAULT_DATA_SOURCE_NAME;
        }
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

    public static final boolean isPrimaryField(Field field) {
        PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
        if (primaryKey != null) {
            return true;
        } else {
            return DEFAULT_PRIMARY_KEY.equals(field.getName());
        }
    }

    public static Object getPrimaryValue(Object domainObject) {
        PrimaryKey primaryKey = getPrimaryKey(domainObject.getClass());
        if (primaryKey != null) {
            return PropertyUtils.read(domainObject, primaryKey.name());
        } else {
            return null;
        }
    }

    public static void writePrimaryValue(Object domainObject, Object primaryValue) {
        PrimaryKey primaryKey = getPrimaryKey(domainObject.getClass());
        if (primaryKey != null) {
            PropertyUtils.write(domainObject, primaryKey.name(), primaryValue);
        }
    }

    public static final Field getPrimaryField(Class tableClass) {
        Field[] fields = tableClass.getDeclaredFields();

        Field defaultField = null;
        Field primaryField = null;

        for (Field field : fields) {
            PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
            if (primaryKey != null) {
                primaryField = field;
            }

            if (DEFAULT_PRIMARY_KEY.equals(field.getName())) {
                defaultField = field;
            }
        }

        return primaryField == null ? defaultField : primaryField;
    }

    public static final String getColumnName(Class tableClass, String fieldName) {
        try {
            Field field = tableClass.getDeclaredField(fieldName);
            Column column = field.getDeclaredAnnotation(Column.class);

            if (column != null) {
                if (!WordUtil.isEmpty(column.name())) {
                    return column.name();
                }
            }

            return WordUtil.underscore(field.getName());
        } catch (NoSuchFieldException ex) {
            throw new DomainModelException(ex.getMessage(), ex);
        }
    }

    public static Validator getValidator() {
        return validator;
    }

    public static final void installValidator(Validator validator) {
        Tables.validator = validator;
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
        String dataSourceName = Tables.getDataSourceName(domainModelDescriptor.getDomainModelClass());
        return (List<T>) Databases.execute(dataSourceName, (connection, sqlExecutor) ->
                sqlExecutor.query(connection, sql, domainModelDescriptor, params));
    }

    public static final int execute(Class<?> domainModelClass, String sql, Object... params) throws SQLException {
        String dataSourceName = Tables.getDataSourceName(domainModelClass);
        return Databases.execute(dataSourceName, (connection, sqlExecutor) ->
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
            } else if(count instanceof BigDecimal) {
                return ((BigDecimal)count).longValue();
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
