package com.github.braisdom.funcsql;

public interface QueryExecutor {

    <T extends Class> T where(T domainModelClass, String condition, Object... args);

    <T extends Class> T select(T domainModelClass, String... columns);

    <T extends Class> T limit(T domainModelClass, int limit);

    SimpleQuery offset(int offset);

    SimpleQuery groupBy(String groupBy);

    SimpleQuery having(String having);

    SimpleQuery orderBy(String orderBy);

    SimpleQuery paginate(int offset, int limit);
}
