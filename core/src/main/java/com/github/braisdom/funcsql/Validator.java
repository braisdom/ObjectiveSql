package com.github.braisdom.funcsql;

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
