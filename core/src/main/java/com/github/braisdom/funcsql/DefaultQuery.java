package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.relation.Relationship;
import com.github.braisdom.funcsql.relation.RelationshipNetwork;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class DefaultQuery<T> extends AbstractQuery<T> {

    public DefaultQuery(Class<T> domainModelClass) {
        super(domainModelClass);
    }

    @Override
    public List<T> execute(Relationship... relationships) throws SQLException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        Connection connection = connectionFactory.getConnection();

        try {
            SQLGenerator sqlGenerator = Database.getSQLGenerator();
            String sql = sqlGenerator.createQuerySQL(getTableName(domainModelClass), projection, filter, groupBy,
                    having, orderBy, offset, limit);
            List<T> rows = executeInternally(connection, domainModelClass, sql);

            if (relationships.length > 0)
                new RelationshipNetwork(connection, domainModelClass).process(rows, relationships);

            return rows;
        } finally {
            if (connection != null)
                connection.close();
        }
    }

    @Override
    public List<Row> executeRawly() throws SQLException {
        return null;
    }

    @Override
    public <C extends Class> List<C> execute(C relevantDomainClass, Relationship... relationships) throws SQLException {
        SQLGenerator sqlGenerator = Database.getSQLGenerator();
        String sql = sqlGenerator.createQuerySQL(getTableName(relevantDomainClass), projection, filter, groupBy,
                having, orderBy, offset, limit);

        return null;
    }
}
