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

public interface Validator {

    class Violation {
        private Class<?> modelClass;
        private String message;
        private Object invalidValue;
        private String propertyPath;

        public Violation(Class<?> modelClass, String message, Object invalidValue, String propertyPath) {
            this.modelClass = modelClass;
            this.message = message;
            this.invalidValue = invalidValue;
            this.propertyPath = propertyPath;
        }

        public Class<?> getModelClass() {
            return modelClass;
        }

        public String getMessage() {
            return message;
        }

        public Object getInvalidValue() {
            return invalidValue;
        }

        public String getPropertyPath() {
            return propertyPath;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    Violation[] validate(Object bean);
}
