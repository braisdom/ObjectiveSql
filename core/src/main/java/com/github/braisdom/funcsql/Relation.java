package com.github.braisdom.funcsql;

public class Relation {

    private RelationType relationType;
    private Class baseClass;
    private Class relatedClass;
    private String primaryKey;
    private String foreignKey;
    private String condition;

    public Relation(RelationType relationType, Class baseClass, Class relatedClass,
                    String primaryKey, String foreignKey, String condition) {
        this.relationType = relationType;
        this.baseClass = baseClass;
        this.relatedClass = relatedClass;
        this.primaryKey = primaryKey;
        this.foreignKey = foreignKey;
        this.condition = condition;
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public Class getBaseClass() {
        return baseClass;
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
