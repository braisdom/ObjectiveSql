package com.github.braisdom.objsql.benchmark;

import com.zaxxer.hikari.HikariDataSource;

public interface ORMFramework {

    void initialize() throws Exception;

    void update() throws Exception;

    User query() throws Exception;

    void teardown();

    class Factory {

        public static ORMFramework createORMFramework(String frameworkName, HikariDataSource dataSource) {
            switch (frameworkName) {
                case MyBatis.FRAMEWORK_NAME:
                    return new MyBatis(dataSource);
                case Jdbc.FRAMEWORK_NAME:
                    return new Jdbc(dataSource);
                case ObjectiveSQL.FRAMEWORK_NAME:
                    return new ObjectiveSQL(dataSource);
                case Hibernate.FRAMEWORK_NAME:
                    return new Hibernate(dataSource);
                default:
                    throw new IllegalArgumentException("Cannot find ORM framework: " + frameworkName);
            }
        }
    }
}
