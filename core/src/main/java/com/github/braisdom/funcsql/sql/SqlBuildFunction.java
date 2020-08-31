package com.github.braisdom.funcsql.sql;

@FunctionalInterface
public interface SqlBuildFunction {

    String apply(SQLContext context);

}
