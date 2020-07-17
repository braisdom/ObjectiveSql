package com.github.braisdom.funcsql.relation;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelationshipNetwork {

    private final Connection connection;
    private final Class baseClass;

    private final Map<String, List> relationObjectsMap;
    private final Map<String, List> groupedObjectsMap;

    private class NetworkNode {
        private Class baseClass;
        private Class relatedClass;
        private String relationType;
        private Map<Object, List> arrangeRows;

        public boolean equals(Object other) {
            if (other instanceof NetworkNode) {
                NetworkNode otherNode = (NetworkNode) other;
                return baseClass.equals(otherNode.baseClass) && relatedClass.equals(otherNode.relatedClass);
            } else
                return false;
        }
    }

    public RelationshipNetwork(Connection connection, Class baseClass) {
        this.connection = connection;
        this.baseClass = baseClass;

        this.relationObjectsMap = new HashMap<>();
        this.groupedObjectsMap = new HashMap<>();
    }

    public List process(List rows, Relationship[] relationships) {
        for(Relationship relationship : relationships) {

        }
        return null;
    }
}
