package com.github.braisdom.objsql;

public interface Logger {

    void info(long elapsedTime, String sql, Object[] params);

    void error(String message, Throwable throwable);
}
