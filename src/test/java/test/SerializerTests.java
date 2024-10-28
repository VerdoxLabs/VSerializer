package test;

import de.verdox.vserializer.generic.SerializationElement;
import de.verdox.vserializer.generic.Serializer;
import model.Selectable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import util.TestInputs;

import java.util.stream.Stream;

public abstract class SerializerTest extends ContextBasedTest {

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

/*    @Test
    void testSelectionSerializerWithEmptyAsStandard(){
        final Serializer<Selectable> SELECTION_SERIALIZER =
                Serializer.Selection.create("selection", Selectable.class)
                        .empty("empty")
                        .variant("var1", Selectable.SERIALIZER, new Selectable("var1", true))
                        .variant("var2", Selectable.SERIALIZER, new Selectable("var2", false))
                ;

        SerializationElement element = SELECTION_SERIALIZER.serialize(context(), null);
        Assertions.assertEquals(context().createNull(), element);
        Assertions.assertNull(SELECTION_SERIALIZER.deserialize(element));
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
}
