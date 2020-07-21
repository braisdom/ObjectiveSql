package com.github.braisdom.funcsql;

public interface Update {

    Update set(String set, Object... args);

    Update where(String filter, Object... args);

    int execute();
}
