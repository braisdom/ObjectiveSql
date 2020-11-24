package com.github.braisdom.objsql.pagination;

import com.github.braisdom.objsql.relation.Relationship;

import java.sql.SQLException;

public class DefaultPaginator<T> implements Paginator<T> {

    @Override
    public PagedList<T> paginate(Paginatable paginatable, Relationship... relationships) throws SQLException {
        return null;
    }
}
