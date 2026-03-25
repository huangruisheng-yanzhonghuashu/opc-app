package com.opc.common.utils.reflect;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

public class ReflectUtilsTest
{
    public static class TestClass
    {
        private String name;
        private Integer age;
        public String publicField;

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public Integer getAge()
        {
            return age;
        }

        public void setAge(Integer age)
        {
            this.age = age;
        }

        public String sayHello(String greeting)
        {
            return greeting + ", " + name;
        }

        private String privateMethod()
        {
            return "private";
        }
    }

    public static class ChildClass extends TestClass
    {
        private String childField;

        public String getChildField()
        {
            return childField;
        }

        public void setChildField(String childField)
        {
            this.childField = childField;
        }
    }

    @Test
    public void testInvokeGetter()
    {
        TestClass obj = new TestClass();
        obj.setName("Test");
        obj.setAge(25);

        assertEquals("Test", ReflectUtils.invokeGetter(obj, "name"));
        assertEquals(25, (Integer) ReflectUtils.invokeGetter(obj, "age"));
    }

    @Test
    public void testInvokeSetter()
    {
        TestClass obj = new TestClass();

        ReflectUtils.invokeSetter(obj, "name", "Test");
        ReflectUtils.invokeSetter(obj, "age", 25);

        assertEquals("Test", obj.getName());
        assertEquals(25, obj.getAge());
    }

    @Test
    public void testGetFieldValue()
    {
        TestClass obj = new TestClass();
        obj.setName("Test");

        Object value = ReflectUtils.getFieldValue(obj, "name");
        assertEquals("Test", value);
    }

    @Test
    public void testGetFieldValuePrivate()
    {
        TestClass obj = new TestClass();
        ReflectUtils.setFieldValue(obj, "name", "Test");

        Object value = ReflectUtils.getFieldValue(obj, "name");
        assertEquals("Test", value);
    }

    @Test
    public void testSetFieldValue()
    {
        TestClass obj = new TestClass();

        ReflectUtils.setFieldValue(obj, "name", "Test");
        ReflectUtils.setFieldValue(obj, "age", 25);

        assertEquals("Test", obj.getName());
        assertEquals(25, obj.getAge());
    }

    @Test
    public void testInvokeMethod()
    {
        TestClass obj = new TestClass();
        obj.setName("John");

        String result = ReflectUtils.invokeMethod(obj, "sayHello", new Class<?>[] { String.class },
                new Object[] { "Hello" });

        assertEquals("Hello, John", result);
    }

    @Test
    public void testInvokeMethodByName()
    {
        TestClass obj = new TestClass();

        ReflectUtils.invokeMethodByName(obj, "setName", new Object[] { "Test" });

        assertEquals("Test", obj.getName());
    }

    @Test
    public void testGetAccessibleField()
    {
        TestClass obj = new TestClass();

        Field field = ReflectUtils.getAccessibleField(obj, "name");
        assertNotNull(field);
        assertEquals("name", field.getName());
    }

    @Test
    public void testGetAccessibleFieldFromParent()
    {
        ChildClass obj = new ChildClass();

        Field field = ReflectUtils.getAccessibleField(obj, "name");
        assertNotNull(field);
        assertEquals("name", field.getName());
    }

    @Test
    public void testGetAccessibleMethod()
    {
        TestClass obj = new TestClass();

        Method method = ReflectUtils.getAccessibleMethod(obj, "sayHello", String.class);
        assertNotNull(method);
        assertEquals("sayHello", method.getName());
    }

    @Test
    public void testGetAccessibleMethodByName()
    {
        TestClass obj = new TestClass();

        Method method = ReflectUtils.getAccessibleMethodByName(obj, "setName", 1);
        assertNotNull(method);
        assertEquals("setName", method.getName());
    }

    @Test
    public void testGetUserClass()
    {
        TestClass obj = new TestClass();

        Class<?> clazz = ReflectUtils.getUserClass(obj);
        assertEquals(TestClass.class, clazz);
    }

    @Test
    public void testGetUserClassWithNull()
    {
        assertThrows(RuntimeException.class, () -> {
            ReflectUtils.getUserClass(null);
        });
    }

    @Test
    public void testInvokeGetterWithNull()
    {
        assertNull(ReflectUtils.invokeGetter(null, "name"));
    }

    @Test
    public void testGetFieldValueNonExistent()
    {
        TestClass obj = new TestClass();

        Object value = ReflectUtils.getFieldValue(obj, "nonExistent");
        assertNull(value);
    }

    @Test
    public void testInvokeMethodWithNull()
    {
        assertNull(ReflectUtils.invokeMethod(null, "method", new Class<?>[] {}, new Object[] {}));
        assertNull(ReflectUtils.invokeMethod(new TestClass(), null, new Class<?>[] {}, new Object[] {}));
    }

    @Test
    public void testGetAccessibleFieldWithNull()
    {
        assertNull(ReflectUtils.getAccessibleField(null, "name"));
    }

    @Test
    public void testGetAccessibleMethodWithNullObj()
    {
        assertNull(ReflectUtils.getAccessibleMethod(null, "method"));
    }

    @Test
    public void testGetAccessibleMethodWithNullMethodName()
    {
        assertThrows(NullPointerException.class, () -> {
            ReflectUtils.getAccessibleMethod(new TestClass(), (String) null);
        });
    }

    @Test
    public void testGetClassGenricType()
    {
        Class<?> clazz = ReflectUtils.getClassGenricType(TestClass.class);
        assertNotNull(clazz);
    }

    @Test
    public void testConvertReflectionExceptionToUnchecked()
    {
        Exception illegalAccess = new IllegalAccessException("test");
        RuntimeException result1 = ReflectUtils.convertReflectionExceptionToUnchecked("msg", illegalAccess);
        assertTrue(result1 instanceof IllegalArgumentException);

        Exception noSuchMethod = new NoSuchMethodException("test");
        RuntimeException result2 = ReflectUtils.convertReflectionExceptionToUnchecked("msg", noSuchMethod);
        assertTrue(result2 instanceof IllegalArgumentException);

        Exception illegalArgument = new IllegalArgumentException("test");
        RuntimeException result3 = ReflectUtils.convertReflectionExceptionToUnchecked("msg", illegalArgument);
        assertTrue(result3 instanceof IllegalArgumentException);

        Exception runtime = new RuntimeException("test");
        RuntimeException result4 = ReflectUtils.convertReflectionExceptionToUnchecked("msg", runtime);
        assertTrue(result4 instanceof RuntimeException);
    }
}
