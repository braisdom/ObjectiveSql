package com.github.braisdom.objsql.apt;

import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.util.Inflector;
import org.junit.Assert;
import org.junit.Test;

public class DomainModelCodeGeneratorTest {

    @Test
    public void testSetterGetter() {
        TestClass clazz = new TestClass();
        clazz.setName("test");
        Assert.assertEquals("test", clazz.getName());
        Assert.assertEquals("test", clazz.name);
    }

    @Test
    public void testCustomizedPrimary() {
        TestClass2 clazz = new TestClass2();
        clazz.setId2(1);
        Assert.assertEquals(new Integer(1), clazz.id2);
    }

    @Test
    public void testTableName() {
        Assert.assertEquals("test_classes", Inflector.getInstance().tableize("TestPerson"));
        Assert.assertEquals("test_class", TestClass.TABLE_NAME);
    }

    @DomainModel
    private static class TestClass {
        private String name;
    }

    @DomainModel(primaryClass = Integer.class, primaryFieldName = "id2")
    private static class TestClass2 {
        private String name;
    }
}
