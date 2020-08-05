package com.github.braisdom.funcsql;

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
public final class Table {

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

    public static Validator getValidator() {
        return validator;
    }

    public static final void installValidator(Validator validator) {
        Table.validator = validator;
    }

    public static final void validate(Object bean, boolean skipValidation) throws ValidationException {
        if (skipValidation) {
            Validator validator = getValidator();
            Validator.Violation[] violations = validator.validate(bean);
            if (violations.length > 0)
                throw new ValidationException(violations);
        }
    }

    public static final void validate(Object[] beans, boolean skipValidation) throws ValidationException {
        if (skipValidation) {
            Validator validator = getValidator();
            List<Validator.Violation> violationList = new ArrayList<>();
            for (Object bean : beans) {
                Validator.Violation[] violations = validator.validate(bean);
                if (violations.length > 0)
                    violationList.addAll(Arrays.asList(violations));
            }
            if (violationList.size() > 0)
                throw new ValidationException(violationList.toArray(new Validator.Violation[]{}));
        }
    }

    public static final int count(Class<?> domainModelClass, String predicate) throws SQLException {
        Query<?> query = Database.getQueryFactory().createQuery(domainModelClass);
        String countAlias = "_count";
        List rows = query.select("COUNT(*) AS " + countAlias).where(predicate).execute();

        if (rows.size() > 0) {
            return (int) PropertyUtils.getRawAttribute(rows.get(0), countAlias);
        } else return 0;
    }

    public static final String encodeDefaultKey(String name) {
        return String.format("%s_%s", name, DEFAULT_KEY_SUFFIX);
    }
}
