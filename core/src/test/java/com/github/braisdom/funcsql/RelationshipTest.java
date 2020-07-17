package com.github.braisdom.funcsql;

import com.github.braisdom.funcsql.annotations.DomainModel;
import com.github.braisdom.funcsql.annotations.Relation;
import com.github.braisdom.funcsql.relation.RelationType;
import com.github.braisdom.funcsql.relation.Relationship;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RelationshipTest {

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
        Relationship relationship = Relationship
                .createRelation(TestDomainModel.class, "testRelativeModels");
        Assertions.assertEquals(relationship.getPrimaryKey(), "id");
    }

    @Test
    public void testGivenPrimaryKey() {
        Relationship relationship = Relationship
                .createRelation(TestDomainModel.class, "testRelativeModels2");
        Assertions.assertEquals(relationship.getPrimaryKey(), "test_id");
    }

    @Test
    public void testHasAnyDefaultForeignKey() {
        Relationship relationship = Relationship
                .createRelation(TestDomainModel.class, "testRelativeModels");
        Assertions.assertEquals(relationship.getForeignKey(), "test_domain_model_id");
    }

    @Test
    public void testHasAnyDefaultForeignFieldName() {
        Relationship relationship = Relationship
                .createRelation(TestDomainModel.class, "testRelativeModels");
        Assertions.assertEquals(relationship.getForeignFieldName(), "testDomainModel");
    }

    @Test
    public void testGivenForeignKey() {
        Relationship relationship = Relationship
                .createRelation(TestDomainModel.class, "testRelativeModels2");
        Assertions.assertEquals(relationship.getForeignKey(), "given_foreign_id");
    }

    @Test
    public void testBelongsToDefaultForeignKey() {
        Relationship relationship = Relationship
                .createRelation(TestRelativeModel.class, "testDomainModel");
        Assertions.assertEquals(relationship.getForeignKey(), "test_domain_model_id");
    }
}
