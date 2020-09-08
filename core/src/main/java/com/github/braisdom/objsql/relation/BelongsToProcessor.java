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

import com.github.braisdom.objsql.reflection.PropertyUtils;

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
                .map(o -> PropertyUtils.readDirectly(o, foreignFieldName))
                .distinct()
                .collect(Collectors.toList());
        List rawRelatedObjects = context.queryRelatedObjects(relatedClass,
                primaryKey, associatedKeys.toArray(), relationship.getRelationCondition());
        Map<Object, List> groupedRelatedObjects = (Map<Object, List>) rawRelatedObjects
                .stream().collect(Collectors.groupingBy(o -> PropertyUtils.readDirectly(o, primaryFieldName)));

        baseObjects.stream().forEach(o -> {
            Object primaryValue = PropertyUtils.readDirectly(o, foreignFieldName);
            List relatedObjects = groupedRelatedObjects.get(primaryValue);
            Relationship.setRelationalObjects(relationship, o, associatedFieldName, relatedObjects);
        });
    }
}
