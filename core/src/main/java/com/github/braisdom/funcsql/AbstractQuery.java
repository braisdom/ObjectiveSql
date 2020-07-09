package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.Table;
import com.github.braisdom.funcsql.util.WordUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class AbstractQuery<T extends Class> implements SimpleQuery<T>, Query {

    private int limit = -1;
    private int offset = -1;

    private List<RelationDefinition> relationDefinitions = new ArrayList<>();

    private String projection;
    private String filter;
    private String orderBy;
    private String groupBy;
    private String having;

    @Override
    public SimpleQuery filter(String filter, Object... args) {
        this.filter = String.format(filter, args);
        return this;
    }

    @Override
    public SimpleQuery select(String... columns) {
        this.projection = String.join(", ", columns);
        return this;
    }

    @Override
    public SimpleQuery limit(int limit) {
        this.limit = limit;
        return this;
    }

    @Override
    public SimpleQuery orderBy(String orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    @Override
    public SimpleQuery groupBy(String groupBy) {
        this.groupBy = groupBy;
        return this;
    }

    @Override
    public SimpleQuery having(String having) {
        this.having = having;
        return this;
    }

    @Override
    public SimpleQuery offset(int offset) {
        this.offset = offset;
        return this;
    }

    @Override
    public SimpleQuery paginate(int offset, int limit) {
        offset(offset);
        limit(limit);
        return this;
    }

    @Override
    public SimpleQuery hasMany(Class relatedClass) {
        relatedTo(new RelationDefinition(RelationType.HAS_MANY, relatedClass));
        return this;
    }

    @Override
    public SimpleQuery hasMany(Class relatedClass, String foreignKey, String conditions) {
        relatedTo(new RelationDefinition(RelationType.HAS_MANY, relatedClass)
                .setConditions(conditions));
        return this;
    }

    @Override
    public SimpleQuery hasOne(Class relatedClass, String foreignKey) {
        relatedTo(new RelationDefinition(RelationType.HAS_ONE, relatedClass));
        return this;
    }

    @Override
    public SimpleQuery hasOne(Class relatedClass, String foreignKey, String conditions) {
        relatedTo(new RelationDefinition(RelationType.HAS_ONE, relatedClass)
                .setConditions(conditions));
        return this;
    }

    @Override
    public SimpleQuery belongsTo(Class relatedClass, String foreignKey) {
        relatedTo(new RelationDefinition(RelationType.BELONGS_TO, relatedClass));
        return this;
    }

    @Override
    public SimpleQuery belongsTo(Class relatedClass, String foreignKey, String conditions) {
        relatedTo(new RelationDefinition(RelationType.BELONGS_TO, relatedClass)
                .setConditions(conditions));
        return this;
    }

    @Override
    public SimpleQuery relatedTo(RelationDefinition relationDefinition) {
        relationDefinitions.add(relationDefinition);
        return this;
    }

    @Override
    public List<T> executeSimply(T rowClass) throws SQLException {
        Objects.requireNonNull(rowClass, "The rowClass cannot be null");

        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        SQLExecutor sqlExecutor = Database.getSqlExecutor();
        SQLGenerator sqlGenerator = Database.getSQLGenerator();
        String tableName = getTableName(rowClass);

        String sql = sqlGenerator.createQuerySQL(tableName, projection, filter, groupBy,
                having, orderBy, offset, limit);
        List<T> objects = sqlExecutor.query(connectionFactory.getConnection(), sql, rowClass);

        processAssociations(connectionFactory, sqlExecutor, rowClass, objects);

        return objects;
    }

    public List<Row> executeSimply(String tableName) throws SQLException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        SQLExecutor sqlExecutor = Database.getSqlExecutor();
        SQLGenerator sqlGenerator = Database.getSQLGenerator();

        String sql = sqlGenerator.createQuerySQL(tableName, projection, filter, groupBy,
                having, orderBy, offset, limit);
        return sqlExecutor.query(connectionFactory.getConnection(), sql);
    }

    @Override
    public List<T> execute(Class rowClass, String sql) throws SQLException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        SQLExecutor sqlExecutor = Database.getSqlExecutor();

        return sqlExecutor.query(connectionFactory.getConnection(), sql, rowClass);
    }

    @Override
    public List<Row> execute(String sql) throws SQLException {
        ConnectionFactory connectionFactory = Database.getConnectionFactory();
        SQLExecutor sqlExecutor = Database.getSqlExecutor();

        return sqlExecutor.query(connectionFactory.getConnection(), sql);
    }

    protected void processAssociations(ConnectionFactory connectionFactory, SQLExecutor sqlExecutor,
                                       Class rowClass, List rows) throws SQLException {
        for (RelationDefinition relationDefinition : relationDefinitions) {
            if(RelationType.BELONGS_TO.equals(relationDefinition.getRelationType()))
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

        Table table = (Table) (tableClass == null ? null : tableClass.getAnnotation(Table.class));

        if (table != null)
            tableName = table.value();
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
