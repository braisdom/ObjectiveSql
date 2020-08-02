package com.github.braisdom.funcsql;

public interface Logger {

    boolean isDebugEnabled();

    boolean isInfoEnabled();

    boolean isErrorEnabled();

    void debug(long elapsedTime, String sql, Object[] params);

    void info(long elapsedTime, String sql, Object[] params);

    void error(String message, Throwable throwable);
}
