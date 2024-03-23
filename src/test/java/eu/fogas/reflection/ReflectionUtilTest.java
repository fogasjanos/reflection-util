package eu.fogas.reflection;

import eu.fogas.reflection.exception.FieldNotFoundException;
import eu.fogas.reflection.exception.FieldValueCannotChangedException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class ReflectionUtilTest {

    @Test
    public void setFieldValue_shouldThrowFieldNotFoundException_whenFieldNameIsNotValid() {
        assertThrows(FieldNotFoundException.class, () ->
                ReflectionUtil.setFieldValue(new TestClass(), "blah", "blahValue"));
    }

    @Test
    public void setFieldValue_shouldSetTheFieldValue_whenFieldIsStatic() {
        final String newValue = "New Value";
        TestClass testClass = new TestClass();

        ReflectionUtil.setFieldValue(testClass, "staticField", newValue);

        assertEquals(newValue, TestClass.staticField);
    }

    @Test
    public void setFieldValue_shouldNotSetTheFieldValue_whenFieldIsFinal() {
        assertThrows(FieldValueCannotChangedException.class, () ->
                ReflectionUtil.setFieldValue(new TestClass(), "FINAL_STATIC_FIELD", "new value"));
    }

    @Test
    public void setFieldValue_shouldSetTheFieldValue_whenFieldIsNotStatic() {
        final String newValue = "New Value";
        TestClass testClass = new TestClass();

        ReflectionUtil.setFieldValue(testClass, "name", newValue);

        assertEquals(newValue, testClass.getName());
    }

    @Test
    public void getFieldValue_shouldReturnTheFieldValue_whenFieldAccessible() {
        TestClass testClass = new TestClass();

        var result = ReflectionUtil.getFieldValue(testClass, "name");

        assertEquals(testClass.getName(), result);
    }

    @Test
    public void getDeclaredField_shouldThrowFieldNotFoundException_whenFieldIsNotPresent() {
        assertThrows(FieldNotFoundException.class, () ->
                ReflectionUtil.getDeclaredField(TestClass.class, "blah"));
    }

    @Test
    public void getDeclaredField_shouldThrowNullPointerException_whenTypeIsNull() {
        assertThrows(NullPointerException.class, () ->
                ReflectionUtil.getDeclaredField(null, "blah"));
    }

    @Test
    public void getDeclaredField_shouldThrowNullPointerException_whenFieldNameIsNull() {
        assertThrows(NullPointerException.class, () ->
                ReflectionUtil.getDeclaredField(TestClass.class, null));
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
    public void getDeclaredField_shouldReturnAllFieldsEvenFromParent() throws NoSuchFieldException {
        var expected = Set.of("nonStaticField", "FINAL_STATIC_FIELD", "parentName", "name", "staticField");

        var result = ReflectionUtil.getAllFields(TestClass.class);

        var fieldNames = result.stream()
                .map(Field::getName)
                .collect(Collectors.toSet());
        assertEquals(expected, fieldNames);
    }

    @Test
    public void getDeclaredConstructors_shouldReturnDefaultConstructor_whenThereIsNoExplicitConstuctor() {
        Constructor<?>[] result = ReflectionUtil.getDeclaredConstructors(TestClass.class);

        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals(0, result[0].getParameterCount());
    }

    @Test
    public void getConstructorWithMostParameters_shouldReturnDefaultConstructor_whenThereIsNoConsturctorDeclared() {
        Constructor<?> result = ReflectionUtil.getConstructorWithMostParameters(TestClass.class);

        assertNotNull(result);
        assertEquals(0, result.getParameterCount());
    }

    @Test
    public void getConstructorWithMostParameters_shouldReturnTheFirstConstructorWithMostParameters() {
        Constructor<?> result = ReflectionUtil.getConstructorWithMostParameters(ParentTestClass.class);

        assertNotNull(result);
        assertEquals(2, result.getParameterCount());
    }

    @Test
    public void getDefaultConstructor_shouldReturnTheConstructorWithoutAnyParams() {
        Constructor<?> result = ReflectionUtil.getDefaultConstructor(ParentTestClass.class);

        assertNotNull(result);
        assertEquals(0, result.getParameterCount());
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

    @Test
    public void isFinal_shouldReturnTrue_whenClassIsFinal() {
        boolean result = ReflectionUtil.isFinal(TestFinalClass.class);

        assertTrue(result);
    }

    @Test
    public void isFinal_shouldReturnFalse_whenClassIsNotFinal() {
        boolean result = ReflectionUtil.isFinal(TestClass.class);

        assertFalse(result);
    }

    @Test
    public void isStatic_shouldReturnTrue_whenFieldIsStatic() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("FINAL_STATIC_FIELD");

        boolean result = ReflectionUtil.isStatic(field);

        assertTrue(result);
    }

    @Test
    public void isStatic_shouldReturnFalse_whenFieldIsNotStatic() throws NoSuchFieldException {
        Field field = TestClass.class.getDeclaredField("name");

        boolean result = ReflectionUtil.isStatic(field);

        assertFalse(result);
    }

    @Test
    public void isStatic_shouldReturnTrue_whenClassIsStatic() {
        boolean result = ReflectionUtil.isStatic(TestFinalClass.class);

        assertTrue(result);
    }

    @Test
    public void isStatic_shouldReturnFalse_whenClassIsNotStatic() {
        boolean result = ReflectionUtil.isStatic(TestNonStaticNonFinalInnerClass.class);

        assertFalse(result);
    }

    @Test
    public void isAbstract_shouldReturnTrue_whenClassIsAbstract() {
        boolean result = ReflectionUtil.isAbstract(TestAbstractClass.class);

        assertTrue(result);
    }

    @Test
    public void isAbstract_shouldReturnFalse_whenClassIsNotAbstract() {
        boolean result = ReflectionUtil.isAbstract(TestClass.class);

        assertFalse(result);
    }

    @Test
    public void isInterface_shouldReturnTrue_whenClassIsInterface() {
        boolean result = ReflectionUtil.isInterface(TestInterface.class);

        assertTrue(result);
    }

    @Test
    public void isInterface_shouldReturnFalse_whenClassIsNotInterface() {
        boolean result = ReflectionUtil.isInterface(TestClass.class);

        assertFalse(result);
    }

    @Test
    public void isEnum_shouldReturnTrue_whenClassIsEnum() {
        boolean result = ReflectionUtil.isEnum(TestEnum.class);

        assertTrue(result);
    }

    @Test
    public void isEnum_shouldReturnFalse_whenClassIsNotEnum() {
        boolean result = ReflectionUtil.isInterface(TestClass.class);

        assertFalse(result);
    }

    @Test
    public void isRecord_shouldReturnTrue_whenClassIsRecord() {
        boolean result = ReflectionUtil.isRecord(TestRecord.class);

        assertTrue(result);
    }

    @Test
    public void isRecord_shouldReturnFalse_whenClassIsNotRecord() {
        boolean result = ReflectionUtil.isRecord(TestClass.class);

        assertFalse(result);
    }

    @Test
    public void isSet_shouldReturnTrue_whenFieldIsSet() throws NoSuchFieldException {
        Field field = TestTypesClass.class.getDeclaredField("set");

        boolean result = ReflectionUtil.isSet(field);

        assertTrue(result);
    }

    @Test
    public void isSet_shouldReturnFalse_whenFieldIsNotSet() throws NoSuchFieldException {
        Field field = TestTypesClass.class.getDeclaredField("string");

        boolean result = ReflectionUtil.isSet(field);

        assertFalse(result);
    }

    @Test
    public void isSet_shouldReturnTrue_whenClassIsSet() {
        boolean result = ReflectionUtil.isSet(HashSet.class);

        assertTrue(result);
    }

    @Test
    public void isSet_shouldReturnFalse_whenClassIsNotSet() {
        boolean result = ReflectionUtil.isSet(TestClass.class);

        assertFalse(result);
    }


    @Test
    public void isList_shouldReturnTrue_whenFieldIsList() throws NoSuchFieldException {
        Field field = TestTypesClass.class.getDeclaredField("list");

        boolean result = ReflectionUtil.isList(field);

        assertTrue(result);
    }

    @Test
    public void isList_shouldReturnFalse_whenFieldIsNotList() throws NoSuchFieldException {
        Field field = TestTypesClass.class.getDeclaredField("string");

        boolean result = ReflectionUtil.isList(field);

        assertFalse(result);
    }

    @Test
    public void isList_shouldReturnTrue_whenClassIsList() {
        boolean result = ReflectionUtil.isList(ArrayList.class);

        assertTrue(result);
    }

    @Test
    public void isList_shouldReturnFalse_whenClassIsNotList() {
        boolean result = ReflectionUtil.isList(TestClass.class);

        assertFalse(result);
    }




    @Test
    public void isMap_shouldReturnTrue_whenFieldIsMap() throws NoSuchFieldException {
        Field field = TestTypesClass.class.getDeclaredField("map");

        boolean result = ReflectionUtil.isMap(field);

        assertTrue(result);
    }

    @Test
    public void isMap_shouldReturnFalse_whenFieldIsNotMap() throws NoSuchFieldException {
        Field field = TestTypesClass.class.getDeclaredField("string");

        boolean result = ReflectionUtil.isMap(field);

        assertFalse(result);
    }

    @Test
    public void isMap_shouldReturnTrue_whenClassIsMap() {
        boolean result = ReflectionUtil.isMap(HashMap.class);

        assertTrue(result);
    }

    @Test
    public void isMap_shouldReturnFalse_whenClassIsNotMap() {
        boolean result = ReflectionUtil.isMap(TestClass.class);

        assertFalse(result);
    }

    @Test
    public void isArray_shouldReturnTrue_whenFieldIsArray() throws NoSuchFieldException {
        Field field = TestTypesClass.class.getDeclaredField("ints");

        boolean result = ReflectionUtil.isArray(field);

        assertTrue(result);
    }

    @Test
    public void isArray_shouldReturnFalse_whenFieldIsNotArray() throws NoSuchFieldException {
        Field field = TestTypesClass.class.getDeclaredField("string");

        boolean result = ReflectionUtil.isArray(field);

        assertFalse(result);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    static class ParentTestClass {
        protected String parentName;
        protected String nonStaticField;
    }

    @Getter
    @Setter
    static class TestClass extends ParentTestClass {
        private String name;
        private static String staticField;
        private final static String FINAL_STATIC_FIELD = "originalValueOfFinalStaticField";

        @Override
        public String toString() {
            return FINAL_STATIC_FIELD;
        }
    }

    static abstract class TestAbstractClass {
        protected String abstractClassField;

        public abstract String getHello();
    }

    static final class TestFinalClass {
    }

    class TestNonStaticNonFinalInnerClass {
    }

    static class TestTypesClass {
        private String string;
        private Set<String> set;
        private List<String> list;
        private Map<String, Object> map;
        private Integer[] integers;
        private int[] ints;
    }

    interface TestInterface {
        String sayHello();
    }

    enum TestEnum {
        FIRST
    }

    record TestRecord(String name) {
    }
}