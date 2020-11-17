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

import static com.github.braisdom.objsql.DatabaseType.MsSqlServer;
import static com.github.braisdom.objsql.DatabaseType.SQLite;
import static com.github.braisdom.objsql.sql.Expressions.literal;

@Syntax(DatabaseType.All)
public class Ansi {

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
                    if (ex.getCause() instanceof SQLSyntaxException) {
                        throw (SQLSyntaxException) ex.getCause();
                    } else {
                        throw ex;
                    }
                }
            }
        };
    }

    public static final Expression now() {
        return new PlainExpression("NOW()");
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
        return new SqlFunctionCall("CEIL", new LiteralExpression(literal));
    }

    public static final Expression ceil(Double literal) {
        return new SqlFunctionCall("CEIL", new LiteralExpression(literal));
    }

    public static final Expression floor(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("FLOOR", expression);
    }

    public static final Expression floor(Float literal) {
        return new SqlFunctionCall("FLOOR", new LiteralExpression(literal));
    }

    public static final Expression floor(Double literal) {
        return new SqlFunctionCall("FLOOR", new LiteralExpression(literal));
    }

    public static final Expression round(Expression expression, Integer decimals) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("ROUND", expression, literal(decimals));
    }

    public static final Expression round(Float literal, Integer decimals) {
        return new SqlFunctionCall("ROUND", literal(literal), literal(decimals));
    }

    public static final Expression round(Double literal, Integer decimals) {
        return new SqlFunctionCall("ROUND", literal(literal), literal(decimals));
    }

    public static final Expression sin(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("SIN", expression);
    }

    public static final Expression sin(Float literal) {
        return new SqlFunctionCall("SIN", new LiteralExpression(literal));
    }

    public static final Expression sin(Double literal) {
        return new SqlFunctionCall("SIN", new LiteralExpression(literal));
    }

    public static final Expression sin(Integer literal) {
        return new SqlFunctionCall("SIN", new LiteralExpression(literal));
    }

    public static final Expression asin(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("ASIN", expression);
    }

    public static final Expression asin(Float literal) {
        return new SqlFunctionCall("ASIN", new LiteralExpression(literal));
    }

    public static final Expression asin(Double literal) {
        return new SqlFunctionCall("ASIN", new LiteralExpression(literal));
    }

    public static final Expression asin(Integer literal) {
        return new SqlFunctionCall("ASIN", new LiteralExpression(literal));
    }

    public static final Expression tan(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("TAN", expression);
    }

    public static final Expression tan(Float literal) {
        return new SqlFunctionCall("TAN", new LiteralExpression(literal));
    }

    public static final Expression tan(Double literal) {
        return new SqlFunctionCall("TAN", new LiteralExpression(literal));
    }

    public static final Expression tan(Integer literal) {
        return new SqlFunctionCall("TAN", new LiteralExpression(literal));
    }

    public static final Expression atan(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("ATAN", expression);
    }

    public static final Expression atan(Float literal) {
        return new SqlFunctionCall("ATAN", new LiteralExpression(literal));
    }

    public static final Expression atan(Double literal) {
        return new SqlFunctionCall("ATAN", new LiteralExpression(literal));
    }

    public static final Expression atan(Integer literal) {
        return new SqlFunctionCall("ATAN", new LiteralExpression(literal));
    }

    public static final Expression cos(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("COS", expression);
    }

    public static final Expression cos(Float literal) {
        return new SqlFunctionCall("COS", new LiteralExpression(literal));
    }

    public static final Expression cos(Double literal) {
        return new SqlFunctionCall("COS", new LiteralExpression(literal));
    }

    public static final Expression cos(Integer literal) {
        return new SqlFunctionCall("COS", new LiteralExpression(literal));
    }

    public static final Expression acos(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("ACOS", expression);
    }

    public static final Expression acos(Float literal) {
        return new SqlFunctionCall("ACOS", new LiteralExpression(literal));
    }

    public static final Expression acos(Double literal) {
        return new SqlFunctionCall("ACOS", new LiteralExpression(literal));
    }

    public static final Expression acos(Integer literal) {
        return new SqlFunctionCall("ACOS", new LiteralExpression(literal));
    }

    public static final Expression exp(Integer literal) {
        return new SqlFunctionCall("EXP", new LiteralExpression(literal));
    }

    public static final Expression exp(Float literal) {
        return new SqlFunctionCall("EXP", new LiteralExpression(literal));
    }

    public static final Expression exp(Expression expression) {
        return new SqlFunctionCall("EXP", expression);
    }

    public static final Expression ln(Integer literal) {
        return new SqlFunctionCall("LN", new LiteralExpression(literal));
    }

    public static final Expression ln(Float literal) {
        return new SqlFunctionCall("LN", new LiteralExpression(literal));
    }

    public static final Expression ln(Expression expression) {
        return new SqlFunctionCall("LN", expression);
    }

    public static final Expression log(Integer literal) {
        return new SqlFunctionCall("LOG", new LiteralExpression(literal));
    }

    public static final Expression log(Float literal) {
        return new SqlFunctionCall("LOG", new LiteralExpression(literal));
    }

    public static final Expression log(Expression expression) {
        return new SqlFunctionCall("LOG", expression);
    }

    @Syntax(except = {SQLite, MsSqlServer})
    public static final Expression log2(Integer literal) {
        return new SqlFunctionCall("LOG2", new LiteralExpression(literal));
    }

    @Syntax(except = {SQLite, MsSqlServer})
    public static final Expression log2(Float literal) {
        return new SqlFunctionCall("LOG2", new LiteralExpression(literal));
    }

    @Syntax(except = {SQLite, MsSqlServer})
    public static final Expression log2(Expression expression) {
        return new SqlFunctionCall("LOG2", expression);
    }

    public static final Expression log10(Integer literal) {
        return new SqlFunctionCall("LOG10", new LiteralExpression(literal));
    }

    public static final Expression log10(Float literal) {
        return new SqlFunctionCall("LOG10", new LiteralExpression(literal));
    }

    public static final Expression log10(Expression expression) {
        return new SqlFunctionCall("LOG10", expression);
    }

    public static final Expression power(Integer literal, Integer power) {
        return new SqlFunctionCall("POWER", new LiteralExpression(literal), new LiteralExpression(power));
    }

    public static final Expression power(Float literal, Integer power) {
        return new SqlFunctionCall("POWER", new LiteralExpression(literal), new LiteralExpression(power));
    }

    public static final Expression power(Expression expression, Integer power) {
        return new SqlFunctionCall("POWER", expression, new LiteralExpression(power));
    }

    public static final Expression radians(Integer literal) {
        return new SqlFunctionCall("RADIANS", new LiteralExpression(literal));
    }

    public static final Expression radians(Float literal) {
        return new SqlFunctionCall("RADIANS", new LiteralExpression(literal));
    }

    public static final Expression radians(Expression expression) {
        return new SqlFunctionCall("RADIANS", expression);
    }

    public static final Expression sqrt(Integer literal) {
        return new SqlFunctionCall("SQRT", new LiteralExpression(literal));
    }

    public static final Expression sqrt(Float literal) {
        return new SqlFunctionCall("SQRT", new LiteralExpression(literal));
    }

    public static final Expression sqrt(Expression expression) {
        return new SqlFunctionCall("SQRT", expression);
    }

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

    public static final Expression substr(Expression expression) {
        Objects.requireNonNull(expression, "The expression cannot be null");
        return new SqlFunctionCall("SUBSTR", expression);
    }

    public static final Expression substr(String literal) {
        return new SqlFunctionCall("SUBSTR", new LiteralExpression(literal));
    }

    public static final Expression concat(Expression... expressions) throws SQLSyntaxException {
        Objects.requireNonNull(expressions, "The expressions cannot be null");
        if(expressions.length == 0) {
            throw new SQLSyntaxException("The expressions cannot be empty");
        }
        return new SqlFunctionCall("CONCAT", expressions);
    }

    public static final Expression trim(Expression... expressions) throws SQLSyntaxException {
        Objects.requireNonNull(expressions, "The expressions cannot be null");
        if(expressions.length == 0) {
            throw new SQLSyntaxException("The expressions cannot be empty");
        }
        return new SqlFunctionCall("TRIM", expressions);
    }

    public static final Expression rtrim(Expression... expressions) throws SQLSyntaxException {
        Objects.requireNonNull(expressions, "The expressions cannot be null");
        if(expressions.length == 0) {
            throw new SQLSyntaxException("The expressions cannot be empty");
        }
        return new SqlFunctionCall("RTRIM", expressions);
    }

    public static final Expression ltrim(Expression... expressions) throws SQLSyntaxException {
        Objects.requireNonNull(expressions, "The expressions cannot be null");
        if(expressions.length == 0) {
            throw new SQLSyntaxException("The expressions cannot be empty");
        }
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

    public static final Expression cast(String str, String type) {
        return cast(new LiteralExpression(str), type);
    }

    public static final Expression cast(int num, String type) {
        return cast(new LiteralExpression(num), type);
    }

    public static final Expression cast(float floatNum, String type) {
        return cast(new LiteralExpression(floatNum), type);
    }

    public static final Expression cast(Expression expression, String type) {
        return new SqlFunctionCall("CAST", expression) {
            @Override
            public String toSql(ExpressionContext expressionContext) throws SQLSyntaxException {
                String[] expressionStrings = Arrays.stream(getExpressions())
                        .map(FunctionWithThrowable
                                .castFunctionWithThrowable(expression -> expression.toSql(expressionContext)))
                        .toArray(String[]::new);
                String alias = getAlias();
                return String.format("%s(%s AS %s) %s", getName(), expressionStrings[0], type,
                        alias == null ? "" : "AS " + expressionContext.quoteColumn(alias));
            }
        };

    }
}
