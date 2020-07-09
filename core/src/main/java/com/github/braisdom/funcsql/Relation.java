package com.github.braisdom.funcsql;

public interface Relation {

    String DEFAULT_PRIMARY_KEY = "id";

    SimpleQuery hasMany(Class relatedClass);

    SimpleQuery hasMany(Class relatedClass, String foreignKey, String conditions);

    SimpleQuery hasOne(Class relatedClass, String foreignKey);

    SimpleQuery hasOne(Class relatedClass, String foreignKey, String conditions);

    SimpleQuery belongsTo(Class relatedClass, String foreignKey);

    SimpleQuery belongsTo(Class relatedClass, String foreignKey, String conditions);

    SimpleQuery relatedTo(RelationDefinition relationDefinition);

}
