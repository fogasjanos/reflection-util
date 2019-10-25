package com.fogas.datarandomizer.core;

import com.fogas.datarandomizer.core.exception.FieldNotFoundException;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class ReflectionUtilTest {

    @Test(expected = FieldNotFoundException.class)
    public void setFieldValue_shouldThrowFieldNotFoundException_whenFieldNameIsNotValid() {
        ReflectionUtil.setFieldValue(new TestClass(), "blah", "blahValue");
    }

    @Test
    public void setFieldValue_shouldSetTheFieldValue_whenFieldIsStatic() {
        final String name = "New Value";
        TestClass testClass = new TestClass();

        ReflectionUtil.setFieldValue(testClass, "staticField", name);

        assertEquals(name, TestClass.staticField);
    }

    @Test
    public void setFieldValue_shouldSetTheFieldValue_whenFieldIsFinalAndStatic() {
        final String name = "New Value";
        TestClass testClass = new TestClass();

        ReflectionUtil.setFieldValueForced(testClass, "FINAL_STATIC_FIELD", name);
        System.out.println(TestClass.FINAL_STATIC_FIELD);
        assertEquals(name, TestClass.FINAL_STATIC_FIELD);
    }

    @Test
    public void setFieldValue_shouldSetTheFieldValue_whenFieldIsNotStatic() {
        final String name = "New Value";
        TestClass testClass = new TestClass();

        ReflectionUtil.setFieldValue(testClass, "name", name);

        assertEquals(name, testClass.getName());
    }

    @Test(expected = FieldNotFoundException.class)
    public void getDeclaredField_shouldThrowFieldNotFoundException_whenFieldIsNotPresent() {
        ReflectionUtil.getDeclaredField(TestClass.class, "blah");
    }

    @Test(expected = NullPointerException.class)
    public void getDeclaredField_shouldThrowNullPointerException_whenTypeIsNull() {
        ReflectionUtil.getDeclaredField(null, "blah");
    }

    @Test(expected = NullPointerException.class)
    public void getDeclaredField_shouldThrowNullPointerException_whenFieldNameIsNull() {
        ReflectionUtil.getDeclaredField(TestClass.class, null);
    }

    @Test
    public void getDeclaredField_shouldReturnField_whenFieldIsPresent() throws NoSuchFieldException {
        String fieldName = "name";

        Field result = ReflectionUtil.getDeclaredField(TestClass.class, fieldName);

        Field expected = TestClass.class.getDeclaredField(fieldName);
        assertEquals(expected, result);
    }

    @Test
    public void getDeclaredField_shouldReturnField_whenFieldIsPresentInParent() throws NoSuchFieldException {
        String fieldName = "parentName";

        Field result = ReflectionUtil.getDeclaredField(TestClass.class, fieldName);

        Field expected = ParentTestClass.class.getDeclaredField(fieldName);
        assertEquals(expected, result);
    }

    @Test
    public void isFinal_shouldReturnTrue_whenFieldIsFinal() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("FINAL_STATIC_FIELD");

        boolean result = ReflectionUtil.isFinal(field);

        assertTrue(result);
    }

    @Test
    public void isFinal_shouldReturnFalse_whenFieldIsNotFinal() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("name");

        boolean result = ReflectionUtil.isFinal(field);

        assertFalse(result);
    }

    @Getter
    static class ParentTestClass {
        protected String parentName;
    }

    @Getter
    @Setter
    static class TestClass extends ParentTestClass {
        private String name;
        private static String staticField;
        private final static String FINAL_STATIC_FIELD = "originalValueOfFinalStaticField";

        public String toString() {
            return FINAL_STATIC_FIELD;
        }
    }
}