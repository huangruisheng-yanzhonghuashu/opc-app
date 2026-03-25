package com.opc.common.utils.bean;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class BeanUtilsTest
{
    public static class TestBean
    {
        private String name;
        private Integer age;
        private Boolean active;

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

        public Boolean getActive()
        {
            return active;
        }

        public void setActive(Boolean active)
        {
            this.active = active;
        }
    }

    @Test
    public void testCopyBeanProp()
    {
        TestBean source = new TestBean();
        source.setName("Test");
        source.setAge(25);
        source.setActive(true);

        TestBean target = new TestBean();
        BeanUtils.copyBeanProp(target, source);

        assertEquals("Test", target.getName());
        assertEquals(25, target.getAge());
        assertEquals(Boolean.TRUE, target.getActive());
    }

    @Test
    public void testCopyBeanPropWithNullSource()
    {
        TestBean target = new TestBean();
        target.setName("Original");

        BeanUtils.copyBeanProp(target, null);

        assertEquals("Original", target.getName());
    }

    @Test
    public void testGetSetterMethods()
    {
        TestBean bean = new TestBean();
        List<java.lang.reflect.Method> setters = BeanUtils.getSetterMethods(bean);

        assertNotNull(setters);
        assertTrue(setters.size() >= 3);

        boolean hasNameSetter = false;
        boolean hasAgeSetter = false;
        boolean hasActiveSetter = false;

        for (java.lang.reflect.Method method : setters)
        {
            if (method.getName().equals("setName"))
                hasNameSetter = true;
            if (method.getName().equals("setAge"))
                hasAgeSetter = true;
            if (method.getName().equals("setActive"))
                hasActiveSetter = true;
        }

        assertTrue(hasNameSetter);
        assertTrue(hasAgeSetter);
        assertTrue(hasActiveSetter);
    }

    @Test
    public void testGetGetterMethods()
    {
        TestBean bean = new TestBean();
        List<java.lang.reflect.Method> getters = BeanUtils.getGetterMethods(bean);

        assertNotNull(getters);
        assertTrue(getters.size() >= 3);

        boolean hasNameGetter = false;
        boolean hasAgeGetter = false;
        boolean hasActiveGetter = false;

        for (java.lang.reflect.Method method : getters)
        {
            if (method.getName().equals("getName"))
                hasNameGetter = true;
            if (method.getName().equals("getAge"))
                hasAgeGetter = true;
            if (method.getName().equals("getActive"))
                hasActiveGetter = true;
        }

        assertTrue(hasNameGetter);
        assertTrue(hasAgeGetter);
        assertTrue(hasActiveGetter);
    }

    @Test
    public void testIsMethodPropEquals()
    {
        assertTrue(BeanUtils.isMethodPropEquals("getName", "setName"));
        assertTrue(BeanUtils.isMethodPropEquals("getAge", "setAge"));
        assertFalse(BeanUtils.isMethodPropEquals("getName", "setAge"));
        assertFalse(BeanUtils.isMethodPropEquals("getName", "getAge"));
    }

    @Test
    public void testCopyBeanPropPartial()
    {
        TestBean source = new TestBean();
        source.setName("Test");
        source.setAge(25);
        source.setActive(false);

        TestBean target = new TestBean();
        target.setName("Original");
        target.setAge(30);
        target.setActive(true);

        BeanUtils.copyBeanProp(target, source);

        assertEquals("Test", target.getName());
        assertEquals(25, target.getAge());
        assertEquals(Boolean.FALSE, target.getActive());
    }
}
