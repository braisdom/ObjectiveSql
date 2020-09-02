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
package com.github.braisdom.objsql.jdbc.wrappers;

import com.github.braisdom.objsql.jdbc.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.ResultSet;

/**
 * Wraps a <code>ResultSet</code> to trim strings returned by the
 * <code>getString()</code> and <code>getObject()</code> methods.
 *
 * <p>
 * Usage Example:
 * This example shows how to decorate ResultSets so processing continues as
 * normal but all Strings are trimmed before being returned from the
 * <code>ResultSet</code>.
 * </p>
 *
 * <pre>
 * ResultSet rs = // somehow get a ResultSet;
 *
 * // Substitute wrapped ResultSet with additional behavior for real ResultSet
 * rs = StringTrimmedResultSet.wrap(rs);
 *
 * // Pass wrapped ResultSet to processor
 * List list = new BasicRowProcessor().toBeanList(rs);
 * </pre>
 */
public class StringTrimmedResultSet implements InvocationHandler {

    /**
     * The factory to create proxies with.
     */
    private static final ProxyFactory factory = ProxyFactory.instance();

    /**
     * Wraps the <code>ResultSet</code> in an instance of this class.  This is
     * equivalent to:
     * <pre>
     * ProxyFactory.instance().createResultSet(new StringTrimmedResultSet(rs));
     * </pre>
     *
     * @param rs The <code>ResultSet</code> to wrap.
     * @return wrapped ResultSet
     */
    public static ResultSet wrap(ResultSet rs) {
        return factory.createResultSet(new StringTrimmedResultSet(rs));
    }

    /**
     * The wrapped result.
     */
    private final ResultSet rs;

    /**
     * Constructs a new instance of <code>StringTrimmedResultSet</code>
     * to wrap the specified <code>ResultSet</code>.
     * @param rs ResultSet to wrap
     */
    public StringTrimmedResultSet(ResultSet rs) {
        super();
        this.rs = rs;
    }

    /**
     * Intercept calls to the <code>getString()</code> and
     * <code>getObject()</code> methods and trim any Strings before they're
     * returned.
     *
     * @see InvocationHandler#invoke(Object, Method, Object[])
     * @param proxy Not used; all method calls go to the internal result set
     * @param method The method to invoke on the result set
     * @param args The arguments to pass to the result set
     * @return string trimmed result
     * @throws Throwable error
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
        throws Throwable {

        Object result = method.invoke(this.rs, args);

        if ((method.getName().equals("getObject")
            || method.getName().equals("getString"))
                && result instanceof String) {
            result = ((String) result).trim();
        }

        return result;
    }

}
