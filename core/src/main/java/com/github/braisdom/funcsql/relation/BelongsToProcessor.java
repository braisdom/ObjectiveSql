package com.github.braisdom.funcsql.relation;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BelongsToProcessor implements RelationProcessor {

    @Override
    public void process(Context context, Relationship relationship) throws SQLException {
        String associatedFieldName = relationship.getRelationField().getName();
        String primaryFieldName = relationship.getPrimaryAssociationFieldName();
        String primaryKey = relationship.getPrimaryKey();
        String foreignFieldName = relationship.getForeignFieldName();

        Class relatedClass = relationship.getRelatedClass();
        List baseObjects = context.getObjects(relationship.getBaseClass());

        List associatedKeys = (List) baseObjects.stream()
                .map(o -> Relationship.getFieldValue(o, foreignFieldName))
                .distinct()
                .collect(Collectors.toList());
        List rawRelatedObjects = context.queryRelatedObjects(relatedClass,
                primaryKey, associatedKeys.toArray(), relationship.getRelationCondition());
        Map<Object, List> groupedRelatedObjects = (Map<Object, List>) rawRelatedObjects
                .stream().collect(Collectors.groupingBy(o -> Relationship.getFieldValue(o, primaryFieldName)));

        baseObjects.stream().forEach(o -> {
            Object primaryValue = Relationship.getFieldValue(o, foreignFieldName);
            List relatedObjects = groupedRelatedObjects.get(primaryValue);
            Relationship.setRelationalObjects(relationship, o, associatedFieldName, relatedObjects);
        });
    }
}
