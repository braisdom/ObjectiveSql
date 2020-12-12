package com.github.braisdom.objsql.apt;

import com.github.braisdom.objsql.Persistence;
import com.github.braisdom.objsql.Query;
import com.github.braisdom.objsql.annotations.DomainModel;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DomainModelCodeGeneratorTest {

    private Map<String, Method> getMethodMaps(Class clazz) {
        Method[] methods = clazz.getMethods();
        Map<String, Method> methodMap = new HashMap<>();
        for(Method method : methods) {
            methodMap.put(method.getName(), method);
        }
        return methodMap;
    }

    private Map<String, Field> getFieldMaps(Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        Map<String, Field> methodMap = new HashMap<>();
        for(Field field : fields) {
            methodMap.put(field.getName(), field);
        }
        return methodMap;
    }

    @Test
    public void testPrimaryKey() {
        Map<String, Method> methodMap = getMethodMaps(TestClass.class);
        Map<String, Field> fieldMapMap = getFieldMaps(TestClass.class);

        Map<String, Method> methodMap2 = getMethodMaps(TestClass2.class);
        Map<String, Field> fieldMapMap2 = getFieldMaps(TestClass2.class);

        Assert.assertNotNull(methodMap.get("setId"));
        Assert.assertNotNull(methodMap2.get("setId2"));
        Assert.assertNotNull(methodMap.get("getId"));
        Assert.assertNotNull(methodMap2.get("getId2"));
        Assert.assertNotNull(fieldMapMap.get("id"));
        Assert.assertNotNull(fieldMapMap2.get("id2"));

        Assert.assertEquals(TestClass.class, methodMap.get("setId").getReturnType());
        Assert.assertEquals(1, methodMap.get("setId").getParameterCount());
        Assert.assertEquals(Long.class, methodMap.get("setId").getParameters()[0].getType());

        Assert.assertEquals(TestClass2.class, methodMap2.get("setId2").getReturnType());
        Assert.assertEquals(1, methodMap2.get("setId2").getParameterCount());
        Assert.assertEquals(Integer.class, methodMap2.get("setId2").getParameters()[0].getType());

        Assert.assertEquals(Long.class, methodMap.get("getId").getReturnType());
        Assert.assertEquals(Integer.class, methodMap2.get("getId2").getReturnType());
    }

    @Test
    public void testSetterGetter() {
        Map<String, Method> methodMap = getMethodMaps(TestClass.class);

        Assert.assertNotNull(methodMap.get("getName"));
        Assert.assertNotNull(methodMap.get("setName"));

        Assert.assertEquals(TestClass.class, methodMap.get("setName").getReturnType());
        Assert.assertEquals(1, methodMap.get("setName").getParameterCount());
        Assert.assertEquals(String.class, methodMap.get("setName").getParameters()[0].getType());
    }

    @Test
    public void testTableName() {
        Assert.assertEquals("test_classes", TestClass.TABLE_NAME);
        Assert.assertEquals("test_class2s", TestClass2.TABLE_NAME);
    }

    @Test
    public void testPersistenceMethod() {
        Map<String, Method> methodMap = getMethodMaps(TestClass.class);

        Assert.assertNotNull(methodMap.get("createPersistence"));
        Assert.assertEquals(Persistence.class, methodMap.get("createPersistence").getReturnType());

        Assert.assertNotNull(methodMap.get("save"));
        Assert.assertEquals(1, methodMap.get("save").getParameterCount());
        Assert.assertEquals(boolean.class, methodMap.get("save").getParameters()[0].getType());
        Assert.assertEquals(TestClass.class, methodMap.get("save").getReturnType());
        Assert.assertEquals(1, methodMap.get("save").getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, methodMap.get("save").getExceptionTypes()[0]);

        Assert.assertNotNull(methodMap.get("create"));
        Assert.assertEquals(2, methodMap.get("create").getParameterCount());
        Assert.assertEquals(TestClass.class, methodMap.get("create").getParameters()[0].getType());
        Assert.assertEquals(boolean.class, methodMap.get("create").getParameters()[1].getType());
        Assert.assertEquals(TestClass.class, methodMap.get("create").getReturnType());
        Assert.assertEquals(1, methodMap.get("create").getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, methodMap.get("create").getExceptionTypes()[0]);
    }

    @Test
    public void testQueryMethod() {
        Map<String, Method> methodMap = getMethodMaps(TestClass.class);

        Assert.assertNotNull(methodMap.get("createQuery"));
        Assert.assertEquals(Query.class, methodMap.get("createQuery").getReturnType());
    }

    @DomainModel
    private static class TestClass {
        private String name;
    }

    @DomainModel(primaryClass = Integer.class, primaryFieldName = "id2")
    private static class TestClass2 {
    }
}
