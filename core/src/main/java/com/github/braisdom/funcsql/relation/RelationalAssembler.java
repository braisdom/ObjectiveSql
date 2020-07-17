package com.github.braisdom.funcsql.relation;

import com.github.braisdom.funcsql.util.StringUtil;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

final class RelationalAssembler {

    private final Relationship relationship;
    private final Object row;

    public RelationalAssembler(Relationship relationship, Object row) {
        this.relationship = relationship;
        this.row = row;
    }

    public Object getRelationalValue() {
        String fieldName = relationship.isPrimaryRelation()
                ? relationship.getPrimaryFieldName() : relationship.getForeignFieldName();
        Class clazz = relationship.isPrimaryRelation() ? relationship.getBaseClass() : relationship.getRelatedClass();
        try {
            return PropertyUtils.getProperty(clazz, fieldName);
        } catch (IllegalAccessException e) {
            throw new RelationalException(StringUtil.encodeExceptionMessage(e,
                    String.format("Read %s from %s access error", fieldName, clazz.getSimpleName())), e);
        } catch (InvocationTargetException e) {
            throw new RelationalException(StringUtil.encodeExceptionMessage(e,
                    String.format("Read %s from % invocation error", fieldName, clazz.getSimpleName())), e);
        } catch (NoSuchMethodException e) {
            throw new RelationalException(StringUtil.encodeExceptionMessage(e,
                    String.format("The %s has no %s error", clazz.getSimpleName(), fieldName)), e);
        }
    }

    public void setRelations(Relationship relationship, List<RelationalAssembler> rawRelationObjects) {
        String fieldName = relationship.isPrimaryRelation()
                ? relationship.getPrimaryFieldName() : relationship.getForeignFieldName();
        if (relationship.isPrimaryRelation()) {
            if (rawRelationObjects.size() > 1)
                throw new RelationalException(String.format("The %s has too many relations", relationship.getPrimaryFieldName()));

            if (rawRelationObjects.size() == 1)
                invokeWriteMethod(relationship.getBaseClass(), fieldName, rawRelationObjects.get(0).getRow());
            else
                invokeWriteMethod(relationship.getBaseClass(), fieldName, null);
        } else {
            List rows = rawRelationObjects.stream().map(o -> o.getRow()).collect(Collectors.toList());
            invokeWriteMethod(relationship.getBaseClass(), fieldName, rows);
        }
    }

    private void invokeWriteMethod(Class clazz, String fieldName, Object value) {
        try {
            PropertyUtils.setProperty(row, fieldName, value);
        } catch (IllegalAccessException e) {
            throw new RelationalException(StringUtil.encodeExceptionMessage(e,
                    String.format("Read %s access error", fieldName)), e);
        } catch (InvocationTargetException e) {
            throw new RelationalException(StringUtil.encodeExceptionMessage(e,
                    String.format("Read %s invocation error", fieldName)), e);
        } catch (NoSuchMethodException e) {
            throw new RelationalException(StringUtil.encodeExceptionMessage(e,
                    String.format("Read %s unknown error (%s)", fieldName, e.getMessage())), e);
        }
    }

    public Object getRow() {
        return row;
    }
}

