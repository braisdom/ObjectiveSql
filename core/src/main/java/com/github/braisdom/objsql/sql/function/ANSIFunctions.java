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
import com.github.braisdom.objsql.sql.*;
import com.github.braisdom.objsql.sql.expression.CaseExpression;
import com.github.braisdom.objsql.sql.expression.LiteralExpression;
import com.github.braisdom.objsql.sql.expression.PlainExpression;
import com.github.braisdom.objsql.util.FunctionWithThrowable;
import com.github.braisdom.objsql.util.SuppressedException;

import java.util.Arrays;
import java.util.Objects;

@Syntax(DatabaseType.All)
public class ANSIFunctions {

    /**
     * Returns the number of rows returned by the query. You can use it as an
     * aggregate or analytic function.
     *
     * Example: SELECT count(*)
     *
     * @return the number of rows returned by the query
     */
    public static final Expression count() {
        return new SqlFunctionCall("COUNT", new PlainExpression("*"));
    }

    /**
     * Returns number of input rows for which the value of expression is not null.
     *
     * Example: SELECT count(column_name)
     *
     * @param expression
     * @return the number of rows where expression is not null
     */
    public static final Expression count(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("COUNT", expression);
    }

    /**
     * Returns number of input rows for which the value of expression is not null
     * and column value is distinct.
     *
     * Example: SELECT count(distinct column_name)
     *
     * @param expression
     * @return the number of rows where column value is distinct
     */
    public static final Expression countDistinct(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("COUNT", expression) {
            @Override
            public String toSql(ExpressionContext expressionContext)  throws SQLSyntaxException {
                try {
                    String[] expressionStrings = Arrays.stream(getExpressions())
                            .map(FunctionWithThrowable
                                    .castFunctionWithThrowable(expression -> expression.toSql(expressionContext))).toArray(String[]::new);
                    String alias = getAlias();
                    return String.format("%s(DISTINCT %s) %s", getName(), String.join(",", expressionStrings),
                            alias == null ? "" : " AS " + expressionContext.quoteColumn(alias));
                } catch (SuppressedException ex) {
                    if (ex.getCause() instanceof SQLSyntaxException)
                        throw (SQLSyntaxException) ex.getCause();
                    else throw ex;
                }
            }
        };
    }

    public static final Expression currentTimestamp() {
        return new PlainExpression("CURRENT_TIMESTAMP");
    }

    public static final Expression currentDate() {
        return new PlainExpression("CURRENT_DATE");
    }

    public static final Expression abs(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("ABS", expression);
    }

    public static final Expression abs(Integer literal) {
        return new SqlFunctionCall("ABS", new LiteralExpression(literal));
    }

    public static final Expression abs(Float literal) {
        return new SqlFunctionCall("ABS", new LiteralExpression(literal));
    }

    public static final Expression abs(Double literal) {
        return new SqlFunctionCall("ABS", new LiteralExpression(literal));
    }

    public static final Expression ceil(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("CEIL", expression);
    }

    public static final Expression ceil(Float literal) {
        return new SqlFunctionCall("ABS", new LiteralExpression(literal));
    }

    public static final Expression ceil(Double literal) {
        return new SqlFunctionCall("ABS", new LiteralExpression(literal));
    }

    public static final Expression floor(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("FLOOR", expression);
    }

    public static final Expression floor(Float literal) {
        return new SqlFunctionCall("ABS", new LiteralExpression(literal));
    }

    public static final Expression floor(Double literal) {
        return new SqlFunctionCall("ABS", new LiteralExpression(literal));
    }

    public static final Expression sin(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("SIN", expression);
    }

    public static final Expression sin(Float literal) {
        return new SqlFunctionCall("ABS", new LiteralExpression(literal));
    }

    public static final Expression sin(Double literal) {
        return new SqlFunctionCall("ABS", new LiteralExpression(literal));
    }

    public static final Expression tan(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("TAN", expression);
    }

    public static final Expression tan(Float literal) {
        return new SqlFunctionCall("ABS", new LiteralExpression(literal));
    }

