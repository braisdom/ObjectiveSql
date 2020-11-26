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
package com.github.braisdom.objsql.pagination;

import com.github.braisdom.objsql.DomainModelDescriptor;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.sql.SQLException;

/**
 * It builds the relevant SQL required for paging。By default,
 * pagination SQL is based on ANSI SQL standard.
 */
public interface PagedSQLBuilder {

    String COUNT_ALIAS = "count_";

    /**
     * Builds counting SQL by given query SQL.
     *
     * @param rawSQL The original query sql.
     *
     * @return
     * @throws SQLException
     */
    default String buildCountSQL(String rawSQL) throws SQLException {
        try {
            Statement statement = CCJSqlParserUtil.parse(rawSQL);
            Select originalSelect = (Select) statement;

            Select select = new Select();
            SubSelect subSelect = new SubSelect();
            PlainSelect plainSelect = new PlainSelect();
            SelectExpressionItem expressionItem = new SelectExpressionItem();
            Function countAllFunction = new Function();

            countAllFunction.setName("COUNT");
            countAllFunction.setParameters(new ExpressionList(new Column("*")));
            expressionItem.setExpression(countAllFunction);
            expressionItem.setAlias(new Alias(COUNT_ALIAS));
            plainSelect.addSelectItems(expressionItem);

            // Raises a error from Oracle when 'AS' added before the alias
            subSelect.setAlias(new TableAlias("T"));
            subSelect.setSelectBody(originalSelect.getSelectBody());
            plainSelect.setFromItem(subSelect);
            select.setSelectBody(plainSelect);

            return select.toString();
        } catch (JSQLParserException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    /**
     * Builds paged querying SQLby given SQL.
     *
     * @param rawSQL
     * @return The original query sql.
     * @throws SQLException
     */
    default String buildQuerySQL(Page page, String rawSQL, DomainModelDescriptor modelDescriptor) throws SQLException {
        try {
            Statement statement = CCJSqlParserUtil.parse(rawSQL);
            Select originalSelect = (Select) statement;

            PlainSelect plainSelect = (PlainSelect) originalSelect.getSelectBody();
            Offset offset = new Offset();
            Fetch fetch = new Fetch();

            offset.setOffset(page.getOffset());
            offset.setOffsetParam("ROWS");
            fetch.setRowCount(page.getPageSize());

            plainSelect.setOffset(offset);
            plainSelect.setFetch(fetch);

            return originalSelect.toString();
        } catch (JSQLParserException e) {
            throw new SQLException(e.getMessage(), e);
        }
    }

    default String getCountAlias() {
        return COUNT_ALIAS;
    }
}
