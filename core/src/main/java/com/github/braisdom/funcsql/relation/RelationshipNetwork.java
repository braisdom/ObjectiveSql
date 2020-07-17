package com.github.braisdom.funcsql.relation;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationshipNetwork {

    private final Connection connection;
    private final Class baseClass;
    private final List rows;
    private final Relationship[] relationships;

    private final Map<String, List> relationshipMap;

    private class NetworkNode {
        private Class relatedClass;
        private String key;
        private Map<Object, List> arrangeRows;
    }

    public RelationshipNetwork(Connection connection, Class baseClass, List rows, Relationship[] relationships) {
        this.connection = connection;
        this.baseClass = baseClass;
        this.rows = rows;
        this.relationships = relationships;

        this.relationshipMap = new HashMap<>();
    }

    public List process() {
        return null;
    }
}
