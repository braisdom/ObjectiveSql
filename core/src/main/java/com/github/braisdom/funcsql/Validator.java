package com.github.braisdom.funcsql;

import java.util.Map;

public interface Validator {

    class Violation {
        private String message;
        private Map<String, Object> attributes;

        public Violation(String message, Map<String, Object> attributes) {
            this.message = message;
            this.attributes = attributes;
        }

        public String getMessage() {
            return message;
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    Violation[] validate(Object bean);
}
