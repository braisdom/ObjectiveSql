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
package com.github.braisdom.funcsql.jdbc.handlers;

import com.github.braisdom.funcsql.jdbc.BasicRowProcessor;
import com.github.braisdom.funcsql.jdbc.ResultSetHandler;
import com.github.braisdom.funcsql.jdbc.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <code>ResultSetHandler</code> implementation that converts a
 * <code>ResultSet</code> into an <code>Object[]</code>. This class is
 * thread safe.
 *
 * @see ResultSetHandler
 */
public class ArrayHandler implements ResultSetHandler<Object[]> {

    /**
     * Singleton processor instance that handlers share to save memory.  Notice
     * the default scoping to allow only classes in this package to use this
     * instance.
     */
    static final RowProcessor ROW_PROCESSOR = new BasicRowProcessor();

    /**
     * An empty array to return when no more rows are available in the ResultSet.
     */
    private static final Object[] EMPTY_ARRAY = new Object[0];

    /**
     * The RowProcessor implementation to use when converting rows
     * into arrays.
     */
    private final RowProcessor convert;

    /**
     * Creates a new instance of ArrayHandler using a
     * <code>BasicRowProcessor</code> for conversion.
     */
    public ArrayHandler() {
        this(ROW_PROCESSOR);
    }

    /**
     * Creates a new instance of ArrayHandler.
     *
     * @param convert The <code>RowProcessor</code> implementation
     * to use when converting rows into arrays.
     */
    public ArrayHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    /**
     * Places the column values from the first row in an <code>Object[]</code>.
     * @param rs <code>ResultSet</code> to process.
     * @return An Object[]. If there are no rows in the <code>ResultSet</code>
     * an empty array will be returned.
     *
     * @throws SQLException if a database access error occurs
     * @see ResultSetHandler#handle(ResultSet)
     */
    @Override
    public Object[] handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toArray(rs) : EMPTY_ARRAY;
    }

}
