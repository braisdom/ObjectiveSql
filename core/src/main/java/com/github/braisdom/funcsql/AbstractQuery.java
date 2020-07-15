package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.util.WordUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractQuery<T> implements Query<T> {

    private List<RelationDefinition> relationDefinitions = new ArrayList<>();

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

    protected void processRelation(Connection connection, List rows, Relation relation) throws SQLException {
        if (RelationType.BELONGS_TO.equals(relation.getRelationType()))
            processBelongsTo(null, null, null, null, rows);
        else
            processHasAny(connection, rows, relation);
    }

    protected void processHasAny(Connection connection, List rows, Relation relation) throws SQLException {
        SQLExecutor sqlExecutor = Database.getSqlExecutor();
        String foreignKey = relation.getForeignKey();
        String primaryKey = relation.getPrimaryKey();
        String relationTableName = getTableName(relation.getRelatedClass());

        SQLGenerator sqlGenerator = Database.getSQLGenerator();

        Map<Object, List<RawRelationObject>> baseRows = (Map<Object, List<RawRelationObject>>) rows.stream()
                .map(row -> new RawRelationObject(domainModelClass, primaryKey, row))
                .collect(Collectors.groupingBy(RawRelationObject::getValue));

        String relationConditions = relation.getCondition() == null
                ? String.format(" %s IN (%s) ", foreignKey, quote(baseRows.keySet().toArray()))
                : String.format(" %s IN (%s) AND (%s)", foreignKey, quote(baseRows.keySet().toArray()), relation.getCondition());
        String childTableQuerySql = sqlGenerator.createQuerySQL(relationTableName, null, relationConditions,
                null, null, null, -1, -1);

        List<Object> relations = sqlExecutor.query(connection, childTableQuerySql,
                relation.getRelatedClass());

        Map<Object, List<RawRelationObject>> relationRows = relations.stream()
                .map(row -> new RawRelationObject(relation.getRelatedClass(), foreignKey, row))
                .collect(Collectors.groupingBy(RawRelationObject::getValue));

        for (Object key : baseRows.keySet()) {
            List<RawRelationObject> relationObjects = relationRows.get(key);
            if (relationObjects != null)
                baseRows.get(key).forEach(baseRow -> baseRow.setRelations(relation, relationObjects));
        }
    }

    protected void processBelongsTo(ConnectionFactory connectionFactory, SQLExecutor sqlExecutor,
                                    RelationDefinition relationDefinition, Class rowClass, List rows) throws SQLException {
        String foreignKey = relationDefinition.getBelongsToForeignKey(relationDefinition.getRelatedClass());
        String primaryKey = relationDefinition.getPrimaryKey(relationDefinition.getRelatedClass());
        String relationTableName = getTableName(relationDefinition.getRelatedClass());

        SQLGenerator sqlGenerator = Database.getSQLGenerator();

        Map<Object, List<RawRelationObject>> relationRows = (Map<Object, List<RawRelationObject>>) rows.stream()
                .map(row -> new RawRelationObject(rowClass, foreignKey, row))
                .collect(Collectors.groupingBy(RawRelationObject::getValue));

        String baseConditions = String.format(" %s IN (%s) ", primaryKey, quote(relationRows.keySet().toArray()));
        String baseTableQuerySql = sqlGenerator.createQuerySQL(relationTableName, null, baseConditions,
                null, null, null, -1, -1);

        List<Object> rawBaseObjects = sqlExecutor.query(connectionFactory.getConnection(), baseTableQuerySql,
                relationDefinition.getRelatedClass());

        Map<Object, List<RawRelationObject>> baseRows = rawBaseObjects.stream()
                .map(row -> new RawRelationObject(relationDefinition.getRelatedClass(), primaryKey, row))
                .collect(Collectors.groupingBy(RawRelationObject::getValue));

        for (Object key : baseRows.keySet()) {
            List<RawRelationObject> baseObjects = relationRows.get(key);
            if (baseObjects != null)
                baseRows.get(key).forEach(baseRow -> baseRow.setRelations(null, baseObjects));
        }
    }

    protected String getTableName(Class tableClass) {
        String tableName;
        DomainModel domainModel = (DomainModel) (tableClass == null ? null : tableClass.getAnnotation(DomainModel.class));

        if (domainModel != null)
            tableName = domainModel.tableName();
        else
            tableName = WordUtil.tableize(tableClass.getSimpleName());

        return tableName;
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
