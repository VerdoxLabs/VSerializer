package de.verdox.vserializer.generic;

import de.verdox.vserializer.SerializableField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/**
 * @param <T> the data type that is serialized
 */
public interface Serializer<T> {

    /**
     * Serializes the given object to a {@link SerializationElement}
     *
     * @param object the object to serialize
     * @return the serialized element
     */
    SerializationElement serialize(SerializationContext serializationContext, T object);

    /**
     * Deserializes the given {@link SerializationElement} to an object
     *
     * @param serializedElement the serialized element
     * @return the object
     */
    T deserialize(SerializationElement serializedElement);

    /**
     * Updates a given object from a serialized element.
     * The update functionality varies depending on its implementation.
     * If the serializer does not support live updating objects (like primitive serializer) it will throw a {@link UnsupportedOperationException}
     *
     * @param existingObject    the existing object that is updated
     * @param serializedElement the update element
     * @throws UnsupportedOperationException if the serializer does not support updating a live object
     */
    default void updateLiveObjectFromJson(@Nullable T existingObject, SerializationElement serializedElement) {
        throw new UnsupportedOperationException("The Serializer " + getClass().getName() + " does not support updating a live object. If you have implemented your own Serializer and want to enable this functionality make sure to proper implement the updateLiveObjectFromJson function from the Serializer interface.");
    }

    /**
     * The id of this serializer
     *
     * @return the id
     */
    String id();

    /**
     * The object type this serializer is used to for.
     *
     * @return the type
     */
    Class<? extends T> getType();

    /**
     * If this serializer does not accept null values it will be replaced by a Null Serializer in {@link SerializableField}
     * Programmers should return true when their Serializer handles null values explicitly.
     *
     * @return whether this serializer accepts null values on serialization
     */
    default boolean acceptsNullValues() {
        return false;
    }

    class UUID implements Serializer<java.util.UUID> {
        public static final UUID INSTANCE = new UUID();

        private UUID() {

        }

        @Override
        public SerializationElement serialize(SerializationContext serializationContext, java.util.UUID object) {
            return serializationContext.create(object.toString());
        }

        @Override
        public java.util.UUID deserialize(SerializationElement serializedElement) {
            return java.util.UUID.fromString(serializedElement.getAsString());
        }

        @Override
        public String id() {
            return "uuid";
        }

        @Override
        public Class<? extends java.util.UUID> getType() {
            return java.util.UUID.class;
        }
    }

    class Null<T> implements Serializer<T> {
        private final Class<? extends T> type;

        public static <T> Null<T> create(Class<? extends T> type) {
            return new Null<>(type);
        }

        private Null(Class<? extends T> type) {
            this.type = type;
        }

        @Override
        public SerializationElement serialize(SerializationContext serializationContext, T object) {
            return serializationContext.createNull();
        }

        @Override
        public T deserialize(SerializationElement serializedElement) {
            return null;
        }

        public static boolean isNull(SerializationElement serializationElement) {
            return serializationElement.isNull();
        }

        @Override
        public void updateLiveObjectFromJson(@Nullable T existingObject, SerializationElement serializedElement) {

        }

        @Override
        public String id() {
            return "null";
        }

        @Override
        public Class<? extends T> getType() {
            return type;
        }
    }

    class Primitive<T> implements Serializer<T> {
        public static final Primitive<Boolean> BOOLEAN = new Primitive<>(SerializationContext::create, SerializationElement::getAsBoolean, Boolean.class);
        public static final Primitive<String> STRING = new Primitive<>(SerializationContext::create, SerializationElement::getAsString, String.class);
        public static final Primitive<Character> CHARACTER = new Primitive<>(SerializationContext::create, SerializationElement::getAsCharacter, Character.class);
        public static final Primitive<Number> NUMBER = new Primitive<>(SerializationContext::create, SerializationElement::getAsNumber, Number.class);
        public static final Primitive<Double> DOUBLE = new Primitive<>(SerializationContext::create, SerializationElement::getAsDouble, Double.class);
        public static final Primitive<Float> FLOAT = new Primitive<>(SerializationContext::create, SerializationElement::getAsFloat, Float.class);
        public static final Primitive<Long> LONG = new Primitive<>(SerializationContext::create, SerializationElement::getAsLong, Long.class);
        public static final Primitive<Integer> INTEGER = new Primitive<>(SerializationContext::create, SerializationElement::getAsInt, Integer.class);
        public static final Primitive<Short> SHORT = new Primitive<>(SerializationContext::create, SerializationElement::getAsShort, Short.class);
        public static final Primitive<Byte> BYTE = new Primitive<>(SerializationContext::create, SerializationElement::getAsByte, Byte.class);

