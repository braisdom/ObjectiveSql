package com.github.braisdom.objsql.apt;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.annotations.Transactional;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLException;

public class TransactionalCodeGeneratorTest {

    @Test
    public void testTransactionalMethod() throws NoSuchMethodException {
        Assert.assertNotNull(TestClass.class.getMethod("saveInTransaction"));
    }

    @DomainModel
    private static class TestClass {

        @Transactional
        public void save() throws SQLException {

        }
    }
}
