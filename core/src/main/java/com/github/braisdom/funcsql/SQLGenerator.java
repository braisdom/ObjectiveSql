package com.github.braisdom.funcsql;

public interface SQLGenerator {

    String createQuerySQL(String tableName, String projections, String filter, String groupBy,
                          String having, String orderBy, int offset, int limit);

    String createQuerySQL(String tableName, String projections, String filter);

    String createUpdateSQL(String tableName, String update, String filter);

    String createDeleteSQL(String tableName, String filter);

    String quote(Object... scalars);
}
