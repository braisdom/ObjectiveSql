package com.github.braisdom.funcsql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DomainModel {

    String tableName() default "";

    String sqlFileName() default "";

    boolean fluent() default true;

    Class<?> primaryClass() default Integer.class;

    String primaryName() default "id";

    boolean skipNullValueOnUpdating() default true;

    boolean allFieldsPersistent() default true;

    boolean disableGeneratedId() default false;
}
