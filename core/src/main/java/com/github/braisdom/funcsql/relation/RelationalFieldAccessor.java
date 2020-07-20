package com.github.braisdom.funcsql.relation;

import com.github.braisdom.funcsql.util.StringUtil;
import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.stream.Collectors;

public final class RelationalFieldAccessor {

    private final Relationship relationship;
    private final Object row;

    public RelationalFieldAccessor(Relationship relationship, Object row) {
        this.relationship = relationship;
        this.row = row;
    }

    public Object getRelationalValue() {
        String fieldName = relationship.isBelongsTo()
                ? relationship.getPrimaryFieldName() : relationship.getForeignFieldName();
        Class clazz = row.getClass();
        try {
            return PropertyUtils.getProperty(row, fieldName);
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

    public void setRelationalObjects(List<RelationalFieldAccessor> relationalObjects) {
        String fieldName = relationship.isBelongsTo()
                ? relationship.getPrimaryFieldName() : relationship.getForeignFieldName();
        if (relationship.isBelongsTo()) {
            if (relationalObjects.size() > 1)
                throw new RelationalException(String.format("The %s has too many relations", relationship.getPrimaryFieldName()));

            if (relationalObjects.size() == 1)
                invokeWriteMethod(fieldName, relationalObjects.get(0).getRow());
            else
                invokeWriteMethod(fieldName, null);
        } else {
            List rows = relationalObjects.stream().map(o -> o.getRow()).collect(Collectors.toList());
            if(rows.size() > 0)
                invokeWriteMethod(fieldName, rows.get(0));
        }
    }

    private void invokeWriteMethod(String fieldName, Object value) {
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

