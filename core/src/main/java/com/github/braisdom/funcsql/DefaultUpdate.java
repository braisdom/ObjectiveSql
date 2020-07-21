package com.github.braisdom.funcsql;

public class DefaultUpdate extends AbstractUpdate {
    public DefaultUpdate(Class domainModelClass) {
        super(domainModelClass);
    }

    @Override
    public int execute() {
        return 0;
    }
}
