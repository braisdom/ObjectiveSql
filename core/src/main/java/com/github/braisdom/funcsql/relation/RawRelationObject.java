package com.github.braisdom.funcsql.relation;

final class RawRelationObject {

    private final Relationship relationship;
    public final Object row;

    public RawRelationObject(Relationship relationship, Object row) {
        this.relationship = relationship;
        this.row = row;
    }

//    public Object getValue() {
//        String fieldName = relationship.getPrimaryName();
//        try {
//            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(row.getClass(), fieldName);
//            return propertyDescriptor.getReadMethod().invoke(row);
//        } catch (IllegalAccessException e) {
//            throw new RelationException(StringUtil.encodeExceptionMessage(e,
//                    String.format("Read %s access error", fieldName)), e);
//        } catch (InvocationTargetException e) {
//            throw new RelationException(StringUtil.encodeExceptionMessage(e,
//                    String.format("Read %s invocation error", fieldName)), e);
//        }
//    }
//
//    public void setRelations(Relationship relationship, List<RawRelationObject> rawRelationObjects) {
//        String fieldName = relationship.getFieldName();
//
//        if (RelationType.HAS_ONE.equals(relationship.getRelationType()) || RelationType.BELONGS_TO.equals(relationship.getRelationType())) {
//            if (rawRelationObjects.size() > 1)
//                throw new RelationException(String.format("The %s has too many relations", relationship.getPrimaryName()));
//
//            if (rawRelationObjects.size() == 1) {
//                invokeWriteMethod(relationship.getBaseClass(), fieldName, rawRelationObjects.get(0).getRow());
//            }
//        } else {
//            List rows = rawRelationObjects.stream().map(o -> o.getRow()).collect(Collectors.toList());
//            invokeWriteMethod(relationship.getBaseClass(), fieldName, rows);
//        }
//    }
//
//    private void invokeWriteMethod(Class clazz, String fieldName, Object value) {
//        try {
//            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(clazz, fieldName);
//            propertyDescriptor.getWriteMethod().invoke(row, value);
//        } catch (IllegalAccessException e) {
//            throw new RelationException(StringUtil.encodeExceptionMessage(e,
//                    String.format("Read %s access error", fieldName)), e);
//        } catch (InvocationTargetException e) {
//            throw new RelationException(StringUtil.encodeExceptionMessage(e,
//                    String.format("Read %s invocation error", fieldName)), e);
//        } catch (Exception e) {
//            throw new RelationException(StringUtil.encodeExceptionMessage(e,
//                    String.format("Read %s unknown error (%s)", fieldName, e.getMessage())), e);
//        }
//    }

    public Object getRow() {
        return row;
    }
}

