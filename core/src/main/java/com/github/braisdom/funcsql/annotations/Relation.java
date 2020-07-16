package com.github.braisdom.funcsql.annotations;

import com.github.braisdom.funcsql.RelationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Relation {
    RelationType relationType() default RelationType.HAS_MANY;

    String primaryKey() default "";

    String foreignKey() default "";

    String condition() default "";
}
