package com.github.braisdom.funcsql;

public abstract class AbstractUpdate implements Update {
    protected final Class domainModelClass;

    protected String set;
    protected String filter;

    protected AbstractUpdate(Class domainModelClass) {
        this.domainModelClass = domainModelClass;
    }

    @Override
    public Update set(String set, Object... args) {
        this.set = String.format(set, args);
        return this;
    }

    @Override
    public Update where(String filter, Object... args) {
        this.filter = String.format(filter, args);
        return this;
    }
}
