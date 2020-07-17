package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.relation.Relationship;

import java.sql.SQLException;
import java.util.List;

public class DefaultFuncSqlQuery<T> extends AbstractQuery<T>  {

    private final String sql;
    private final Object[] params;

    public DefaultFuncSqlQuery(Class<T> domainModelClass, String sql, Object... params) {
        super(domainModelClass);

        this.sql = sql;
        this.params = params;
    }

    @Override
    public List<T> execute(Relationship... relationships) throws SQLException {
        return null;
    }

    @Override
    public List<Row> executeRawly() throws SQLException {
        return null;
    }

    @Override
    public <C extends Class> List<C> execute(C relevantDomainClass, Relationship... relationships) throws SQLException {
        return null;
    }
}
