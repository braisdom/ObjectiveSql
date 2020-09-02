package com.github.braisdom.objsql;

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
        if(violations.length > 0) {
            Validator.Violation violation = violations[0];
            return String.format("[%s] %s '%s'", violation.getPropertyPath(), violation.getMessage(), violation.getInvalidValue());
        }else {
            String s = getClass().getName();
            String message = violations.length > 0 ? violations[0].getMessage() : getLocalizedMessage();
            return (message != null) ? (s + ": " + message) : s;
        }
    }
}
