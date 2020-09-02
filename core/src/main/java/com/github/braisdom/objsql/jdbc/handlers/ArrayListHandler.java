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
import com.github.braisdom.objsql.jdbc.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <code>ResultSetHandler</code> implementation that converts the
 * <code>ResultSet</code> into a <code>List</code> of <code>Object[]</code>s.
 * This class is thread safe.
 *
 * @see ResultSetHandler
 */
public class ArrayListHandler extends AbstractListHandler<Object[]> {

    /**
     * The RowProcessor implementation to use when converting rows
     * into Object[]s.
     */
    private final RowProcessor convert;

    /**
     * Creates a new instance of ArrayListHandler using a
     * <code>BasicRowProcessor</code> for conversions.
     */
    public ArrayListHandler() {
        this(ArrayHandler.ROW_PROCESSOR);
    }

    /**
     * Creates a new instance of ArrayListHandler.
     *
     * @param convert The <code>RowProcessor</code> implementation
     * to use when converting rows into Object[]s.
     */
    public ArrayListHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }


    /**
     * Convert row's columns into an <code>Object[]</code>.
     * @param rs <code>ResultSet</code> to process.
     * @return <code>Object[]</code>, never <code>null</code>.
     *
     * @throws SQLException if a database access error occurs
     * @see AbstractListHandler#handle(ResultSet)
     */
    @Override
    protected Object[] handleRow(ResultSet rs) throws SQLException {
        return this.convert.toArray(rs);
    }

}
