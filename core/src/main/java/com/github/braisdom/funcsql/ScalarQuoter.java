package com.github.braisdom.funcsql;

public interface ScalarQuoter {

    String quote(Object... scalars);
}