        private final BiFunction<SerializationContext, T, SerializationPrimitive> to;
        private final Function<SerializationElement, T> from;
        private final Class<? extends T> type;

        private Primitive(BiFunction<SerializationContext, T, SerializationPrimitive> to, Function<SerializationElement, T> from, Class<? extends T> type) {
            this.to = to;
            this.from = from;
            this.type = type;
        }

        @Override
        public SerializationElement serialize(SerializationContext serializationContext, T object) {
            return to.apply(serializationContext, object);
        }

        @Override
        public T deserialize(SerializationElement serializedElement) {
            return from.apply(serializedElement);
        }

        @Override
        public String id() {
            return "primitive";
        }

        @Override
        public Class<? extends T> getType() {
            return type;
        }
    }

    class Array<E> implements Serializer<E[]> {
        protected final Serializer<E> elementSerializer;
        private final IntFunction<? extends E[]> arrayCreator;

        private Array(Serializer<E> elementSerializer, IntFunction<? extends E[]> arrayCreator) {
            this.elementSerializer = elementSerializer;
            this.arrayCreator = arrayCreator;
        }

        public static <E> Array<E> create(@NotNull Serializer<E> serializer, IntFunction<? extends E[]> arrayCreator) {
            return new Array<>(serializer, arrayCreator);
        }

        @Override
        public SerializationElement serialize(SerializationContext serializationContext, E[] objects) {
            SerializationArray array = serializationContext.createArray();
            for (E e : objects)
                array.add(elementSerializer.serialize(serializationContext, e));
            return array;
        }

        @Override
        public E[] deserialize(SerializationElement serializedElement) {
            SerializationArray jsonArray = serializedElement.getAsArray();
            List<E> list = new ArrayList<>();
            for (SerializationElement element : jsonArray)
                list.add(elementSerializer.deserialize(element));
            return list.toArray(arrayCreator::apply);
        }

        @Override
        public void updateLiveObjectFromJson(E @Nullable [] existingObject, SerializationElement serializedElement) {
            E[] deserializedArray = deserialize(serializedElement);
            if (existingObject == null)
                return;
            if (deserializedArray == null) {
                Arrays.fill(existingObject, null);
                return;
            }
            for (int i = 0; i < deserializedArray.length && i < existingObject.length; i++) {
                existingObject[i] = deserializedArray[i];
            }
        }

        @Override
        public String id() {
            return "array";
        }

        @Override
        public Class<? extends E[]> getType() {
            return (Class<? extends E[]>) java.lang.reflect.Array.newInstance(elementSerializer.getType(), 0).getClass();
        }
    }

    class Enum<E extends java.lang.Enum<E>> implements Serializer<E> {
        private final String id;
        private final Class<? extends E> type;

        public static <E extends java.lang.Enum<E>> Enum<E> create(String id, Class<? extends E> type) {
            return new Enum<>(id, type);
        }

        private Enum(String id, Class<? extends E> type) {
            this.id = id;
            this.type = type;
        }

        @Override
        public SerializationElement serialize(SerializationContext serializationContext, E object) {
            return serializationContext.create(object.name());
        }

        @Override
        public E deserialize(SerializationElement serializedElement) {
            return java.lang.Enum.valueOf((Class<E>) type, serializedElement.getAsString());
        }

        @Override
        public String id() {
            return id;
        }

        @Override
        public Class<? extends E> getType() {
            return type;
        }
    }

    abstract class Collection<T, C extends java.util.Collection<T>> implements Serializer<C> {
        public static <T, C extends java.util.Collection<T>> Collection<T, C> create(@NotNull Serializer<T> serializer, @NotNull Supplier<C> collectionSupplier) {
            return new Collection<>(serializer) {
                @Override
                protected C supplyCollection() {
                    return collectionSupplier.get();
                }

                @Override
                public String id() {
                    return "collection";
                }

                @Override
                public Class<? extends C> getType() {
                    return (Class<? extends C>) supplyCollection().getClass();
                }
            };
        }

        protected final Serializer<T> elementSerializer;

        private Collection(Serializer<T> elementSerializer) {
            this.elementSerializer = elementSerializer;
        }

        @Override
        public SerializationElement serialize(SerializationContext serializationContext, C objects) {
            SerializationArray array = serializationContext.createArray();
            for (T object : objects)
                array.add(elementSerializer.serialize(serializationContext, object));
            return array;
        }

