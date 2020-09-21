package com.github.braisdom.objsql;

import java.util.Map;

public abstract class AbstractTableRow {

    private Map<String, Object> rawAttributes;

    public Map<String, Object> getRawAttributes() {
        return rawAttributes;
    }

    public void setRawAttributes(Map<String, Object> rawAttributes) {
        this.rawAttributes = rawAttributes;
    }
}
