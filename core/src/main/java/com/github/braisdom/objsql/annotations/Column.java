package com.github.braisdom.objsql.annotations;

import com.github.braisdom.objsql.transition.ColumnTransitional;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotation is used when the field name is not Java compliant only.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Column {

    String name() default "";

    /**
     * Customize a column transition for the column
     * @return
     */
    Class<? extends ColumnTransitional> transition() default ColumnTransitional.class;

    boolean insertable() default true;

    boolean updatable() default true;
}
