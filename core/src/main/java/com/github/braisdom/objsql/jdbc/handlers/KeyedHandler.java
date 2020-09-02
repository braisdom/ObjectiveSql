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
import java.util.Map;

/**
 * <p>
 * <code>ResultSetHandler</code> implementation that returns a Map of Maps.
 * <code>ResultSet</code> rows are converted into Maps which are then stored
 * in a Map under the given key.
 * </p>
 * <p>
 * If you had a Person table with a primary key column called ID, you could
 * retrieve rows from the table like this:
 * <pre>
 * ResultSetHandler h = new KeyedHandler("id");
 * Map found = (Map) queryRunner.query("project id, name, age from person", h);
 * Map jane = (Map) found.get(new Long(1)); // jane's id is 1
 * String janesName = (String) jane.get("name");
 * Integer janesAge = (Integer) jane.get("age");
 * </pre>
 * Note that the "id" passed to KeyedHandler and "name" and "age" passed to the
 * returned Map's get() method can be in any case.  The data types returned for
 * name and age are dependent upon how your JDBC driver converts SQL column
 * types from the Person table into Java types.
 * </p>
 * <p>This class is thread safe.</p>
 *
 * @param <K> The type of the key
 * @see ResultSetHandler
 * @since DbUtils 1.1
 */
public class KeyedHandler<K> extends AbstractKeyedHandler<K, Map<String, Object>> {

    /**
     * The RowProcessor implementation to use when converting rows
     * into Objects.
     */
    protected final RowProcessor convert;

    /**
     * The column index to retrieve key values from.  Defaults to 1.
     */
    protected final int columnIndex;

    /**
     * The column name to retrieve key values from.  Either columnName or
     * columnIndex will be used but never both.
     */
    protected final String columnName;

    /**
     * Creates a new instance of KeyedHandler.  The value of the first column
     * of each row will be a key in the Map.
     */
    public KeyedHandler() {
        this(ArrayHandler.ROW_PROCESSOR, 1, null);
    }

    /**
     * Creates a new instance of KeyedHandler.  The value of the first column
     * of each row will be a key in the Map.
     *
     * @param convert The <code>RowProcessor</code> implementation
     * to use when converting rows into Maps
     */
    public KeyedHandler(RowProcessor convert) {
        this(convert, 1, null);
    }

    /**
     * Creates a new instance of KeyedHandler.
     *
     * @param columnIndex The values to use as keys in the Map are
     * retrieved from the column at this index.
     */
    public KeyedHandler(int columnIndex) {
        this(ArrayHandler.ROW_PROCESSOR, columnIndex, null);
    }

    /**
     * Creates a new instance of KeyedHandler.
     *
     * @param columnName The values to use as keys in the Map are
     * retrieved from the column with this name.
     */
    public KeyedHandler(String columnName) {
        this(ArrayHandler.ROW_PROCESSOR, 1, columnName);
    }

    /** Private Helper
     * @param convert The <code>RowProcessor</code> implementation
     * to use when converting rows into Maps
     * @param columnIndex The values to use as keys in the Map are
     * retrieved from the column at this index.
     * @param columnName The values to use as keys in the Map are
     * retrieved from the column with this name.
     */
    private KeyedHandler(RowProcessor convert, int columnIndex,
                         String columnName) {
        super();
        this.convert = convert;
        this.columnIndex = columnIndex;
        this.columnName = columnName;
    }
    /**
     * This factory method is called by <code>handle()</code> to retrieve the
     * key value from the current <code>ResultSet</code> row.  This
     * implementation returns <code>ResultSet.getObject()</code> for the
     * configured key column name or index.
     * @param rs ResultSet to create a key from
     * @return Object from the configured key column name/index
     *
     * @throws SQLException if a database access error occurs
     * @throws ClassCastException if the class datatype does not match the column type
     */
    // We assume that the user has picked the correct type to match the column
    // so getObject will return the appropriate type and the cast will succeed.
    @SuppressWarnings("unchecked")
    @Override
    protected K createKey(ResultSet rs) throws SQLException {
        return (columnName == null) ?
               (K) rs.getObject(columnIndex) :
               (K) rs.getObject(columnName);
    }

    /**
     * This factory method is called by <code>handle()</code> to store the
     * current <code>ResultSet</code> row in some object. This
     * implementation returns a <code>Map</code> with case insensitive column
     * names as keys.  Calls to <code>map.get("COL")</code> and
     * <code>map.get("col")</code> return the same value.
     * @param rs ResultSet to create a row from
     * @return Object typed Map containing column names to values
     * @throws SQLException if a database access error occurs
     */
    @Override
    protected Map<String, Object> createRow(ResultSet rs) throws SQLException {
        return this.convert.toMap(rs);
    }

}
