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

    List<T> execute(Relationship... relationships) throws SQLException;

    List<Row> executeCrudely() throws SQLException;

    <C extends Class> List<C> executeCrudely(C relevantDomainClass, Relationship... relationships) throws SQLException;
}
