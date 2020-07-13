package com.github.braisdom.funcsql;

import java.sql.SQLException;
import java.util.List;

public class DefaultQuery<T extends Class> extends AbstractQuery<T> implements SimpleQuery<T> {

    public DefaultQuery(Class<T> domainModelClass) {
        super(domainModelClass);
    }

    @Override
    public List<T> execute(Relation... relations) throws SQLException {
        SQLGenerator sqlGenerator = Database.getSQLGenerator();
        String sql = sqlGenerator.createQuerySQL(getTableName(domainModelClass), projection, filter, groupBy,
                having, orderBy, offset, limit);
        return executeInternally(domainModelClass, sql);
    }

    @Override
    public <C extends Class> List<C> execute(C relevantDomainClass, Relation... relations) throws SQLException {
        SQLGenerator sqlGenerator = Database.getSQLGenerator();
        String sql = sqlGenerator.createQuerySQL(getTableName(relevantDomainClass), projection, filter, groupBy,
                having, orderBy, offset, limit);
        return executeInternally(relevantDomainClass, sql);
    }
}
