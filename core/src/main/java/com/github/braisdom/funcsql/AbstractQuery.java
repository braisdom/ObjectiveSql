package com.github.braisdom.funcsql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public abstract class AbstractQuery<T> implements Query<T> {

    protected final Class<T> domainModelClass;

    protected int limit = -1;
    protected int offset = -1;

    protected String projection;
    protected String filter;
    protected String orderBy;
    protected String groupBy;
    protected String having;

    public AbstractQuery(Class<T> domainModelClass) {
        this.domainModelClass = domainModelClass;
    }

    @Override
    public Query where(String filter, Object... args) {
        this.filter = String.format(filter, args);
        return this;
    }

    @Override
    public Query select(String... columns) {
        this.projection = String.join(", ", columns);
        return this;
    }

    @Override
    public Query limit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public Query orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    @Override
    public Query groupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    @Override
    public Query having(String having) {
        this.having = having;
        return this;
    }

    @Override
    public Query offset(int offset) {
        this.offset = offset;
        return this;
    }

    protected <C> List<C> executeInternally(Connection connection, Class<C> domainModelClass, String sql) throws SQLException {
        SQLExecutor sqlExecutor = Database.getSqlExecutor();
        return sqlExecutor.query(connection, sql, domainModelClass);
    }

    protected List<Row> executeRawInternally(String sql) throws SQLException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        SQLExecutor sqlExecutor = Database.getSqlExecutor();

        return sqlExecutor.query(connectionFactory.getConnection(), sql);
    }

    protected void processRelation(Connection connection, List rows, Relationship relationship) throws SQLException {
        SQLExecutor sqlExecutor = Database.getSqlExecutor();
        String foreignKey = relationship.getForeignKey();
        String relationTableName = getTableName(relationship.getRelatedClass());

        SQLGenerator sqlGenerator = Database.getSQLGenerator();
//
//        Map<Object, List<RawRelationObject>> baseRows = (Map<Object, List<RawRelationObject>>) rows.stream()
//                .map(row -> new RawRelationObject(relationship, row))
//                .collect(Collectors.groupingBy(RawRelationObject::getValue));
//
//        String relationConditions = relationship.getCondition() == null
//                ? String.format(" %s IN (%s) ", foreignKey, quote(baseRows.keySet().toArray()))
//                : String.format(" %s IN (%s) AND (%s)", foreignKey, quote(baseRows.keySet().toArray()), relationship.getCondition());
//        String relationTableQuerySql = sqlGenerator.createQuerySQL(relationTableName, null, relationConditions,
//                null, null, null, -1, -1);
//
//        List<Object> relations = sqlExecutor.query(connection, relationTableQuerySql,
//                relationship.getRelatedClass());
//
//        Map<Object, List<RawRelationObject>> relationRows = relations.stream()
//                .map(row -> new RawRelationObject(relationship, row))
//                .collect(Collectors.groupingBy(RawRelationObject::getValue));
//
//        for (Object key : baseRows.keySet()) {
//            List<RawRelationObject> relationObjects = relationRows.get(key);
//            if (relationObjects != null)
//                baseRows.get(key).forEach(baseRow -> baseRow.setRelations(relationship, relationObjects));
//        }
    }

    protected String getTableName(Class tableClass) {
        return Table.getTableName(tableClass);
    }

    protected String quote(Object... values) {
        StringBuilder sb = new StringBuilder();

        for (Object value : values) {
            if (value instanceof Integer || value instanceof Long ||
                    value instanceof Float || value instanceof Double)
                sb.append(String.valueOf(value));
            else
                sb.append(String.format("'%s'", String.valueOf(value)));
            sb.append(",");
        }
        sb.delete(sb.length() - 1, sb.length());
        return sb.toString();
    }
}
