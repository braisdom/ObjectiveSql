package com.github.braisdom.funcsql.relation;

import com.github.braisdom.funcsql.Database;
import com.github.braisdom.funcsql.SQLExecutor;
import com.github.braisdom.funcsql.SQLGenerator;
import com.github.braisdom.funcsql.Table;
import com.github.braisdom.funcsql.util.StringUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RelationshipNetwork implements RelationProcessor.Context{

    private final Connection connection;
    private final Class baseClass;

    private final Map<Class, List> relationObjectsMap;

    public RelationshipNetwork(Connection connection, Class baseClass) {
        this.connection = connection;
        this.baseClass = baseClass;

        this.relationObjectsMap = new HashMap<>();
    }

    @Override
    public List queryRelatedObjects(Class clazz, String associationColumn,
                                    Object[] associatedValues, String condition) throws SQLException {
        List cachedObjects = relationObjectsMap.get(clazz);
        if(cachedObjects == null) {
            cachedObjects = queryObjects(clazz, associationColumn, associatedValues, condition);
            relationObjectsMap.put(clazz, cachedObjects);
        }
        return cachedObjects;
    }

    @Override
    public List getObjects(Class clazz) {
        return relationObjectsMap.get(clazz);
    }

    public void process(List rows, Relationship[] relationships) throws SQLException {
        catchObjects(baseClass, rows);

        for(Relationship relationship:relationships)
            relationship.createProcessor().process(this, relationship);
    }

    private void setupAssociatedObjects(Class baseClass, Relationship relationship, Relationship[] relationships) throws SQLException {
        RelationProcessor relationProcessor = relationship.createProcessor();

        final Class childClass = relationship.getRelatedClass();
        Relationship childRelationship = (Relationship) Arrays.stream(relationships)
                .filter(r -> r.getBaseClass().equals(childClass)).toArray()[0];
        if(childRelationship != null)
            setupAssociatedObjects(childClass, childRelationship, relationships);
    }

    protected List queryObjects(Class clazz, String associatedColumnName,
                                Object[] associatedValues, String condition) throws SQLException {
        String relationTableName = Table.getTableName(clazz);

        SQLExecutor sqlExecutor = Database.getSqlExecutor();
        SQLGenerator sqlGenerator = Database.getSQLGenerator();

        String relationConditions = StringUtil.isBlank(condition)
                ? String.format(" %s IN (%s) ", associatedColumnName, quote(associatedValues))
                : String.format(" %s IN (%s) AND (%s)", associatedColumnName, quote(associatedValues),
                    condition);
        String relationTableQuerySql = sqlGenerator.createQuerySQL(relationTableName, null, relationConditions);

        return sqlExecutor.query(connection, relationTableQuerySql, clazz);
    }

    protected void catchObjects(Class clazz, List objects) {
        this.relationObjectsMap.put(clazz, objects);
    }

    private Relationship getRelation(Class clazz, Relationship[] relationships) {
        Relationship[] filteredRelations = Arrays.stream(relationships)
                .filter(r -> r.getBaseClass().equals(clazz))
                .collect(Collectors.toList()).toArray(new Relationship[]{});
        if(filteredRelations.length > 0)
            return filteredRelations[0];
        return null;
    }

    private String quote(Object... values) {
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

    private String encodeGroupKey(Class clazz, String fieldName) {
        return String.format("%s_%s", clazz.getName(), fieldName);
    }
}
