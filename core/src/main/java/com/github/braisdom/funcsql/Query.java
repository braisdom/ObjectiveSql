package com.github.braisdom.funcsql;

import java.sql.SQLException;
import java.util.List;

public interface Query<T> {

    Query where(String filter, Object... args);

    Query select(String... columns);

    Query limit(int limit);

    Query offset(int offset);

    Query groupBy(String groupBy);

    Query having(String having);

    Query orderBy(String orderBy);

    <C extends Class> List<C> execute(C relevantDomainClass, Relation... relations) throws SQLException;

}
