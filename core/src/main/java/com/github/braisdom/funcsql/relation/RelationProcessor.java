package com.github.braisdom.funcsql.relation;

import java.sql.SQLException;
import java.util.List;

public interface RelationProcessor {

    interface Context {
        List queryRelatedObjects(Class clazz, String associationColumn,
                                 Object[] associatedValues, String condition) throws SQLException;

        List getObjects(Class clazz);
    }

    void process(Context context, Relationship relationship) throws SQLException;
}
