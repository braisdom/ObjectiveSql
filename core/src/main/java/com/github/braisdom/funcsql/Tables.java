package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.Column;
import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.PrimaryKey;
import com.github.braisdom.funcsql.reflection.PropertyUtils;
import com.github.braisdom.funcsql.util.StringUtil;
import com.github.braisdom.funcsql.util.WordUtil;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;

/**
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

        String tableName;
        if (!StringUtil.isBlank(domainModel.tableName()))
            tableName = domainModel.tableName();
        else
            tableName = WordUtil.tableize(baseClass.getSimpleName());

        return tableName;
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
        if (primaryKey != null)
            return true;
        else return DEFAULT_PRIMARY_KEY.equals(field.getName());
    }

    public static final Field getPrimaryField(Class tableClass) {
        Field[] fields = tableClass.getDeclaredFields();

        Field defaultField = null;
        Field primaryField = null;

        for (Field field : fields) {
            PrimaryKey primaryKey = field.getDeclaredAnnotation(PrimaryKey.class);
            if (primaryKey != null)
                primaryField = field;

            if (DEFAULT_PRIMARY_KEY.equals(field.getName()))
                defaultField = field;
        }

        return primaryField == null ? defaultField : primaryField;
    }

    public static final String getColumnName(Class tableClass, String fieldName) {
        try {
            Field field = tableClass.getDeclaredField(fieldName);
            Column column = field.getDeclaredAnnotation(Column.class);
            if(column != null)
                return column.name();

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

    public static final void validate(Object bean) throws ValidationException {
        validate(bean, true);
    }

    public static final Validator.Violation[] validate(Object bean, boolean suppressException) throws ValidationException {
        Validator validator = getValidator();
        Validator.Violation[] violations = validator.validate(bean);
        if (violations.length > 0 && !suppressException)
            throw new ValidationException(violations);

        return violations;
    }

    public static final void validate(Object[] beans) throws ValidationException {
        validate(beans, false);
    }

    public static final Validator.Violation[] validate(Object[] beans, boolean suppressException) throws ValidationException {
        Validator validator = getValidator();
        List<Validator.Violation> violationList = new ArrayList<>();
        for (Object bean : beans) {
            Validator.Violation[] violations = validator.validate(bean);
            if (violations.length > 0)
                violationList.addAll(Arrays.asList(violations));
        }
        if (violationList.size() > 0 && !suppressException)
            throw new ValidationException(violationList.toArray(new Validator.Violation[]{}));

        return violationList.toArray(new Validator.Violation[0]);
    }

    public static final <T> List<T> query(Class<T> domainModelClass, String sql, Object... params) throws SQLException {
        return query(new BeanModelDescriptor<>(domainModelClass), sql, params);
    }

    public static final <T> List<T> query(DomainModelDescriptor<T> domainModelDescriptor, String sql, Object... params) throws SQLException {
        return (List<T>) Databases.execute((connection, sqlExecutor) ->
                sqlExecutor.query(connection, sql, domainModelDescriptor, params));
    }

    public static final int execute(String sql, Object... params) throws SQLException {
        return Databases.execute((connection, sqlExecutor) ->
                sqlExecutor.execute(connection, sql, params));
    }

    public static final int count(Class<?> domainModelClass, String predicate, Object... params) throws SQLException {
        Query<?> query = Databases.getQueryFactory().createQuery(domainModelClass);
        String countAlias = "_count";
        List rows = query.select("COUNT(*) AS " + countAlias).where(predicate, params).execute();

        if (rows.size() > 0) {
            Object count = PropertyUtils.getRawAttribute(rows.get(0), countAlias);
            return count == null ? 0 : (int) count;
        } else return 0;
    }

    public static final String encodeDefaultKey(String name) {
        return String.format("%s_%s", name, DEFAULT_KEY_SUFFIX);
    }
}
