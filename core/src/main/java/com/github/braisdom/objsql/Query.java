package com.github.braisdom.objsql;

import com.github.braisdom.objsql.relation.Relationship;

import java.sql.SQLException;
import java.util.List;

/**
 * A programmable structure for SQL statement.
 * @param <T>
 */
public interface Query<T> {

    Query where(String filter, Object... args);

    Query select(String... columns);

    Query limit(int limit);

    Query offset(int offset);

    Query groupBy(String groupBy);

    Query having(String having);

    Query orderBy(String orderBy);

    List<T> execute(Relationship... relationships) throws SQLException;

    T queryFirst(Relationship... relationships) throws SQLException;

    <C extends Class> List<C> execute(C relevantDomainClass, Relationship... relationships) throws SQLException;
}
