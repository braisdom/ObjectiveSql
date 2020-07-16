package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.util.StringUtil;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

final class RawRelationObject {

    private final RelationDefinition relationDefinition;
    public final Object row;

    public RawRelationObject(RelationDefinition relationDefinition, Object row) {
        this.relationDefinition = relationDefinition;
        this.row = row;
    }

//    public Object getValue() {
//        String fieldName = relationDefinition.getPrimaryName();
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
//    public void setRelations(RelationDefinition relationDefinition, List<RawRelationObject> rawRelationObjects) {
//        String fieldName = relationDefinition.getFieldName();
//
//        if (RelationType.HAS_ONE.equals(relationDefinition.getRelationType()) || RelationType.BELONGS_TO.equals(relationDefinition.getRelationType())) {
//            if (rawRelationObjects.size() > 1)
//                throw new RelationException(String.format("The %s has too many relations", relationDefinition.getPrimaryName()));
//
//            if (rawRelationObjects.size() == 1) {
//                invokeWriteMethod(relationDefinition.getBaseClass(), fieldName, rawRelationObjects.get(0).getRow());
//            }
//        } else {
//            List rows = rawRelationObjects.stream().map(o -> o.getRow()).collect(Collectors.toList());
//            invokeWriteMethod(relationDefinition.getBaseClass(), fieldName, rows);
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

