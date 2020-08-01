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

import com.github.braisdom.funcsql.jdbc.ResultSetHandler;
import com.github.braisdom.funcsql.jdbc.RowProcessor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * <code>ResultSetHandler</code> implementation that converts the first
 * <code>ResultSet</code> row into a <code>Map</code>. This class is thread
 * safe.
 *
 * @see ResultSetHandler
 */
public class MapHandler implements ResultSetHandler<Map<String, Object>> {

    /**
     * The RowProcessor implementation to use when converting rows
     * into Maps.
     */
    private final RowProcessor convert;

    /**
     * Creates a new instance of MapHandler using a
     * <code>BasicRowProcessor</code> for conversion.
     */
    public MapHandler() {
        this(ArrayHandler.ROW_PROCESSOR);
    }

    /**
     * Creates a new instance of MapHandler.
     *
     * @param convert The <code>RowProcessor</code> implementation
     * to use when converting rows into Maps.
     */
    public MapHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    /**
     * Converts the first row in the <code>ResultSet</code> into a
     * <code>Map</code>.
     * @param rs <code>ResultSet</code> to process.
     * @return A <code>Map</code> with the values from the first row or
     * <code>null</code> if there are no rows in the <code>ResultSet</code>.
     *
     * @throws SQLException if a database access error occurs
     *
     * @see ResultSetHandler#handle(ResultSet)
     */
    @Override
    public Map<String, Object> handle(ResultSet rs) throws SQLException {
        return rs.next() ? this.convert.toMap(rs) : null;
    }

}
