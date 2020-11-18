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
package com.github.braisdom.objsql.util;

public final class StringUtil {

    public static boolean isBlank(String string) {
        return string == null || string.trim().length() == 0;
    }

    public static String encodeExceptionMessage(Exception ex, String addition) {
        return String.format("%s: %s", addition, ex.getMessage());
    }

    public static String[] splitNameOf(Class<?> type) {
        return type.getName().split("\\.");
    }

    public static String truncate(String str, int length) {
        assert length >= 3 : length;
        length = length - 3;
        if (str.length() <= length) {
            return str;
        } else {
            return str.substring(0, length) + "...";
        }
    }
}
