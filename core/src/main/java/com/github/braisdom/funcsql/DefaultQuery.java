package com.github.braisdom.funcsql;

import java.sql.SQLException;
import java.util.List;

public class DefaultQuery<T> extends AbstractQuery<T> {

    public DefaultQuery(Class<T> domainModelClass) {
        super(domainModelClass);
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
