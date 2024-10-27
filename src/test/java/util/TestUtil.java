package util;

import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.SerializationElement;
import de.verdox.vserializer.generic.Serializer;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class TestUtil {

    /**
     * Used to test whether a map serializer produces right results
     *
     * @param serializationContext The serialization context
     * @param keySerializer        The key serializer
     * @param valueSerializer      The value serializer
     * @param map                  The map to be serialized
     * @param mapSupplier          A supplier for new map instances
     * @param <K>                  The key generic type
     * @param <V>                  The value generic type
     */
    public static <K, V> void testMapSerialization(SerializationContext serializationContext, Serializer<K> keySerializer, Serializer<V> valueSerializer, Map<K, V> map, Supplier<Map<K, V>> mapSupplier) {
        Serializer<Map<K, V>> mapSerializer = Serializer.Map.create(keySerializer, valueSerializer, mapSupplier);
        SerializationElement serializationElement = mapSerializer.serialize(serializationContext, map);
        Assertions.assertEquals(map, mapSerializer.deserialize(serializationElement));
    }

    /**
     * Used to test whether a collection serializer produces right results
     *
     * @param serializationContext The serialization context
     * @param elementSerializer    the element serializer
     * @param collection           the collection to be serialized
     * @param collectionSupplier   A supplier for new collection instances
     * @param <T>                  The generic element type
     */
    public static <T> void testCollectionSerialization(SerializationContext serializationContext, Serializer<T> elementSerializer, Collection<T> collection, Supplier<Collection<T>> collectionSupplier) {
        Serializer<Collection<T>> collectionSerializer = Serializer.Collection.create(elementSerializer, collectionSupplier);
        SerializationElement serializationElement = collectionSerializer.serialize(serializationContext, collection);
        Assertions.assertEquals(collection, collectionSerializer.deserialize(serializationElement));
    }

    /**
     * Used to test whether an array serializer produces right results
     *
     * @param serializationContext The serialization context
     * @param elementSerializer    the element serializer
     * @param array                the array to be serialized
     * @param arrayCreator         A function to create a new array
     * @param <T>                  The generic element type
     */
    public static <T> void testArraySerialization(SerializationContext serializationContext, Serializer<T> elementSerializer, T[] array, IntFunction<? extends T[]> arrayCreator) {
        Serializer<T[]> arraySerializer = Serializer.Array.create(elementSerializer, arrayCreator);
        SerializationElement serializationElement = arraySerializer.serialize(serializationContext, array);
        Assertions.assertArrayEquals(array, arraySerializer.deserialize(serializationElement));
    }

    public static <T> void testPrimitiveSerialization(SerializationContext serializationContext, Serializer.Primitive<T> primitiveSerializer, T primitive){
        testSerialization(serializationContext, primitiveSerializer, primitive);
    }

    public static <T> void testSerialization(SerializationContext serializationContext, Serializer<T> primitiveSerializer, T primitive){
        SerializationElement serializationElement = primitiveSerializer.serialize(serializationContext, primitive);
        Assertions.assertEquals(primitive, primitiveSerializer.deserialize(serializationElement));
    }

}
