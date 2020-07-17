package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.relation.Relationship;

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

    List<Row> executeRawly() throws SQLException;

    <C extends Class> List<C> execute(C relevantDomainClass, Relationship... relationships) throws SQLException;
}
