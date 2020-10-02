package com.github.braisdom.objsql;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Queryable;
import com.github.braisdom.objsql.annotations.Relation;
import com.github.braisdom.objsql.relation.RelationType;
import com.github.braisdom.objsql.util.ArrayUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RelationshipTest {

    @DomainModel(primaryColumnName = "test_id")
    public static class TestDomainModel {

        @Queryable
        private String name;

        private boolean enabled;

        @Relation(relationType = RelationType.HAS_MANY)
        private List<TestRelativeModel> testRelativeModels;

        @Relation(relationType = RelationType.HAS_MANY, primaryKey = "test_id", foreignKey = "given_foreign_id")
        private List<TestRelativeModel> testRelativeModels2;
    }

    @DomainModel
    public static class TestRelativeModel {
        Integer a = 10;
        Integer[] t = new Integer[]{1, 2, 3, 4};
    }

    @Test
    public void test() {
        Integer a = 10;
        Integer[] t = new Integer[]{1, 2, 3, 4};

        Integer[] s = ArrayUtil.aheadElement(Integer.class, t, a);
        System.out.println(s);
    }
}
