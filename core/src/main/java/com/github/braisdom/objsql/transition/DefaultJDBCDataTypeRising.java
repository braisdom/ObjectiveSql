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
package com.github.braisdom.objsql.transition;

import java.math.BigInteger;

public class DefaultJDBCDataTypeRising implements JDBCDataTypeRising {

    @Override
    public Float risingFloat(Object lower) {
        if(lower instanceof Float)
            return (Float) lower;
        if(lower instanceof Double)
            return Float.valueOf(String.valueOf(lower));
        if(lower instanceof Integer)
            return Float.valueOf(String.valueOf(lower));
        else
            throw new TransitionException(String.format("'%s' cannot convert to Float", String.valueOf(lower)));
    }

    @Override
    public Double risingDouble(Object lower) {
        if(lower instanceof Double)
            return (Double) lower;
        if(lower instanceof Float)
            return Double.valueOf(String.valueOf(lower));
        if(lower instanceof Integer)
            return Double.valueOf(String.valueOf(lower));
        else
            throw new TransitionException(String.format("'%s' cannot convert to Double", String.valueOf(lower)));
    }

    @Override
    public Short risingShort(Object lower) {
        if(lower instanceof Short)
            return (Short) lower;
        if(lower instanceof Integer)
            return Short.valueOf(String.valueOf(lower));
        else
            throw new TransitionException(String.format("'%s' cannot convert to Short", String.valueOf(lower)));
    }

    @Override
    public Integer risingInteger(Object lower) {
        if(lower instanceof Integer)
            return (Integer) lower;
        if(lower instanceof Long)
            return Integer.valueOf(String.valueOf(lower));
        if(lower instanceof BigInteger)
            return ((BigInteger)lower).intValue();
        else
            throw new TransitionException(String.format("'%s' cannot convert to Integer", String.valueOf(lower)));
    }

    @Override
    public Long risingLong(Object lower) {
        if(lower instanceof Long)
            return (Long) lower;
        if(lower instanceof Integer)
            return Long.valueOf(String.valueOf(lower));
        else
            throw new TransitionException(String.format("'%s' cannot convert to Long", String.valueOf(lower)));
    }

    @Override
    public Boolean risingBoolean(Object lower) {
        if(lower instanceof Integer)
            return ((Integer)lower) == 1;
        if(lower instanceof Short)
            return ((Short)lower) == 1;
        else
            throw new TransitionException(String.format("'%s' cannot convert to Boolean", String.valueOf(lower)));
    }

    @Override
    public <T extends Enum<T>> T risingEnum(Class<T> clazz,  Object lower) {
        return Enum.valueOf(clazz, String.valueOf(lower));
    }
}
