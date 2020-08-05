package com.github.braisdom.funcsql;

public interface Logger {

    void info(long elapsedTime, String sql, Object[] params);

    void error(String message, Throwable throwable);
}
