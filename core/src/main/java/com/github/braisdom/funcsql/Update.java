package com.github.braisdom.funcsql;

public interface Update {

    Update set(String filter, Object... args);

    int execute();
}