        @Override
        public C deserialize(SerializationElement serializedElement) {
            SerializationArray jsonArray = serializedElement.getAsArray();
            C list = supplyCollection();
            for (SerializationElement element : jsonArray)
                list.add(elementSerializer.deserialize(element));
            return list;
        }

        @Override
        public void updateLiveObjectFromJson(@Nullable C existingObject, SerializationElement serializedElement) {
            if (existingObject == null)
                return;
            C deserialized = deserialize(serializedElement);
            if (deserialized == null)
                return;
            existingObject.clear();
            existingObject.addAll(deserialized);
        }

        protected abstract C supplyCollection();
    }

    abstract class Map<K, V, M extends java.util.Map<K, V>> implements Serializer<M> {
        public static <K, V, M extends java.util.Map<K, V>> Map<K, V, M> create(@NotNull Serializer<K> key, @NotNull Serializer<V> value, @NotNull Supplier<M> mapSupplier) {
            return new Map<>(key, value) {

                @Override
                public Class<? extends M> getType() {
                    return (Class<? extends M>) supplyMap().getClass();
                }

                @Override
                protected M supplyMap() {
                    return mapSupplier.get();
                }
            };
        }

        private final Serializer<K> key;
        private final Serializer<V> value;

        private Map(Serializer<K> key, Serializer<V> value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public SerializationElement serialize(SerializationContext serializationContext, M object) {
            boolean isStringKeys = object.keySet().stream().anyMatch(k -> k instanceof String);
            if (isStringKeys) {
                SerializationContainer container = serializationContext.createContainer();

                object.forEach((k, v) -> {
                    SerializationElement keyElement = key.serialize(serializationContext, k);
                    SerializationElement valueElement = value.serialize(serializationContext, v);
                    container.set(keyElement.getAsString(), valueElement);
                });
                return container;
            } else {
                SerializationArray array = serializationContext.createArray();

                object.forEach((k, v) -> {
                    SerializationElement keyElement = key.serialize(serializationContext, k);
                    SerializationElement valueElement = value.serialize(serializationContext, v);

                    SerializationContainer element = serializationContext.createContainer();
                    element.set("key", keyElement);
                    element.set("value", valueElement);

                    array.add(element);
                });
                return array;
            }
        }

        @Override
        public M deserialize(SerializationElement serializedElement) {
            M map = supplyMap();
            if (serializedElement.isArray()) {
                SerializationArray array = serializedElement.getAsArray();
                for (SerializationElement element : array) {
                    SerializationElement keyElement = element.getAsContainer().get("key");
                    SerializationElement valueElement = element.getAsContainer().get("value");

                    K key = this.key.deserialize(keyElement);
                    V value = this.value.deserialize(valueElement);
                    map.put(key, value);
                }
            } else {
                SerializationContainer container = serializedElement.getAsContainer();
                for (String serializationKey : container.getChildKeys()) {
                    K key = (K) serializationKey;
                    V value = this.value.deserialize(container.get(serializationKey));
                    map.put(key, value);
                }
            }
            return map;
        }

        @Override
        public void updateLiveObjectFromJson(@Nullable M existingObject, SerializationElement serializedElement) {
            if (existingObject == null)
                return;
            M deserialized = deserialize(serializedElement);
            if (deserialized == null)
                return;
            existingObject.clear();
            existingObject.putAll(deserialized);
        }

        protected abstract M supplyMap();

        @Override
        public String id() {
            return "mapping";
        }
    }

    class Types<T> implements Serializer<T> {
        private final String id;
        private final Class<? extends T> type;
        private final java.util.Map<String, Serializer<T>> types = new HashMap<>();

        public static <T> Types<T> create(String id, Class<? extends T> type) {
            return new Types<>(id, type);
        }

        private Types(String id, Class<? extends T> type) {
            this.id = id;
            this.type = type;
        }

        public <R extends T> Types<T> type(String id, Serializer<R> variantSerializer) {
            types.put(id, (Serializer<T>) variantSerializer);
            return this;
        }

        @Override
        public SerializationElement serialize(SerializationContext serializationContext, T object) {
            SerializationContainer container = serializationContext.createContainer();

            String type = types.entrySet().stream().filter(stringVariantEntry -> stringVariantEntry.getValue().getType().equals(object.getClass()))
                    .map(java.util.Map.Entry::getKey).findAny().orElseThrow(() -> new IllegalStateException("Types Serializer " + id + " could not find a variant for type " + object.getClass()));

            container.set("type", serializationContext.create(type));
            container.set(type, types.get(type).serialize(serializationContext, object));
            return container;
        }

