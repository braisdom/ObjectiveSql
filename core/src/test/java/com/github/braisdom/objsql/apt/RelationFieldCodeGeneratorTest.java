package com.github.braisdom.objsql.apt;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Queryable;
import com.github.braisdom.objsql.annotations.Relation;
import com.github.braisdom.objsql.relation.RelationType;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class RelationFieldCodeGeneratorTest {

    @Test
    public void testRelationField() throws NoSuchFieldException {
        Assert.assertNotNull(TestClass.class.getDeclaredField("HAS_MANY_RELATED_CLASSES"));
        Assert.assertNotNull(TestClass.class.getDeclaredField("HAS_ONE_RELATED_CLASS"));
        Assert.assertNotNull(TestClass.class.getDeclaredField("BELONGS_TO_RELATED_CLASS2"));
    }

    @DomainModel
    private static class TestClass {
        @Queryable
        private String name;

        @Relation(relationType = RelationType.HAS_MANY)
        private List<RelatedClass> relatedClasses;

        @Relation(relationType = RelationType.HAS_ONE)
        private RelatedClass relatedClass;

        @Relation(relationType = RelationType.BELONGS_TO)
        private RelatedClass2 relatedClass2;
    }

    @DomainModel
    private static class RelatedClass {
        @Queryable
        private String name;
    }

    @DomainModel
    private static class RelatedClass2 {
        @Queryable
        private String name;
    }
}
