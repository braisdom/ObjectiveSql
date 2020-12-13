package com.github.braisdom.objsql.apt;

import com.github.braisdom.objsql.Persistence;
import com.github.braisdom.objsql.Query;
import com.github.braisdom.objsql.annotations.DomainModel;
import com.github.braisdom.objsql.pagination.Page;
import com.github.braisdom.objsql.pagination.PagedList;
import com.github.braisdom.objsql.relation.Relationship;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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
    public void testPersistenceMethod() throws NoSuchMethodException {
        Method createPersistenceMethod = TestClass.class.getMethod("createPersistence");
        Assert.assertNotNull(createPersistenceMethod);
        Assert.assertEquals(Persistence.class, createPersistenceMethod.getReturnType());

        Method saveMethod = TestClass.class.getMethod("save", boolean.class);
        Assert.assertNotNull(saveMethod);
        Assert.assertEquals(TestClass.class, saveMethod.getReturnType());
        Assert.assertEquals(1, saveMethod.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, saveMethod.getExceptionTypes()[0]);

        Method createMethod = TestClass.class.getMethod("create", TestClass.class, boolean.class);
        Assert.assertNotNull(createMethod);
        Assert.assertEquals(TestClass.class, createMethod.getReturnType());
        Assert.assertEquals(1, createMethod.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, createMethod.getExceptionTypes()[0]);

        Method createArrayMethod = TestClass.class.getMethod("create", TestClass[].class, boolean.class);
        Assert.assertNotNull(createArrayMethod);
        Assert.assertEquals(int[].class, createArrayMethod.getReturnType());
        Assert.assertEquals(1, createArrayMethod.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, createArrayMethod.getExceptionTypes()[0]);

        Method updateMethod = TestClass.class.getMethod("update", Long.class, TestClass.class, boolean.class);
        Assert.assertNotNull(updateMethod);
        Assert.assertEquals(TestClass.class, updateMethod.getReturnType());
        Assert.assertEquals(1, updateMethod.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, updateMethod.getExceptionTypes()[0]);

        Method update2Method = TestClass.class.getMethod("update", String.class, String.class, Object[].class);
        Assert.assertNotNull(update2Method);
        Assert.assertEquals(int.class, update2Method.getReturnType());
        Assert.assertEquals(1, update2Method.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, update2Method.getExceptionTypes()[0]);

        Method destroyMethod = TestClass.class.getMethod("destroy", Long.class);
        Assert.assertNotNull(destroyMethod);
        Assert.assertEquals(int.class, destroyMethod.getReturnType());
        Assert.assertEquals(1, destroyMethod.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, destroyMethod.getExceptionTypes()[0]);

        Method destroy2Method = TestClass.class.getMethod("destroy", String.class, Object[].class);
        Assert.assertNotNull(destroy2Method);
        Assert.assertEquals(int.class, destroy2Method.getReturnType());
        Assert.assertEquals(1, destroy2Method.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, destroy2Method.getExceptionTypes()[0]);

        Method executeMethod = TestClass.class.getMethod("execute", String.class, Object[].class);
        Assert.assertNotNull(executeMethod);
        Assert.assertEquals(int.class, executeMethod.getReturnType());
        Assert.assertEquals(1, executeMethod.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, executeMethod.getExceptionTypes()[0]);
    }

    @Test
    public void testQueryMethod() throws NoSuchMethodException {
        Method createQueryMethod = TestClass.class.getMethod("createQuery");
        Assert.assertNotNull(createQueryMethod);
        Assert.assertEquals(Query.class, createQueryMethod.getReturnType());

        Method queryMethod = TestClass.class.getMethod("query", String.class, Object[].class);
        Assert.assertNotNull(queryMethod);
        Assert.assertEquals(List.class, queryMethod.getReturnType());
        Assert.assertEquals(1, queryMethod.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, queryMethod.getExceptionTypes()[0]);

        Method query2Method = TestClass.class.getMethod("query", String.class, Relationship[].class, Object[].class);
        Assert.assertNotNull(query2Method);
        Assert.assertEquals(List.class, query2Method.getReturnType());
        Assert.assertEquals(1, query2Method.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, query2Method.getExceptionTypes()[0]);

        Method queryFirstMethod = TestClass.class.getMethod("queryFirst", String.class, Relationship[].class, Object[].class);
        Assert.assertNotNull(queryFirstMethod);
        Assert.assertEquals(TestClass.class, queryFirstMethod.getReturnType());
        Assert.assertEquals(1, queryFirstMethod.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, queryFirstMethod.getExceptionTypes()[0]);

        Method queryFirst2Method = TestClass.class.getMethod("queryFirst", String.class, Object[].class);
        Assert.assertNotNull(queryFirst2Method);
        Assert.assertEquals(TestClass.class, queryFirst2Method.getReturnType());
        Assert.assertEquals(1, queryFirst2Method.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, queryFirst2Method.getExceptionTypes()[0]);

        Method queryAllMethod = TestClass.class.getMethod("queryAll", Relationship[].class);
        Assert.assertNotNull(queryAllMethod);
        Assert.assertEquals(List.class, queryAllMethod.getReturnType());
        Assert.assertEquals(1, queryAllMethod.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, queryAllMethod.getExceptionTypes()[0]);

        Method countMethod = TestClass.class.getMethod("count", String.class, Object[].class);
        Assert.assertNotNull(countMethod);
        Assert.assertEquals(long.class, countMethod.getReturnType());
        Assert.assertEquals(1, countMethod.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, countMethod.getExceptionTypes()[0]);

        Method countAllMethod = TestClass.class.getMethod("countAll");
        Assert.assertNotNull(countAllMethod);
        Assert.assertEquals(long.class, countAllMethod.getReturnType());
        Assert.assertEquals(1, countAllMethod.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, countAllMethod.getExceptionTypes()[0]);

        Method pagedQueryMethod = TestClass.class.getMethod("pagedQuery", Page.class, String.class, Object[].class);
        Assert.assertNotNull(pagedQueryMethod);
        Assert.assertEquals(PagedList.class, pagedQueryMethod.getReturnType());
        Assert.assertEquals(1, pagedQueryMethod.getExceptionTypes().length);
        Assert.assertEquals(SQLException.class, pagedQueryMethod.getExceptionTypes()[0]);
    }

    public void testOthers() {
        
    }

    @DomainModel
    private static class TestClass {
        private String name;
    }

    @DomainModel(primaryClass = Integer.class, primaryFieldName = "id2")
    private static class TestClass2 {
    }
}
