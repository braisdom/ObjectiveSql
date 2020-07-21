package com.github.braisdom.funcsql.relation;

import java.util.List;

public interface RelationProcessor {

    interface Context {
        List getRelatedObjects(Class clazz, String associationColumn);
    }

    void process(Context context, List rows, Relationship relationship);
}
