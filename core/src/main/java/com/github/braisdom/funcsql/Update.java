package com.github.braisdom.funcsql;

public interface Update {

    Update set(String filter, Object... args);

    Query where(String filter, Object... args);

    int execute();
}
