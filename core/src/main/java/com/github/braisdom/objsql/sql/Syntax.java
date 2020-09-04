package com.github.braisdom.objsql.sql;

import com.github.braisdom.objsql.DatabaseType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Syntax {
    DatabaseType[] value() default {DatabaseType.Unknown};

    DatabaseType[] only() default {};

    DatabaseType[] except() default {};
}

