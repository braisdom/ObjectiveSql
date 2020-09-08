package com.github.braisdom.objsql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The domain model is a core concept, it holds the logic and status in application,
 * describes the data structure of database at the same time.
 * In ObjectiveSql, there are queries, persistence and convenient methods for database,
 *
 * <ul>
 *     <li>The setter and getter methods of fields</li>
 *     <li>The factory method for query and persistence</li>
 *     <li>The query method of queryable field and model instance</li>
 *     <li>The persistence method of model instance</li>
 *     <li>The transactional method of domain logic</li>
 * </ul>
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainModel {

    String tableName() default "";

    boolean fluent() default true;

    Class<?> primaryClass() default Integer.class;

    String primaryColumnName() default "id";

    String primaryFieldName() default "id";

    boolean skipNullValueOnUpdating() default true;

    boolean allFieldsPersistent() default true;
}
