package com.opc.common.utils.bean;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BeanUtilsTest {

    public static class TestSource {
        private String name;
        private Integer age;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
    }

    public static class TestTarget {
        private String name;
        private Integer age;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Integer getAge() { return age; }
        public void setAge(Integer age) { this.age = age; }
    }

    @Test
    public void testCopyBeanProp() {
        TestSource source = new TestSource();
        source.setName("John");
        source.setAge(30);

        TestTarget target = new TestTarget();
        BeanUtils.copyBeanProp(target, source);

        assertEquals("John", target.getName());
        assertEquals(Integer.valueOf(30), target.getAge());
    }

    @Test
    public void testCopyBeanPropWithNullSource() {
        TestTarget target = new TestTarget();
        target.setName("Original");

        BeanUtils.copyBeanProp(target, null);

        assertEquals("Original", target.getName());
    }

    @Test
    public void testGetSetterMethods() {
        TestSource obj = new TestSource();
        List<Method> setters = BeanUtils.getSetterMethods(obj);

        assertNotNull(setters);
        assertTrue(setters.size() >= 2);

        boolean hasNameSetter = setters.stream()
            .anyMatch(m -> m.getName().equals("setName"));
        boolean hasAgeSetter = setters.stream()
            .anyMatch(m -> m.getName().equals("setAge"));

        assertTrue(hasNameSetter);
        assertTrue(hasAgeSetter);
    }

    @Test
    public void testGetGetterMethods() {
        TestSource obj = new TestSource();
        List<Method> getters = BeanUtils.getGetterMethods(obj);

        assertNotNull(getters);
        assertTrue(getters.size() >= 2);

        boolean hasNameGetter = getters.stream()
            .anyMatch(m -> m.getName().equals("getName"));
        boolean hasAgeGetter = getters.stream()
            .anyMatch(m -> m.getName().equals("getAge"));

        assertTrue(hasNameGetter);
        assertTrue(hasAgeGetter);
    }

    @Test
    public void testIsMethodPropEquals() {
        assertTrue(BeanUtils.isMethodPropEquals("getName", "setName"));
        assertTrue(BeanUtils.isMethodPropEquals("getAge", "setAge"));
        assertFalse(BeanUtils.isMethodPropEquals("getName", "setAge"));
        assertFalse(BeanUtils.isMethodPropEquals("getName", "getAge"));
    }

    @Test
    public void testCopyBeanPropPartial() {
        TestSource source = new TestSource();
        source.setName("John");
        // age is null

        TestTarget target = new TestTarget();
        target.setAge(25);

        BeanUtils.copyBeanProp(target, source);

        assertEquals("John", target.getName());
    }

    public static class DifferentTypesSource {
        private String value;
        public String getValue() { return value; }
        public void setValue(String value) { this.value = value; }
    }

    public static class DifferentTypesTarget {
        private Integer value;
        public Integer getValue() { return value; }
        public void setValue(Integer value) { this.value = value; }
    }
}
