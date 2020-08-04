package com.github.braisdom.funcsql;

public class ValidationException extends PersistenceException {

    private final Validator.Violation[] violations;

    public ValidationException(Validator.Violation[] violations) {
        this.violations = violations;
    }

    public Validator.Violation[] getViolations() {
        return violations;
    }
}
