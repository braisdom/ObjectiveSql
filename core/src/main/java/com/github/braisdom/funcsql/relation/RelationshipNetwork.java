package com.github.braisdom.funcsql.relation;

import com.github.braisdom.funcsql.Database;
import com.github.braisdom.funcsql.SQLExecutor;
import com.github.braisdom.funcsql.Table;
import com.github.braisdom.funcsql.util.StringUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class RelationshipNetwork implements RelationProcessor.Context {

    private static final String SELECT_RELATION_STATEMENT = "SELECT * FROM %s WHERE %s";

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
        if (cachedObjects == null) {
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

        List<Relationship> baseRelationships = Arrays.stream(relationships)
                .filter(r -> r.getBaseClass().equals(baseClass)).collect(Collectors.toList());

        for (Relationship relationship : baseRelationships)
            setupAssociatedObjects(relationship, new ArrayList<>(Arrays.asList(relationships)));
    }

    private void setupAssociatedObjects(Relationship relationship, List<Relationship> relationships) throws SQLException {
        RelationProcessor relationProcessor = relationship.createProcessor();
        relationProcessor.process(this, relationship);
        relationships.remove(relationship);

        final Class childClass = relationship.getRelatedClass();
        Relationship[] childRelationships = relationships.stream()
                .filter(r -> r.getBaseClass().equals(childClass)).toArray(Relationship[]::new);
        if (childRelationships.length > 0)
            setupAssociatedObjects(childRelationships[0], relationships);
    }

    protected List queryObjects(Class clazz, String associatedColumnName,
                                Object[] associatedValues, String condition) throws SQLException {
        String relationTableName = Table.getTableName(clazz);

        SQLExecutor sqlExecutor = Database.getSqlExecutor();

        String relationConditions = StringUtil.isBlank(condition)
                ? String.format(" %s IN (%s) ", associatedColumnName, Database.quote(associatedValues))
                : String.format(" %s IN (%s) AND (%s)", associatedColumnName, Database.quote(associatedValues),
                condition);
        String relationTableQuerySql = String.format(SELECT_RELATION_STATEMENT, relationTableName, relationConditions);

        return sqlExecutor.query(connection, relationTableQuerySql, clazz);
    }

    protected void catchObjects(Class clazz, List objects) {
        this.relationObjectsMap.put(clazz, objects);
    }
}
