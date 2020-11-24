package com.github.braisdom.objsql.pagination;

import com.github.braisdom.objsql.relation.Relationship;

import java.sql.SQLException;

public interface Paginator<T> {

    PagedList<T> paginate(Paginatable paginatable,
                          Relationship... relationships) throws SQLException;
}
