package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.util.WordUtil;

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

    protected <C> List<C> executeInternally(Class<C> domainModelClass, String sql) throws SQLException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        SQLExecutor sqlExecutor = Database.getSqlExecutor();
        List<C> objects = sqlExecutor.query(connectionFactory.getConnection(), sql, domainModelClass);

        processAssociations(connectionFactory, sqlExecutor, domainModelClass, objects);

        return objects;
    }

    protected List<Row> executeRawInternally(String sql) throws SQLException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        SQLExecutor sqlExecutor = Database.getSqlExecutor();

        return sqlExecutor.query(connectionFactory.getConnection(), sql);
    }

    protected void processAssociations(ConnectionFactory connectionFactory, SQLExecutor sqlExecutor,
                                       Class rowClass, List rows) throws SQLException {
        for (RelationDefinition relationDefinition : relationDefinitions) {
            if (RelationType.BELONGS_TO.equals(relationDefinition.getRelationType()))
                processBelongsTo(connectionFactory, sqlExecutor, relationDefinition, rowClass, rows);
            else
                processHasAny(connectionFactory, sqlExecutor, relationDefinition, rowClass, rows);
        }
    }

    protected void processHasAny(ConnectionFactory connectionFactory, SQLExecutor sqlExecutor,
                                 RelationDefinition relationDefinition, Class rowClass, List rows) throws SQLException {
        String foreignKey = relationDefinition.getForeignKey(rowClass);
        String primaryKey = relationDefinition.getPrimaryKey(rowClass);
        String relationTableName = getTableName(relationDefinition.getRelatedClass());

        SQLGenerator sqlGenerator = Database.getSQLGenerator();

        Map<Object, List<RawRelationObject>> baseRows = (Map<Object, List<RawRelationObject>>) rows.stream()
                .map(row -> new RawRelationObject(rowClass, primaryKey, row))
                .collect(Collectors.groupingBy(RawRelationObject::getValue));

        String relationConditions = relationDefinition.getConditions() == null
                ? String.format(" %s IN (%s) ", foreignKey, quote(baseRows.keySet().toArray()))
                : relationDefinition.getConditions();
        String childTableQuerySql = sqlGenerator.createQuerySQL(relationTableName, null, relationConditions,
                null, null, null, -1, -1);

        List<Object> relations = sqlExecutor.query(connectionFactory.getConnection(), childTableQuerySql,
                relationDefinition.getRelatedClass());

        Map<Object, List<RawRelationObject>> relationRows = relations.stream()
                .map(row -> new RawRelationObject(relationDefinition.getRelatedClass(), foreignKey, row))
                .collect(Collectors.groupingBy(RawRelationObject::getValue));

        for (Object key : baseRows.keySet()) {
            List<RawRelationObject> relationObjects = relationRows.get(key);
            if (relationObjects != null)
                baseRows.get(key).forEach(baseRow -> baseRow.setRelations(relationDefinition.getRelationType(),
                        relationDefinition.getRelatedClass(), relationObjects));
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
                baseRows.get(key).forEach(baseRow -> baseRow.setRelations(relationDefinition.getRelationType(),
                        relationDefinition.getRelatedClass(), baseObjects));
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
