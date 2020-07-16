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

        @Relation(relationType = RelationType.HAS_MANY, primaryKey = "test_id", foreignKey = "given_foreign_id")
        private List<TestRelativeModel> testRelativeModels2;
    }

    @DomainModel
    public static class TestRelativeModel {
        @Relation(relationType = RelationType.BELONGS_TO)
        private TestDomainModel testDomainModel;
    }

    @Test
    public void testDefaultPrimaryKey() {
        RelationDefinition relationDefinition = RelationDefinition
                .createRelation(TestDomainModel.class, "testRelativeModels");
        Assertions.assertEquals(relationDefinition.getPrimaryKey(), "id");
    }

    @Test
    public void testGivenPrimaryKey() {
        RelationDefinition relationDefinition = RelationDefinition
                .createRelation(TestDomainModel.class, "testRelativeModels2");
        Assertions.assertEquals(relationDefinition.getPrimaryKey(), "test_id");
    }

    @Test
    public void testHasAnyDefaultForeignKey() {
        RelationDefinition relationDefinition = RelationDefinition
                .createRelation(TestDomainModel.class, "testRelativeModels2");
        Assertions.assertEquals(relationDefinition.getForeignKey(), "test_domain_model_id");
    }

    @Test
    public void testGivenForeignKey() {
        RelationDefinition relationDefinition = RelationDefinition
                .createRelation(TestDomainModel.class, "testRelativeModels2");
        Assertions.assertEquals(relationDefinition.getForeignKey(), "given_foreign_id");
    }

    @Test
    public void testBelongsToDefaultForeignKey() {
        RelationDefinition relationDefinition = RelationDefinition
                .createRelation(TestRelativeModel.class, "testDomainModel");
        Assertions.assertEquals(relationDefinition.getForeignKey(), "test_domain_model_id");
    }
}
