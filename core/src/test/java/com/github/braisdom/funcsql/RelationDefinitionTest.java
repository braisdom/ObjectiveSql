package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.Relation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RelationDefinitionTest {

    @DomainModel
    public static class TestDomainModel {

        @Relation(relationType = RelationType.HAS_MANY)
        private List<TestRelativeModel> testRelativeModels;
    }

    @DomainModel
    public static class TestRelativeModel {

    }

    @Test
    public void testPrimaryKey() {
        RelationDefinition relationDefinition = RelationDefinition
                .createRelation(TestDomainModel.class, "testRelativeModels");
        Assertions.assertEquals(relationDefinition.getPrimaryKey(), "id");
    }
}
