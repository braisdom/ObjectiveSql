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
package com.github.braisdom.objsql.sql.expression;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.sql.AbstractExpression;
import com.github.braisdom.objsql.sql.ExpressionContext;

public class TableExpression extends AbstractExpression {

    private final DomainModel domainModel;

    public TableExpression(Class domainModelClass) {
        this.domainModel = (DomainModel) domainModelClass.getAnnotation(DomainModel.class);
        if(this.domainModel == null)
            throw new IllegalArgumentException("The " + domainModelClass.getName() + " has no DomainModel annotation");
    }

    public TableExpression(DomainModel domainModel) {
        this.domainModel = domainModel;
    }

    @Override
    public String toSql(ExpressionContext expressionContext) {
        return expressionContext.quoteTable(domainModel.tableName());
    }
}
