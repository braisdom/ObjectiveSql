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
        Assert.assertNotNull(TestClass.class.getMethod("saveInTransaction", int.class));
    }

    @DomainModel
    private static class TestClass {

        @Transactional
        public void save() throws SQLException {

        }

        @Transactional
        public void save(int test) throws SQLException {

        }
    }
}
