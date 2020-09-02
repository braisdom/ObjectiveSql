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
package com.github.braisdom.objsql.jdbc.handlers;

import com.github.braisdom.objsql.jdbc.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that simplify development of <code>ResultSetHandler</code>
 * classes that convert <code>ResultSet</code> into <code>List</code>.
 *
 * @param <T> the target List generic type
 * @see ResultSetHandler
 */
public abstract class AbstractListHandler<T> implements ResultSetHandler<List<T>> {
    /**
     * Whole <code>ResultSet</code> handler. It produce <code>List</code> as
     * result. To convert individual rows into Java objects it uses
     * <code>handleRow(ResultSet)</code> method.
     *
     * @see #handleRow(ResultSet)
     * @param rs <code>ResultSet</code> to process.
     * @return a list of all rows in the result set
     * @throws SQLException error occurs
     */
    @Override
    public List<T> handle(ResultSet rs) throws SQLException {
        List<T> rows = new ArrayList<T>();
        while (rs.next()) {
            rows.add(this.handleRow(rs));
        }
        return rows;
    }

    /**
     * Row handler. Method converts current row into some Java object.
     *
     * @param rs <code>ResultSet</code> to process.
     * @return row processing result
     * @throws SQLException error occurs
     */
    protected abstract T handleRow(ResultSet rs) throws SQLException;
}
