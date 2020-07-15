package com.github.braisdom.funcsql;

public class Relation {

    private RelationType relationType;
    private final String fieldName;
    private Class baseClass;
    private Class relatedClass;
    private String primaryKey;
    private final String primaryName;
    private String foreignKey;
    private String condition;

    public Relation(RelationType relationType, String fieldName, Class baseClass, Class relatedClass,
                    String primaryKey, String primaryName, String foreignKey) {
        this(relationType, fieldName, baseClass, relatedClass, primaryKey, primaryName, foreignKey, null);
    }

    public Relation(RelationType relationType, String fieldName, Class baseClass, Class relatedClass,
                    String primaryKey, String primaryName, String foreignKey, String condition) {
        this.relationType = relationType;
        this.fieldName = fieldName;
        this.baseClass = baseClass;
        this.relatedClass = relatedClass;
        this.primaryKey = primaryKey;
        this.primaryName = primaryName;
        this.foreignKey = foreignKey;
        this.condition = condition;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public Class getBaseClass() {
        return baseClass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getPrimaryName() {
        return primaryName;
    }

    public Class getRelatedClass() {
        return relatedClass;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public String getCondition() {
        return condition;
    }
}
