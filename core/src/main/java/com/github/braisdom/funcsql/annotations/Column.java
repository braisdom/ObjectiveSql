package com.github.braisdom.funcsql.annotations;

import com.github.braisdom.funcsql.transition.ColumnTransitional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    String name() default "";

    Class<? extends ColumnTransitional> transition() default ColumnTransitional.class;

    boolean insertable() default true;

    boolean updatable() default true;
}
