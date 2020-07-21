package com.github.braisdom.funcsql.relation;

import java.util.List;

public class HasAnyProcessor implements RelationProcessor {

    @Override
    public void process(Context context, List rows, Relationship relationship) {
        String primaryFieldName = relationship.getPrimaryAssociationFieldName();
        String foreignKey = relationship.getForeignKey();
        String foreignFieldName = relationship.getForeignFieldName();
        Class baseClass = relationship.getBaseClass();
        Class relatedClass = relationship.getRelatedClass();

        List relatedObjects = context.getRelatedObjects(relatedClass, foreignKey);

    }

}
