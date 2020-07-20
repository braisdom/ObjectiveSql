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

public class RelationshipNetwork {

    private final Connection connection;
    private final Class baseClass;

    private final Map<Class, List> relationObjectsMap;

    public RelationshipNetwork(Connection connection, Class baseClass) {
        this.connection = connection;
        this.baseClass = baseClass;

        this.relationObjectsMap = new HashMap<>();
    }

    public void process(List rows, Relationship[] relationships) throws SQLException {
        Relationship sourceRelationship = getRelation(baseClass, relationships);

        catchObjects(baseClass, rows);
        setupAssociatedObjects(baseClass, sourceRelationship, relationships);
    }

    private void setupAssociatedObjects(Class baseClass, Relationship relationship, Relationship[] relationships) throws SQLException {
        final String baseFieldName = relationship.getBaseFieldName();
        final String associatedFieldName = relationship.getAssociatedFieldName();

        Class childClass = relationship.getRelatedClass();
        List baseObjects = getCachedObjects(baseClass);

        List associatedValues = (List) baseObjects.stream()
                .map(r -> Relationship.getAssociatedValue(r, baseFieldName)).distinct()
                .collect(Collectors.toList());
        List associatedObjects = queryObjects(relationship.getRelatedClass(), relationship,
                baseFieldName, associatedValues.toArray());
        Map<Object, List> relationRows = (Map<Object, List>) associatedObjects.stream()
                .collect(Collectors.groupingBy(r -> Relationship.getAssociatedValue(r, associatedFieldName)));

        catchObjects(childClass, associatedObjects);

        baseObjects.forEach(o -> Relationship.setRelationalObjects(relationship, o, baseFieldName, associatedObjects));

        Relationship childRelationship = (Relationship) Arrays.stream(relationships)
                .filter(r -> r.getBaseClass().equals(childClass)).toArray()[0];
        if(childRelationship != null)
            setupAssociatedObjects(childClass, relationship, relationships);
    }

    protected List queryObjects(Class clazz, Relationship relationship, String associatedKey,
                                Object[] associatedValues) throws SQLException {
        String relationTableName = Table.getTableName(clazz);

        SQLExecutor sqlExecutor = Database.getSqlExecutor();
        SQLGenerator sqlGenerator = Database.getSQLGenerator();

        String relationConditions = StringUtil.isBlank(relationship.getRelationCondition())
                ? String.format(" %s IN (%s) ", associatedKey, quote(associatedValues))
                : String.format(" %s IN (%s) AND (%s)", associatedKey, quote(associatedValues),
                    relationship.getRelationCondition());
        String relationTableQuerySql = sqlGenerator.createQuerySQL(relationTableName, null, relationConditions);

        return sqlExecutor.query(connection, relationTableQuerySql, clazz);
    }

    protected boolean isObjectLoaded(Class clazz) {
        return this.relationObjectsMap.containsKey(clazz);
    }

    protected List getCachedObjects(Class clazz) {
        return this.relationObjectsMap.get(clazz);
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
