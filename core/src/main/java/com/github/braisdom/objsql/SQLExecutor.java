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
package com.github.braisdom.objsql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * This class is a extension point for ObjectiveSql, who will be customized
 * for different JDBC programming.
 *
 * @param <T>
 */
public interface SQLExecutor<T> {

    List<T> query(Connection connection, String sql,
                  TableRowAdapter tableRowAdapter, Object... params) throws SQLException;

    T insert(Connection connection, String sql,
             TableRowAdapter tableRowAdapter, Object... params) throws SQLException;

    int[] insert(Connection connection, String sql,
                 TableRowAdapter tableRowAdapter, Object[][] params) throws SQLException;

    int execute(Connection connection, String sql, Object... params) throws SQLException;

}
