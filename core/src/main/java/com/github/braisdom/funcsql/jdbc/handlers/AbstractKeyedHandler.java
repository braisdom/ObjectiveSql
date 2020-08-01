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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * <code>ResultSetHandler</code> implementation that returns a Map.
 * <code>ResultSet</code> rows are converted into objects (Vs) which are then stored
 * in a Map under the given keys (Ks).
 * </p>
 *
 * @param <K> the type of keys maintained by the returned map
 * @param <V> the type of mapped values
 * @see ResultSetHandler
 * @since DbUtils 1.3
 */
public abstract class AbstractKeyedHandler<K, V> implements ResultSetHandler<Map<K, V>> {


    /**
     * Convert each row's columns into a Map and store then
     * in a <code>Map</code> under <code>ResultSet.getObject(key)</code> key.
     * @param rs <code>ResultSet</code> to process.
     * @return A <code>Map</code>, never <code>null</code>.
     * @throws SQLException if a database access error occurs
     * @see ResultSetHandler#handle(ResultSet)
     */
    @Override
    public Map<K, V> handle(ResultSet rs) throws SQLException {
        Map<K, V> result = createMap();
        while (rs.next()) {
            result.put(createKey(rs), createRow(rs));
        }
        return result;
    }

    /**
     * This factory method is called by <code>handle()</code> to create the Map
     * to store records in.  This implementation returns a <code>HashMap</code>
     * instance.
     *
     * @return Map to store records in
     */
    protected Map<K, V> createMap() {
        return new HashMap<K, V>();
    }

    /**
     * This factory method is called by <code>handle()</code> to retrieve the
     * key value from the current <code>ResultSet</code> row.
     * @param rs ResultSet to create a key from
     * @return K from the configured key column name/index
     * @throws SQLException if a database access error occurs
     */
    protected abstract K createKey(ResultSet rs) throws SQLException;

    /**
     * This factory method is called by <code>handle()</code> to store the
     * current <code>ResultSet</code> row in some object.
     * @param rs ResultSet to create a row from
     * @return V object created from the current row
     * @throws SQLException if a database access error occurs
     */
    protected abstract V createRow(ResultSet rs) throws SQLException;

}
