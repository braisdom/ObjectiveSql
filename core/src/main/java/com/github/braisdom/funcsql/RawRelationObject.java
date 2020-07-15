package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.RelatedTo;
import com.github.braisdom.funcsql.util.StringUtil;
import com.github.braisdom.funcsql.util.WordUtil;
import org.springframework.beans.BeanUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

final class RawRelationObject {

    private final Class baseClass;
    public final String key;
    public final Object row;

    public RawRelationObject(Class baseClass, String key, Object row) {
        this.baseClass = baseClass;
        this.key = key;
        this.row = row;
    }

    public Object getValue() {
        String fieldName = WordUtil.camelize(key, true);
        try {
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(row.getClass(), fieldName);
            return propertyDescriptor.getReadMethod().invoke(row);
        } catch (IllegalAccessException e) {
            throw new RelationException(StringUtil.encodeExceptionMessage(e,
                    String.format("Read %s access error", fieldName)), e);
        } catch (InvocationTargetException e) {
            throw new RelationException(StringUtil.encodeExceptionMessage(e,
                    String.format("Read %s invocation error", fieldName)), e);
        }
    }

    public void setRelations(Relation relation, List<RawRelationObject> rawRelationObjects) {
        String fieldName = relation.getName();

        if (RelationType.HAS_ONE.equals(relation.getRelationType()) || RelationType.BELONGS_TO.equals(relation.getRelationType())) {
            if (rawRelationObjects.size() > 1)
                throw new RelationException(String.format("The %s has too many relations", key));

            if (rawRelationObjects.size() == 1) {
                invokeWriteMethod(baseClass, fieldName, rawRelationObjects.get(0).getRow());
            }
        } else {
            List rows = rawRelationObjects.stream().map(o -> o.getRow()).collect(Collectors.toList());
            invokeWriteMethod(baseClass, fieldName, rows);
        }
    }

    private void invokeWriteMethod(Class clazz, String fieldName, Object value) {
        try {
            PropertyDescriptor propertyDescriptor = BeanUtils.getPropertyDescriptor(clazz, fieldName);
            propertyDescriptor.getWriteMethod().invoke(row, value);
        } catch (IllegalAccessException e) {
            throw new RelationException(StringUtil.encodeExceptionMessage(e,
                    String.format("Read %s access error", fieldName)), e);
        } catch (InvocationTargetException e) {
            throw new RelationException(StringUtil.encodeExceptionMessage(e,
                    String.format("Read %s invocation error", fieldName)), e);
        } catch (Exception e) {
            throw new RelationException(StringUtil.encodeExceptionMessage(e,
                    String.format("Read %s unknown error (%s)", fieldName, e.getMessage())), e);
        }
    }

    private String getRelationName(RelationType relationType, Class relationClass) {
        String relationName;

        RelatedTo[] relationTos = (RelatedTo[]) (relationClass == null ? null
                : relationClass.getAnnotationsByType(RelatedTo.class));
        Optional<RelatedTo> relatedTo = Arrays.stream(relationTos).filter(r -> r.base().equals(baseClass)).findFirst();

        if (relatedTo.isPresent())
            relationName = relatedTo.get().name();
        else {
            if (RelationType.BELONGS_TO.equals(relationType) || RelationType.HAS_ONE.equals(relationType))
                relationName = WordUtil.camelize(relationClass.getSimpleName(), true);
            else
                relationName = WordUtil.pluralize(WordUtil.camelize(relationClass.getSimpleName(), true));
        }

        return relationName;
    }

    public Object getRow() {
        return row;
    }
}

