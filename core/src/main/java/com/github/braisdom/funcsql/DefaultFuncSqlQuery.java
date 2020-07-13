package com.github.braisdom.funcsql;

import java.sql.SQLException;
import java.util.List;

public class DefaultFuncSqlQuery<T> extends AbstractQuery<T> implements FunSqlQuery<T> {

    private final String sql;
    private final Object[] params;

    public DefaultFuncSqlQuery(Class<T> domainModelClass, String sql, Object... params) {
        super(domainModelClass);

        this.sql = sql;
        this.params = params;
    }

    @Override
    public List<T> execute() throws SQLException {
        return null;
    }

    @Override
    public List<Row> execute(Relation... relations) throws SQLException {
        return null;
    }

    @Override
    public List execute(Class relevantDomainClass, Relation... relations) throws SQLException {
        return null;
    }
}
