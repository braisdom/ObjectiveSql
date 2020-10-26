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

/**
 * It defines a extension pointer for various Log framework, such as Slf4J, Log4F, etc,
 * Application system should implement it and fill the log logic.
 */
public interface Logger {

    void debug(long elapsedTime, String sql, Object[] params);

    void info(long elapsedTime, String sql, Object[] params);

    void error(String message, Throwable throwable);
}
