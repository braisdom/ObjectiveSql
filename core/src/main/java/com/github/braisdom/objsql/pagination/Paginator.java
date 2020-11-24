package com.github.braisdom.objsql.pagination;

import com.github.braisdom.objsql.Query;
import com.github.braisdom.objsql.relation.Relationship;

import java.sql.SQLException;

public interface Paginator<T> extends Query<T> {

    PagedList<T> paginate(Relationship... relationships) throws SQLException;
}