    public static final Expression tan(Double literal) {
        return new SqlFunctionCall("ABS", new LiteralExpression(literal));
    }

    public static final Expression cos(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("COS", expression);
    }

    public static final Expression cos(Float literal) {
        return new SqlFunctionCall("ABS", new LiteralExpression(literal));
    }

    public static final Expression cos(Double literal) {
        return new SqlFunctionCall("ABS", new LiteralExpression(literal));
    }

    @Syntax(except = DatabaseType.SQLite)
    public static final Expression mod(Expression expression1, Expression expression2) {
        Objects.requireNonNull(expression1, "The expression1 cannot be null");
        Objects.requireNonNull(expression2, "The expression2 cannot be null");
        return new SqlFunctionCall("MOD", expression1, expression2);
    }

    public static final Expression sum(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("SUM", expression);
    }

    public static final Expression avg(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("AVG", expression);
    }

    public static final Expression max(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("MAX", expression);
    }

    public static final Expression min(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("MIN", expression);
    }

    public static final Expression length(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("LENGTH", expression);
    }

    public static final Expression length(String literal) {
        return new SqlFunctionCall("LENGTH", new LiteralExpression(literal));
    }

    public static final Expression left(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("LEFT", expression);
    }

    public static final Expression left(String literal) {
        return new SqlFunctionCall("LEFT", new LiteralExpression(literal));
    }

    public static final Expression upper(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("UPPER", expression);
    }

    public static final Expression upper(String literal) {
        return new SqlFunctionCall("UPPER", new LiteralExpression(literal));
    }

    public static final Expression lower(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("LOWER", expression);
    }

    public static final Expression lower(String literal) {
        return new SqlFunctionCall("LOWER", new LiteralExpression(literal));
    }

    public static final Expression subStr(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("SUBSTR", expression);
    }

    public static final Expression subStr(String literal) {
        return new SqlFunctionCall("SUBSTR", new LiteralExpression(literal));
    }

    public static final Expression concat(Expression... expressions) throws SQLSyntaxException {
        Objects.requireNonNull(expressions, "The expressions cannot be null");
        if(expressions.length == 0)
            throw new SQLSyntaxException("The expressions cannot be empty");
        return new SqlFunctionCall("CONCAT", expressions);
    }

    public static final Expression trim(Expression... expressions) throws SQLSyntaxException {
        Objects.requireNonNull(expressions, "The expressions cannot be null");
        if(expressions.length == 0)
            throw new SQLSyntaxException("The expressions cannot be empty");
        return new SqlFunctionCall("TRIM", expressions);
    }

    public static final Expression rtrim(Expression... expressions) throws SQLSyntaxException {
        Objects.requireNonNull(expressions, "The expressions cannot be null");
        if(expressions.length == 0)
            throw new SQLSyntaxException("The expressions cannot be empty");
        return new SqlFunctionCall("RTRIM", expressions);
    }

    public static final Expression ltrim(Expression... expressions) throws SQLSyntaxException {
        Objects.requireNonNull(expressions, "The expressions cannot be null");
        if(expressions.length == 0)
            throw new SQLSyntaxException("The expressions cannot be empty");
        return new SqlFunctionCall("LTRIM", expressions);
    }

    public static final Expression sqlIf(Expression expression, Expression expression1, Expression expression2) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        Objects.requireNonNull(expression1, "The expression1 cannot be null");
        Objects.requireNonNull(expression2, "The expression2 cannot be null");

        return new SqlFunctionCall("IF", expression, expression1, expression2);
    }

    public static final CaseExpression sqlCase() {
        return new CaseExpression();
    }

    public static final CaseExpression sqlCase(Expression caseExpr) {
        return new CaseExpression(caseExpr);
    }

    public static Expression groupConcat(Expression... expressions) {
        return new SqlFunctionCall("group_concat", expressions);
    }

    public static Expression subStr(Expression expression, int length) {
        return new SqlFunctionCall("SUBSTR", expression, new LiteralExpression(length));
    }
}
