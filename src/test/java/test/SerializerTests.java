package test;

import de.verdox.vserializer.generic.*;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import util.TestInputs;

import java.util.stream.Stream;

public abstract class SerializerTests extends ContextBasedTest {

    static Stream<TestInputs.TestInput<?>> testInputsProvider() {
        return TestInputs.TEST_INPUTS.stream();
    }

    @ParameterizedTest
    @MethodSource("testInputsProvider")
    @DisplayName("Run Primitive Serialization Test")
    void testPrimitiveSerialization(TestInputs.TestInput<?> input) {
        input.runPrimitiveTest(context());
    }

    @ParameterizedTest
    @MethodSource("testInputsProvider")
    @DisplayName("Run Serializer Test")
    void testSerializerSerialization(TestInputs.TestInput<?> input) {
        input.runSerializerTest(context());
    }

    @ParameterizedTest
    @MethodSource("testInputsProvider")
    @DisplayName("Run Array Serialization Test")
    void testArraySerialization(TestInputs.TestInput<?> input) {
        input.runArrayTest(context());
    }

    @ParameterizedTest
    @MethodSource("testInputsProvider")
    @DisplayName("Run Collection Serialization Test")
    void testCollectionSerialization(TestInputs.TestInput<?> input) {
        input.runCollectionTest(context());
    }

    @ParameterizedTest
    @MethodSource("testInputsProvider")
    @DisplayName("Run Map Serialization Test")
    void testMapSerialization(TestInputs.TestInput<?> input) {
        input.runMapTest(context());
    }

    @Test
    public void testComplexToPrimitiveSerializer(){

        Serializer<Person> PERSON_BY_NAME_SERIALIZER = SerializerBuilder.createObjectToPrimitiveSerializer("person_by_name", Person.class, Serializer.Primitive.STRING, Person::getName, s -> new Person(s, 0, Gender.MALE));

        SerializationElement element = PERSON_BY_NAME_SERIALIZER.serialize(context(), new Person("Peter", 23, Gender.MALE));
        Assertions.assertEquals(element, context().create("Peter"));

        Person deserializedPerson = PERSON_BY_NAME_SERIALIZER.deserialize(element);
        Assertions.assertEquals("Peter", deserializedPerson.getName());
        Assertions.assertEquals(0, deserializedPerson.getAge());
        Assertions.assertEquals(Gender.MALE, deserializedPerson.getGender());
        Assertions.assertNull(deserializedPerson.getJob());
    }

    @Test
    void testSelectionSerializerWithEmpty(){
        Car car = new Car();
        SerializationElement serializedCar = GroundVehicle.VEHICLE_SELECTION_SERIALIZER.serialize(context(), car);

        Motorbike motorbike = new Motorbike();
        SerializationElement serializedMotorbike = GroundVehicle.VEHICLE_SELECTION_SERIALIZER.serialize(context(), motorbike);

        Assertions.assertNotEquals(serializedCar, serializedMotorbike);

        SerializationElement serializedNull = GroundVehicle.VEHICLE_SELECTION_SERIALIZER.serialize(context(), null);
        // This test passes since car is the first variant and thus the standard one. If a null object is serialized, the standard type is chosen.
        Assertions.assertNotEquals(serializedCar, serializedNull);
    }

    @Test
    void testRecognizeByte() {
        SerializationPrimitive primitive = context().create((byte) 1);
        Assertions.assertTrue(primitive.isByte());
    }

    @Test
    void testRecognizeByteFromContainer() {
        SerializationContainer container = context().createContainer();
        SerializationPrimitive primitive = context().create((byte) 1);

        container.set("child", primitive);
        Assertions.assertTrue(container.get("child").getAsPrimitive().isByte());
    }

    @Test
    void testRecognizeShort() {
        SerializationPrimitive primitive = context().create((short) 1);
        Assertions.assertTrue(primitive.isShort());
    }

    @Test
    void testRecognizeShortFromContainer() {
        SerializationContainer container = context().createContainer();
        SerializationPrimitive primitive = context().create((short) 1);

        container.set("child", primitive);
        Assertions.assertTrue(container.get("child").getAsPrimitive().isShort());
    }

    @Test
    void testRecognizeInteger() {
        SerializationPrimitive primitive = context().create(1);
        Assertions.assertTrue(primitive.isInteger());
    }

    @Test
    void testRecognizeIntegerFromContainer() {
        SerializationContainer container = context().createContainer();
        SerializationPrimitive primitive = context().create(1);

        container.set("child", primitive);
        Assertions.assertTrue(container.get("child").getAsPrimitive().isInteger());
    }

    @Test
    void testRecognizeLong() {
        SerializationPrimitive primitive = context().create(1L);
        Assertions.assertTrue(primitive.isLong());
    }

    @Test
    void testRecognizeLongFromContainer() {
        SerializationContainer container = context().createContainer();
        SerializationPrimitive primitive = context().create(1L);

        container.set("child", primitive);
        Assertions.assertTrue(container.get("child").getAsPrimitive().isLong());
    }

    @Test
    void testRecognizeFloat() {
        SerializationPrimitive primitive = context().create(1F);
        Assertions.assertTrue(primitive.isFloat());
    }

    @Test
    void testRecognizeFloatFromContainer() {
        SerializationContainer container = context().createContainer();
        SerializationPrimitive primitive = context().create(1F);

        container.set("child", primitive);
        Assertions.assertTrue(container.get("child").getAsPrimitive().isFloat());
    }

    @Test
    void testRecognizeDouble() {
        SerializationPrimitive primitive = context().create(1D);
        Assertions.assertTrue(primitive.isDouble());
    }

    @Test
    void testRecognizeDoubleFromContainer() {
        SerializationContainer container = context().createContainer();
        SerializationPrimitive primitive = context().create(1D);

        container.set("child", primitive);
        Assertions.assertTrue(container.get("child").getAsPrimitive().isDouble());
    }
}
