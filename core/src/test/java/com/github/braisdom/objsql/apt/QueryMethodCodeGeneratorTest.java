package com.github.braisdom.objsql.apt;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Queryable;
import com.github.braisdom.objsql.relation.Relationship;
import org.junit.Assert;
import org.junit.Test;

public class QueryMethodCodeGeneratorTest {

    @Test
    public void testQueryMethod() throws NoSuchMethodException {
        Assert.assertNotNull(TestClass.class.getDeclaredMethod("queryByName", String.class, Relationship[].class));
    }

    @DomainModel
    private static class TestClass {
        @Queryable
        private String name;
    }
}
