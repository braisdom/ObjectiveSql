package com.github.braisdom.funcsql.annotations;

public @interface Transactional {

    boolean rollback() default true;

}
