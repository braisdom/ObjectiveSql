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
 * <code>ResultSetHandler</code> implementation that converts a
 * <code>ResultSet</code> into a <code>List</code> of <code>Map</code>s.
 * This class is thread safe.
 *
 * @see ResultSetHandler
 */
public class MapListHandler extends AbstractListHandler<Map<String, Object>> {

    /**
     * The RowProcessor implementation to use when converting rows
     * into Maps.
     */
    private final RowProcessor convert;

    /**
     * Creates a new instance of MapListHandler using a
     * <code>BasicRowProcessor</code> for conversion.
     */
    public MapListHandler() {
        this(ArrayHandler.ROW_PROCESSOR);
    }

    /**
     * Creates a new instance of MapListHandler.
     *
     * @param convert The <code>RowProcessor</code> implementation
     * to use when converting rows into Maps.
     */
    public MapListHandler(RowProcessor convert) {
        super();
        this.convert = convert;
    }

    /**
     * Converts the <code>ResultSet</code> row into a <code>Map</code> object.
     * @param rs <code>ResultSet</code> to process.
     * @return A <code>Map</code>, never null.
     *
     * @throws SQLException if a database access error occurs
     *
     * @see AbstractListHandler#handle(ResultSet)
     */
    @Override
    protected Map<String, Object> handleRow(ResultSet rs) throws SQLException {
        return this.convert.toMap(rs);
    }

}
