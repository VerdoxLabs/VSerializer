package util;

import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.Serializer;
import model.Gender;
import model.Job;
import model.Person;

import java.util.*;
import java.util.function.IntFunction;

public class TestInputs {
    public static final Set<TestInput<?>> TEST_INPUTS = new HashSet<>();

    static {
        Person hans = new Person("Hans", 28, Gender.MALE);
        Person lisa = new Person("Lisa", 20, Gender.FEMALE);
        Job job1 = new Job("Minysoft", 1000);
        Job job2 = new Job("Zanamon", 5000);

        hans.setJob(job1);

        TEST_INPUTS.add(new TestInput<>(Serializer.Primitive.BOOLEAN, true, List.of(true, false, true), Map.of("test", true), Boolean[]::new, true, false, true));
        TEST_INPUTS.add(new TestInput<>(Serializer.Primitive.BYTE, ((byte) 1), List.of(((byte) 1), ((byte) 2), ((byte) 3)), Map.of("test", ((byte) 1)), Byte[]::new, ((byte) 1), ((byte) 2), ((byte) 3)));
        TEST_INPUTS.add(new TestInput<>(Serializer.Primitive.SHORT, ((short) 1), List.of(((short) 1), ((short) 2), ((short) 3)), Map.of("test", ((short) 1)), Short[]::new, ((short) 1), ((short) 2), ((short) 3)));
        TEST_INPUTS.add(new TestInput<>(Serializer.Primitive.INTEGER, 1, List.of(1, 2, 3), Map.of("test", 1), Integer[]::new, 1, 2, 3));
        TEST_INPUTS.add(new TestInput<>(Serializer.Primitive.LONG, 1L, List.of(1L, 2L, 3L), Map.of("test", 1L), Long[]::new, 1L, 2L, 3L));
        TEST_INPUTS.add(new TestInput<>(Serializer.Primitive.FLOAT, 1F, List.of(1F, 2F, 3F), Map.of("test", 1F), Float[]::new, 1F, 2F, 3F));
        TEST_INPUTS.add(new TestInput<>(Serializer.Primitive.DOUBLE, 1D, List.of(1D, 2D, 3D), Map.of("test", 1D), Double[]::new, 1D, 2D, 3D));
        TEST_INPUTS.add(new TestInput<>(Serializer.Primitive.CHARACTER, 'a', List.of('c', '4', '#'), Map.of("test", 'e'), Character[]::new, 'c', '4', '#'));
        TEST_INPUTS.add(new TestInput<>(Serializer.Primitive.STRING, "abc", List.of("bgdadsad", "asdasddad", "gfdkgkg"), Map.of("test", "asjgigiort"), String[]::new, "bgdadsad", "asdasddad", "gfdkgkg"));
        TEST_INPUTS.add(new TestInput<>(Person.SERIALIZER, hans, List.of(hans, lisa), Map.of("hans", hans, "lisa", lisa), Person[]::new, hans, lisa));
        TEST_INPUTS.add(new TestInput<>(Job.SERIALIZER, job1, List.of(job1, job2), Map.of("job1", job1, "job2", job2), Job[]::new, job1, job2));
    }
    public record TestInput<T>(Serializer<T> serializer, T singleInput, Collection<T> collectionInput,
                                Map<String, T> mapInput, IntFunction<? extends T[]> arrayCreator, T... arrayInput) {

        public void runPrimitiveTest(SerializationContext context) {
            if (serializer instanceof Serializer.Primitive<T> primitive)
                TestUtil.testPrimitiveSerialization(context, primitive, singleInput);
        }

        public void runSerializerTest(SerializationContext context) {
            TestUtil.testSerialization(context, serializer, singleInput);
        }

        public void runArrayTest(SerializationContext context) {
            TestUtil.testArraySerialization(context, serializer, arrayInput, arrayCreator);
        }

        public void runCollectionTest(SerializationContext context) {
            TestUtil.testCollectionSerialization(context, serializer, collectionInput, ArrayList::new);
        }

        public void runMapTest(SerializationContext context) {
            TestUtil.testMapSerialization(context, Serializer.Primitive.STRING, serializer, mapInput, HashMap::new);
        }
    }
}