        @Override
        public T deserialize(SerializationElement serializedElement) {
            SerializationContainer container = serializedElement.getAsContainer();
            if (!container.contains("type"))
                return null;

            String type = container.get("type").getAsString();
            if (!types.containsKey(type))
                throw new IllegalArgumentException("The type " + type + " is not known in this Selection Serializer");
            if (!container.contains(type))
                throw new IllegalArgumentException("The type " + type + " is known to this Selection Serializer. However the type is not specified in the serialized element "+container);
            return types.get(type).deserialize(container.get(type));
        }

        @Override
        public String id() {
            return id;
        }

        @Override
        public Class<? extends T> getType() {
            return type;
        }
    }

    class Selection<T> implements Serializer<T> {
        private final String id;
        private final Class<? extends T> type;

        public static <T> Selection<T> create(String id, Class<? extends T> type) {
            return new Selection<>(id, type);
        }

        private final java.util.Map<String, Selection.Variant<T>> variants = new HashMap<>();
        private String standardVariantID;
        private Selection.Variant<T> standardVariant;

        private Selection(String id, Class<? extends T> type) {
            this.id = id;
            this.type = type;
        }

        public <R extends T> Selection<T> variant(String id, Serializer<R> variantSerializer, R variantObject) {
            Selection.Variant<T> variant = (Selection.Variant<T>) new Selection.Variant<>(variantSerializer, variantObject);
            variants.put(id, variant);
            if (standardVariantID == null)
                standardVariantID = id;
            if (standardVariant == null)
                standardVariant = variant;
            return this;
        }

        public <R extends T> Selection<T> variant(String id, Dummy<R> variantSerializer) {
            return variant(id, variantSerializer, variantSerializer.defaultValue);
        }

        public Selection<T> empty(String id) {
            return variant(id, Null.create(getType()), null);
        }

        @Override
        public boolean acceptsNullValues() {
            return true;
        }

        @Override
        public SerializationElement serialize(SerializationContext serializationContext, T object) {
            SerializationContainer container = serializationContext.createContainer();

            variants.forEach((s, variant) -> container.set(s, variant.serializer().serialize(serializationContext, variant.variant())));

            String type;
            if (object == null) {
                type = variants.keySet().stream().findAny().orElseThrow(() -> new IllegalStateException("Selection Serializer " + id + " need at least one selectable element"));

                if (standardVariantID != null && standardVariant != null) {
                    container.set(standardVariantID, standardVariant.serializer().serialize(serializationContext, standardVariant.variant()));
                    type = standardVariantID;
                }

            } else {
                type = variants.entrySet().stream().filter(stringVariantEntry -> stringVariantEntry.getValue().serializer().getType().equals(object.getClass()) || stringVariantEntry.getValue().serializer().getType().isAssignableFrom(object.getClass()))
                        .map(java.util.Map.Entry::getKey).findAny().orElseThrow(() -> new IllegalStateException("Selection Serializer " + id + " could not find a variant for type " + object.getClass()));

                container.set(type, variants.get(type).serializer().serialize(serializationContext, object));
            }

            container.set("type", serializationContext.create(type));
            return container;
        }

        @Override
        public T deserialize(SerializationElement serializedElement) {
            SerializationContainer container = serializedElement.getAsContainer();
            if (!container.contains("type"))
                return null;

            String type = container.get("type").getAsString();
            if (!variants.containsKey(type))
                throw new IllegalArgumentException("The type " + type + " is not known in this Selection Serializer");

            Variant<?> variant = variants.get(type);
            if (!container.contains(type) && variant.variant != null)
                throw new IllegalArgumentException("The type " + type + " is known to this Selection Serializer. However the type is not specified in the serialized element "+container);
            return variants.get(type).serializer().deserialize(container.get(type));
        }

        @Override
        public String id() {
            return id;
        }

        @Override
        public Class<? extends T> getType() {
            return type;
        }

        private record Variant<T>(Serializer<T> serializer, T variant) {
        }
    }

    class Dummy<T> implements Serializer<T> {
        public static <T> Dummy<T> create(T defaultValue) {
            return new Dummy<>(defaultValue);
        }

        private final T defaultValue;

        public Dummy(T defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public SerializationElement serialize(SerializationContext serializationContext, T object) {
            return serializationContext.create("");
        }

        @Override
        public T deserialize(SerializationElement serializedElement) {
            return defaultValue;
        }

        @Override
        public String id() {
            return "dummy";
        }

        @Override
        public Class<? extends T> getType() {
            return (Class<? extends T>) defaultValue.getClass();
        }
    }
}
