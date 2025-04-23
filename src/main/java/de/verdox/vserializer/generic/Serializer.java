package de.verdox.vserializer.generic;

import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import de.verdox.vserializer.SerializableField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/**
 * Serializers describe how an object is serialized and deserialized.
 *
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

    default T defaultValue() {
        return null;
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

    class Optional<T> implements Serializer<java.util.Optional<T>> {
        private final Serializer<T> elementSerializer;
        private final TypeToken<java.util.Optional<T>> token;

        public static <E> Optional<E> create(@NotNull Serializer<E> serializer) {
            TypeToken<E> typeTokenT = TypeToken.of((Class<E>) serializer.getType());
            TypeToken<java.util.Optional<E>> optionalTypeToken = new TypeToken<java.util.Optional<E>>() {}
                    .where(new TypeParameter<>(){}, typeTokenT);

            return new Optional<>(serializer, optionalTypeToken);
        }

        private Optional(Serializer<T> elementSerializer, TypeToken<java.util.Optional<T>> type) {
            this.elementSerializer = elementSerializer;
            this.token = type;
        }

        @Override
        public SerializationElement serialize(SerializationContext serializationContext, java.util.Optional<T> object) {
            if (object.isPresent()) {
                return elementSerializer.serialize(serializationContext, object.get());
            }
            return serializationContext.createNull();
        }

        @Override
        public java.util.Optional<T> deserialize(SerializationElement serializedElement) {
            if (serializedElement.isNull()) {
                return java.util.Optional.empty();
            }
            return java.util.Optional.ofNullable(elementSerializer.deserialize(serializedElement));
        }

        @Override
        public String id() {
            return "optional";
        }

        @Override
        public Class<? extends java.util.Optional<T>> getType() {
            return (Class<? extends java.util.Optional<T>>) token.getRawType();
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
        public static final Primitive<Boolean> BOOLEAN = new Primitive<>(SerializationContext::create, SerializationElement::getAsBoolean, Boolean.class, false);
        public static final Primitive<String> STRING = new Primitive<>(SerializationContext::create, SerializationElement::getAsString, String.class, "");
        public static final Primitive<Character> CHARACTER = new Primitive<>(SerializationContext::create, SerializationElement::getAsCharacter, Character.class, (char) 0);
        public static final Primitive<Number> NUMBER = new Primitive<>(SerializationContext::create, SerializationElement::getAsNumber, Number.class, 0);
        public static final Primitive<Double> DOUBLE = new Primitive<>(SerializationContext::create, SerializationElement::getAsDouble, Double.class, 0d);
        public static final Primitive<Float> FLOAT = new Primitive<>(SerializationContext::create, SerializationElement::getAsFloat, Float.class, 0f);
        public static final Primitive<Long> LONG = new Primitive<>(SerializationContext::create, SerializationElement::getAsLong, Long.class, 0L);
        public static final Primitive<Integer> INTEGER = new Primitive<>(SerializationContext::create, SerializationElement::getAsInt, Integer.class, 0);
        public static final Primitive<Short> SHORT = new Primitive<>(SerializationContext::create, SerializationElement::getAsShort, Short.class, (short) 0);
        public static final Primitive<Byte> BYTE = new Primitive<>(SerializationContext::create, SerializationElement::getAsByte, Byte.class, (byte) 0);

        private final BiFunction<SerializationContext, T, SerializationPrimitive> to;
        private final Function<SerializationElement, T> from;
        private final Class<? extends T> type;
        private final T defaultValue;

        private Primitive(BiFunction<SerializationContext, T, SerializationPrimitive> to, Function<SerializationElement, T> from, Class<? extends T> type, T defaultValue) {
            this.to = to;
            this.from = from;
            this.type = type;
            this.defaultValue = defaultValue;
        }

        @Override
        public SerializationElement serialize(SerializationContext serializationContext, T object) {
            return to.apply(serializationContext, object);
        }

        @Override
        public T deserialize(SerializationElement serializedElement) {
            var value = from.apply(serializedElement);
            if (value == null) {
                return defaultValue;
            }
            return value;
        }

        @Override
        public String id() {
            return "primitive";
        }

        @Override
        public Class<? extends T> getType() {
            return type;
        }

        @Override
        public T defaultValue() {
            return defaultValue;
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

    /**
     * A variant serializer that collects multiple serializers as different variants for the same serialization type.
     *
     * @param <T> The serialization type
     * @param <R> The variant type collected
     */
    abstract class VariantsSerializer<T, R> implements Serializer<T> {
        protected final String id;
        protected final Class<? extends T> type;
        protected final java.util.Map<String, R> variants = new HashMap<>();
        protected final Set<Class<? extends T>> containedTypes = new HashSet<>();

        private VariantsSerializer(String id, Class<? extends T> type) {
            this.id = id;
            this.type = type;
        }

        protected VariantsSerializer<T, R> addVariant(String id, R variant, Class<? extends T> variantType) {
            variants.put(id, variant);
            if (containedTypes.contains(variantType))
                throw new IllegalArgumentException("The serializer " + id + " does already contain a type serializer for the type " + variantType);
            containedTypes.add(variantType);
            return this;
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

    class Types<T> extends VariantsSerializer<T, Serializer<T>> {
        public static <T> Types<T> create(String id, Class<? extends T> type) {
            return new Types<>(id, type);
        }

        private Types(String id, Class<? extends T> type) {
            super(id, type);
        }

        public <R extends T> Types<T> type(String id, Serializer<R> variantSerializer) {
            return (Types<T>) addVariant(id, (Serializer<T>) variantSerializer, variantSerializer.getType());
        }

        @Override
        public SerializationElement serialize(SerializationContext serializationContext, T object) {
            SerializationContainer container = serializationContext.createContainer();

            String type = variants.entrySet().stream().filter(stringVariantEntry -> stringVariantEntry.getValue().getType().equals(object.getClass()))
                    .map(java.util.Map.Entry::getKey).findAny().orElseThrow(() -> new IllegalStateException("Types Serializer " + id + " could not find a variant for type " + object.getClass()));

            container.set("type", serializationContext.create(type));
            container.set(type, variants.get(type).serialize(serializationContext, object));
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
            if (!container.contains(type))
                throw new IllegalArgumentException("The type " + type + " is known to this Selection Serializer. However the type is not specified in the serialized element " + container);
            return variants.get(type).deserialize(container.get(type));
        }
    }

    /**
     * A selection serializer should be used when an object is configured in a config file. The Serializer will always provide all variants.
     * A variant can be picked by changing the "type" field in the {@link SerializationElement} which is normally written into the config file.
     * If you serialize an object with this serializer, the serializer will search for the right variant based on its class type.
     * If the serializer finds the right type it will write the object into the respective field. Else it will throw an {@link IllegalStateException}
     *
     * @param <T> The serialized type.
     */
    class Selection<T> extends VariantsSerializer<T, Selection.Variant<T>> {
        public static <T> Selection<T> create(String id, Class<? extends T> type) {
            return new Selection<>(id, type);
        }

        private String standardVariantID;
        private Selection.Variant<T> standardVariant;

        private Selection(String id, Class<? extends T> type) {
            super(id, type);
        }

        public <R extends T> Selection<T> variant(String id, Serializer<R> variantSerializer, R variantObject) {
            Selection.Variant<T> variant = (Selection.Variant<T>) new Selection.Variant<>(variantSerializer, variantObject);
            if (standardVariantID != null && standardVariant == null) {
                standardVariantID = id;
                standardVariant = variant;
            }
            return (Selection<T>) addVariant(id, variant, variantSerializer.getType());
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
                throw new IllegalArgumentException("The type " + type + " is known to this Selection Serializer. However the type is not specified in the serialized element " + container);
            return variants.get(type).serializer().deserialize(container.get(type));
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
