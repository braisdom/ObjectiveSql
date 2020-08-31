package com.github.braisdom.funcsql.osql;

public interface Expression extends Sqlizable {

    Expression as(String alias);
}
