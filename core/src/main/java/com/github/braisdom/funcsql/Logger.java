package com.github.braisdom.funcsql;

public interface Logger {

    boolean isDebugEnabled();

    void debug(long elapsed, String sql, Object[] params);

    void info(long elapsed, String sql, Object[] params);
}
