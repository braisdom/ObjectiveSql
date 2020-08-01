package com.github.braisdom.funcsql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Transactional {

    boolean rollback() default true;

    Class<? extends Throwable>[] skipRollbackAt() default {};

}
