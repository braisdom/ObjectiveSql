/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.braisdom.objsql.relation;

import com.github.braisdom.objsql.DomainModelException;
import com.github.braisdom.objsql.Tables;
import com.github.braisdom.objsql.annotations.PrimaryKey;
import com.github.braisdom.objsql.annotations.Relation;
import com.github.braisdom.objsql.reflection.PropertyUtils;
import com.github.braisdom.objsql.util.StringUtil;
import com.github.braisdom.objsql.util.WordUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class Relationship {

    private final Class baseClass;
    private final Field relationField;
    private final Relation relation;

    public Relationship(Class baseClass, Field relationField, Relation relation) {
        Objects.requireNonNull(relationField, "The relationField cannot be null");
        Objects.requireNonNull(relation, String.format("The %s has no relation annotation",
                relationField.getName()));

        this.baseClass = baseClass;
        this.relationField = relationField;
        this.relation = relation;
    }

    public Class getBaseClass() {
        return baseClass;
    }

    public Class getRelatedClass() {
        if (Collection.class.isAssignableFrom(relationField.getType())) {
            ParameterizedType parameterizedType = (ParameterizedType) relationField.getGenericType();
            Type[] genericTypes = parameterizedType.getActualTypeArguments();

            if (genericTypes.length == 0) {
                throw new RelationalException(String.format("The %s of %s has no generic type",
                        relationField.getName(), getBaseClass().getSimpleName()));
            }

            try {
                return Class.forName((genericTypes[0]).getTypeName());
            } catch (ClassNotFoundException e) {
                throw new RelationalException(e.getMessage(), e);
            }
        } else {
            return relationField.getType();
        }
    }

    public Field getRelationField() {
        return relationField;
    }

    public String getRelationCondition() {
        return relation.condition();
    }

    public String getPrimaryKey() {
        if (StringUtil.isBlank(relation.primaryKey())) {
            if (isBelongsTo()) {
                PrimaryKey primaryKey = Tables.getPrimaryKey(getBaseClass());
                if(primaryKey == null) {
                    throw new DomainModelException(String.format("The %s has no primary key", getBaseClass().getSimpleName()));
                }
                return primaryKey.name();
            } else {
                PrimaryKey primaryKey = Tables.getPrimaryKey(getRelatedClass());
                if(primaryKey == null) {
                    throw new DomainModelException(String.format("The %s has no primary key", getRelatedClass().getSimpleName()));
                }
                return primaryKey.name();
            }
        } else {
            return relation.primaryKey();
        }
    }

    public String getForeignKey() {
        if (StringUtil.isBlank(relation.foreignKey())) {
            if (isBelongsTo()) {
                String rawForeignKey = getRelatedClass().getSimpleName();
                return Tables.encodeDefaultKey(WordUtil.underscore(rawForeignKey));
            } else {
                String rawForeignKey = baseClass.getSimpleName();
                return Tables.encodeDefaultKey(WordUtil.underscore(rawForeignKey));
            }
        } else {
            return relation.foreignKey();
        }
    }

    public String getPrimaryAssociationFieldName() {
        if (StringUtil.isBlank(relation.primaryFieldName())) {
            if(isBelongsTo()) {
                return Tables.getPrimaryField(getRelatedClass()).getName();
            } else {
                return Tables.getPrimaryField(getBaseClass()).getName();
            }
        } else {
            return relation.primaryFieldName();
        }
    }

    public String getForeignFieldName() {
        if (StringUtil.isBlank(relation.foreignFieldName())) {
            if (isBelongsTo()) {
                String rawForeignFieldName = WordUtil.underscore(getRelatedClass().getSimpleName());
                return WordUtil.camelize(Tables.encodeDefaultKey(rawForeignFieldName), true);
            } else {
                String rawForeignFieldName = WordUtil.underscore(getBaseClass().getSimpleName());
                return WordUtil.camelize(Tables.encodeDefaultKey(rawForeignFieldName), true);
            }
        } else {
            return relation.foreignFieldName();
        }
    }

    public boolean isBelongsTo() {
        return RelationType.BELONGS_TO.equals(relation.relationType());
    }

    public RelationProcessor createProcessor() {
        if(isBelongsTo()) {
            return new BelongsToProcessor();
        }
        return new HasAnyProcessor();
    }

    public static void setRelationalObjects(Relationship relationship, Object row,
                                            String fieldName, List associatedObjects) {
        if(associatedObjects != null) {
            if (relationship.isBelongsTo()) {
                if (associatedObjects.size() > 1) {
                    throw new RelationalException(String.format("The %s[belongs_to] has too many relations", fieldName));
                }

                if (associatedObjects.size() == 1) {
                    PropertyUtils.write(row, fieldName, associatedObjects.get(0));
                } else {
                    PropertyUtils.write(row, fieldName, null);
                }
            } else {
                PropertyUtils.write(row, fieldName, associatedObjects);
            }
        }
    }

    public static final Relationship createRelation(Class baseClass, String fieldName) {
        try {
            Field field = baseClass.getDeclaredField(fieldName);
            Relation relation = field.getAnnotation(Relation.class);
            if (relation == null) {
                throw new RelationalException(String.format("The %s has not relation", field));
            }
            return new Relationship(baseClass, field, relation);
        } catch (NoSuchFieldException ex) {
            throw new RelationalException(String.format("The %s has no field '%s' (%s)", baseClass.getSimpleName(),
                    fieldName, ex.getMessage()), ex);
        }
    }
}
