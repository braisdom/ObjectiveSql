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
package com.github.braisdom.objsql.sql.function;

import com.github.braisdom.objsql.DatabaseType;
import com.github.braisdom.objsql.sql.Expression;
import com.github.braisdom.objsql.sql.SQLSyntaxException;
import com.github.braisdom.objsql.sql.SqlFunctionCall;
import com.github.braisdom.objsql.sql.Syntax;
import com.github.braisdom.objsql.sql.expression.LiteralExpression;
import com.github.braisdom.objsql.util.ArrayUtil;

import java.util.Objects;

@Syntax(only = DatabaseType.PostgreSQL, version = "all")
public class PostgreSql {

    public static final Expression concatWs(String delimiter, Expression... expressions) throws SQLSyntaxException {
        Objects.requireNonNull(expressions, "The expressions cannot be null");
        if(expressions.length == 0)
            throw new SQLSyntaxException("The expressions cannot be empty");

        return new SqlFunctionCall("concat_ws", ArrayUtil.aheadElement(Expression.class, expressions,
                new LiteralExpression(delimiter)));
    }

    public static final Expression md5(Expression expression) {
        return new SqlFunctionCall("MD5", expression);
    }

    public static final Expression md5(String literal) {
        return new SqlFunctionCall("MD5", new LiteralExpression(literal));
    }
}
