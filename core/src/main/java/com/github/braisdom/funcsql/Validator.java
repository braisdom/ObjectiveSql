package com.github.braisdom.funcsql;

public interface Validator {

    public static class Violation {
        private String message;
        private Object invalidValue;
        private String propertyPath;

        public Violation(String message, Object invalidValue, String propertyPath) {
            this.message = message;
            this.invalidValue = invalidValue;
            this.propertyPath = propertyPath;
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
