package test;

import org.junit.jupiter.api.DisplayName;
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
}
