package com.github.braisdom.funcsql;

import java.sql.SQLException;
import java.util.List;

public class FuncSqlQuery<T extends Class> extends AbstractQuery<T> {

    private final String sql;
    private final Object[] params;

    public FuncSqlQuery(Class<T> domainModelClass, String sql, Object... params) {
        super(domainModelClass);

        this.sql = sql;
        this.params = params;
    }

    @Override
    public List<T> execute() throws SQLException {
        return null;
    }

    @Override
    public List<T> execute(Relation... relations) throws SQLException {
        return null;
    }

    @Override
    public <C extends Class> List<C> execute(C relevantDomainClass) throws SQLException {
        return null;
    }

    @Override
    public <C extends Class> List<C> execute(C relevantDomainClass, Relation... relations) throws SQLException {
        return null;
    }

    @Override
    public List<Row> executeRawly() throws SQLException {
        return null;
    }
}
