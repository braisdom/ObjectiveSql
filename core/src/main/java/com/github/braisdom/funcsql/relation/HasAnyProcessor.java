package com.github.braisdom.funcsql.relation;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HasAnyProcessor implements RelationProcessor {

    @Override
    public void process(Context context, Relationship relationship) throws SQLException {
        String associatedFieldName = relationship.getRelationField().getName();
        String primaryFieldName = relationship.getPrimaryAssociationFieldName();
        String foreignKey = relationship.getForeignKey();
        String foreignFieldName = relationship.getForeignFieldName();

        Class relatedClass = relationship.getRelatedClass();
        List baseObjects = context.getObjects(relationship.getBaseClass());

        List associatedKeys = (List) baseObjects.stream()
                .map(o -> Relationship.getFieldValue(o, primaryFieldName))
                .distinct()
                .collect(Collectors.toList());

        List relatedObjects = context.queryRelatedObjects(relatedClass,
                foreignKey, associatedKeys.toArray(), relationship.getRelationCondition());
        Map<Object, List> groupedRelatedObjects = (Map<Object, List>) relatedObjects
                .stream().collect(Collectors.groupingBy(o -> Relationship.getFieldValue(o, foreignFieldName)));
    }

}
