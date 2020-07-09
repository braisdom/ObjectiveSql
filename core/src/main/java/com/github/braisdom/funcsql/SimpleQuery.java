package com.github.braisdom.funcsql;

import java.sql.SQLException;
import java.util.List;

public interface SimpleQuery<T extends Class> extends Relation {

    SimpleQuery filter(String filter, Object... args);

    SimpleQuery select(String... columns);

    SimpleQuery limit(int limit);

    SimpleQuery offset(int offset);

    SimpleQuery groupBy(String groupBy);

    SimpleQuery having(String having);

    SimpleQuery orderBy(String orderBy);

    SimpleQuery paginate(int offset, int limit);

    List<T> executeSimply(T rowClass) throws SQLException;

    List<Row> executeSimply(String tableName) throws SQLException;
}
