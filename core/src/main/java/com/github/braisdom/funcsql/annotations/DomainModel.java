package com.github.braisdom.funcsql.annotations;

import com.github.braisdom.funcsql.DefaultFuncSqlQuery;
import com.github.braisdom.funcsql.DefaultQuery;
import com.github.braisdom.funcsql.Query;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.SOURCE)
public @interface DomainModel {

    String tableName() default "";

    String sqlFileName() default "";

    boolean allFieldsPersistent() default true;

    Class<? extends Query> normalQueryClass() default DefaultQuery.class;

    Class<? extends Query> funcSqlQueryClass() default DefaultFuncSqlQuery.class;
}
