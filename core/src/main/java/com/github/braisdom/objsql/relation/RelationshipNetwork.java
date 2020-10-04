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

import com.github.braisdom.objsql.*;
import com.github.braisdom.objsql.util.StringUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class RelationshipNetwork implements RelationProcessor.Context {

    private static final String SELECT_RELATION_STATEMENT = "SELECT * FROM %s WHERE %s";

    private final Connection connection;
    private final DomainModelDescriptor domainModelDescriptor;
    private final Map<Class, List> relationObjectsMap;

    public RelationshipNetwork(Connection connection, DomainModelDescriptor domainModelDescriptor) {
        this.connection = connection;
        this.domainModelDescriptor = domainModelDescriptor;

        this.relationObjectsMap = new HashMap<>();
    }

    @Override
    public List queryRelatedObjects(Class clazz, String associationColumn,
                                    Object[] associatedValues, String condition) throws SQLException {
        List cachedObjects = relationObjectsMap.get(clazz);
        if (cachedObjects == null) {
            cachedObjects = queryObjects(clazz, associationColumn, associatedValues, condition);
            relationObjectsMap.put(clazz, cachedObjects);
        }
        return cachedObjects;
    }

    @Override
    public List getObjects(Class clazz) {
        return relationObjectsMap.get(clazz);
    }

    public void process(List rows, Relationship[] relationships) throws SQLException {
        catchObjects(domainModelDescriptor.getDomainModelClass(), rows);

        List<Relationship> baseRelationships = Arrays.stream(relationships)
                .filter(r -> r.getBaseClass().equals(domainModelDescriptor.getDomainModelClass())).collect(Collectors.toList());

        for (Relationship relationship : baseRelationships)
            setupAssociatedObjects(relationship, new ArrayList<>(Arrays.asList(relationships)));
    }

    private void setupAssociatedObjects(Relationship relationship, List<Relationship> relationships) throws SQLException {
        RelationProcessor relationProcessor = relationship.createProcessor();
        relationProcessor.process(this, relationship);
        relationships.remove(relationship);

        final Class childClass = relationship.getRelatedClass();
        Relationship[] childRelationships = relationships.stream()
                .filter(r -> r.getBaseClass().equals(childClass)).toArray(Relationship[]::new);
        if (childRelationships.length > 0)
            setupAssociatedObjects(childRelationships[0], relationships);
    }

    protected List queryObjects(Class clazz, String associatedColumnName,
                                Object[] associatedValues, String condition) throws SQLException {
        String relationTableName = Tables.getTableName(clazz);

        SQLExecutor sqlExecutor = Databases.getSqlExecutor();
        Quoter quoter = Databases.getQuoter();

        String associatedValueString = String.join(",", quoter.quoteValues(associatedValues));
        String relationConditions = StringUtil.isBlank(condition)
                ? String.format(" %s IN (%s) ", associatedColumnName, associatedValueString)
                : String.format(" %s IN (%s) AND (%s)", associatedColumnName, associatedValueString, condition);
        String relationTableQuerySql = String.format(SELECT_RELATION_STATEMENT, relationTableName, relationConditions);

        return sqlExecutor.query(connection, relationTableQuerySql, domainModelDescriptor.getRelatedModeDescriptor(clazz));
    }

    protected void catchObjects(Class clazz, List objects) {
        this.relationObjectsMap.put(clazz, objects);
    }
}
