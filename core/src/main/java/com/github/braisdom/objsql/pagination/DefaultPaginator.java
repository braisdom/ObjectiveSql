package com.github.braisdom.objsql.pagination;

import com.github.braisdom.objsql.DefaultQuery;
import com.github.braisdom.objsql.relation.Relationship;

import java.sql.SQLException;

public class DefaultPaginator<T> extends DefaultQuery<T> implements Paginator<T> {

    public DefaultPaginator(Class<T> domainModelClass) {
        super(domainModelClass);
    }

    @Override
    public PagedList<T> paginate(Relationship... relationships) throws SQLException {
        return null;
    }
}
