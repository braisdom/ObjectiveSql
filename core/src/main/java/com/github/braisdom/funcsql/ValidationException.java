package com.github.braisdom.funcsql;

public class ValidationException extends PersistenceException {

    private final Validator.Violation[] violations;

    public ValidationException(Validator.Violation[] violations) {
        this.violations = violations;
    }

    public Validator.Violation[] getViolations() {
        return violations;
    }

    @Override
    public String toString() {
        String s = getClass().getName();
        String message = violations.length > 0 ? violations[0].getMessage() : getLocalizedMessage();
        return (message != null) ? (s + ": " + message) : s;
    }
}
