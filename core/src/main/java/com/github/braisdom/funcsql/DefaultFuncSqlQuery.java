package com.github.braisdom.funcsql;

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
    public List<T> execute(RelationDefinition... relationDefinitions) throws SQLException {
        return null;
    }

    @Override
    public List<Row> executeCrudely() throws SQLException {
        return null;
    }

    @Override
    public <C extends Class> List<C> executeCrudely(C relevantDomainClass, RelationDefinition... relationDefinitions) throws SQLException {
        return null;
    }
}
