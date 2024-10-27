package de.verdox.vserializer.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.verdox.vserializer.SerializableField;
import de.verdox.vserializer.util.gson.JsonArrayBuilder;
import de.verdox.vserializer.util.gson.JsonObjectBuilder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public interface JsonSerializer<T> {
    /**
     * Serializes the given object to a {@link JsonElement}
     *
     * @param wrapped the object to serialize
     * @return the serialized json element
     */
    JsonElement toJson(T wrapped);

    /**
     * Deserializes the given {@link JsonElement} to an object
     *
     * @param jsonElement the serialized json element
     * @return the object
     */
    @Nullable
    T fromJson(JsonElement jsonElement);

    /**
     * Updates a given object from a serialized json string.
     * The update functionality varies depending on its implementation.
     * If the serializer does not support live updating objects (like primitive serializer) it will throw a {@link JsonUpdateMethodNotSupportedException}
     *
     * @param existingObject the existing object that is updated
     * @param jsonElement    the update json string
     * @throws JsonUpdateMethodNotSupportedException if the serializer does not support updating a live object
     */
    default void updateLiveObjectFromJson(@Nullable T existingObject, JsonElement jsonElement) {
        throw new JsonUpdateMethodNotSupportedException("The Json Serializer " + getClass().getName() + " does not support updating a live object. If you have implemented your own JsonSerializer and want to enable this functionality make sure to proper implement the updateLiveObjectFromJson function from the JsonSerializer interface.");
    }

    /**
     * The id of this serializer
     * @return the id
     */
    String id();

    /**
     * The object type this serializer is used to for.
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

    class Primitive<T> implements JsonSerializer<T> {
        public static final Primitive<Boolean> BOOLEAN = new Primitive<>(JsonPrimitive::new, JsonElement::getAsBoolean, Boolean.class);
        public static final Primitive<String> STRING = new Primitive<>(JsonPrimitive::new, JsonElement::getAsString, String.class);
        public static final Primitive<Character> CHARACTER = new Primitive<>(JsonPrimitive::new, JsonElement::getAsCharacter, Character.class);
        public static final Primitive<Number> NUMBER = new Primitive<>(JsonPrimitive::new, JsonElement::getAsNumber, Number.class);
        public static final Primitive<Double> DOUBLE = new Primitive<>(JsonPrimitive::new, JsonElement::getAsDouble, Double.class);
        public static final Primitive<Float> FLOAT = new Primitive<>(JsonPrimitive::new, JsonElement::getAsFloat, Float.class);
        public static final Primitive<Long> LONG = new Primitive<>(JsonPrimitive::new, JsonElement::getAsLong, Long.class);
        public static final Primitive<Integer> INTEGER = new Primitive<>(JsonPrimitive::new, JsonElement::getAsInt, Integer.class);
        public static final Primitive<Short> SHORT = new Primitive<>(JsonPrimitive::new, JsonElement::getAsShort, Short.class);
        public static final Primitive<Byte> BYTE = new Primitive<>(JsonPrimitive::new, JsonElement::getAsByte, Byte.class);

        private final Function<T, JsonPrimitive> toFunction;
        private final Function<JsonElement, T> from;
        private final Class<? extends T> type;

        private Primitive(Function<T, JsonPrimitive> to, Function<JsonElement, T> from, Class<? extends T> type) {
            toFunction = to;
            this.from = from;
            this.type = type;
        }

        @Override
        public JsonElement toJson(T wrapped) {
            return toFunction.apply(wrapped);
        }

        @Override
        public @Nullable T fromJson(JsonElement jsonElement) {
            return from.apply(jsonElement);
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

    class Array<E> implements JsonSerializer<E[]> {
        protected final JsonSerializer<E> elementSerializer;
        private final IntFunction<? extends E[]> arrayCreator;

        private Array(JsonSerializer<E> elementSerializer, IntFunction<? extends E[]> arrayCreator) {
            this.elementSerializer = elementSerializer;
            this.arrayCreator = arrayCreator;
        }

        public static <E> Array<E> create(@NotNull JsonSerializer<E> serializer, IntFunction<? extends E[]> arrayCreator) {
            return new Array<>(serializer, arrayCreator);
        }

        @Override
        public JsonElement toJson(E[] wrapped) {
            JsonArrayBuilder jsonArrayBuilder = JsonArrayBuilder.create();
            for (E e : wrapped)
                jsonArrayBuilder.add(elementSerializer.toJson(e));
            return jsonArrayBuilder.build();
        }

        @Override
        public E @Nullable [] fromJson(JsonElement jsonElement) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            List<E> list = new ArrayList<>();
            for (JsonElement element : jsonArray)
                list.add(elementSerializer.fromJson(element));
            return list.toArray(arrayCreator::apply);
        }

        @Override
        public void updateLiveObjectFromJson(@Nullable E[] existingObject, JsonElement jsonElement) {
            E[] deserializedArray = fromJson(jsonElement);
            if (deserializedArray == null)
                return;
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

    class UUID implements JsonSerializer<java.util.UUID> {
        public static final UUID INSTANCE = new UUID();

        private UUID() {

        }

        @Override
        public JsonElement toJson(java.util.UUID wrapped) {
            return new JsonPrimitive(wrapped.toString());
        }

        @Override
        public @Nullable java.util.UUID fromJson(JsonElement jsonElement) {
            return java.util.UUID.fromString(jsonElement.getAsString());
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

    class Enum<E extends java.lang.Enum<E>> implements JsonSerializer<E> {
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
        public JsonElement toJson(E wrapped) {
            return new JsonPrimitive(wrapped.name());
        }

        @Override
        public @Nullable E fromJson(JsonElement jsonElement) {
            return java.lang.Enum.valueOf((Class<E>) type, jsonElement.getAsString());
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

    abstract class Collection<T, C extends java.util.Collection<T>> implements JsonSerializer<C> {
        protected final JsonSerializer<T> elementSerializer;

        private Collection(JsonSerializer<T> elementSerializer) {
            this.elementSerializer = elementSerializer;
        }

        public static <T, C extends java.util.Collection<T>> Collection<T, C> create(@NotNull JsonSerializer<T> serializer, @NotNull Supplier<C> collectionSupplier) {
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

        @Override
        public final JsonElement toJson(C wrapped) {
            JsonArrayBuilder jsonArrayBuilder = JsonArrayBuilder.create();
            for (T t : wrapped)
                jsonArrayBuilder.add(elementSerializer.toJson(t));
            return jsonArrayBuilder.build();
        }

        @Nullable
        @Override
        public final C fromJson(JsonElement jsonElement) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            C list = supplyCollection();
            for (JsonElement element : jsonArray)
                list.add(elementSerializer.fromJson(element));
            return list;
        }

        @Override
        public void updateLiveObjectFromJson(@Nullable C existingObject, JsonElement jsonElement) {
            if(existingObject == null)
                return;
            C deserialized = fromJson(jsonElement);
            if(deserialized == null)
                return;
            existingObject.clear();
            existingObject.addAll(deserialized);
        }

        protected abstract C supplyCollection();
    }

    abstract class Map<K, V, M extends java.util.Map<K, V>> implements JsonSerializer<M> {
        public static <K, V, M extends java.util.Map<K, V>> Map<K, V, M> create(@NotNull JsonSerializer<K> key, @NotNull JsonSerializer<V> value, @NotNull Supplier<M> mapSupplier) {
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

        private final JsonSerializer<K> key;
        private final JsonSerializer<V> value;

        private Map(JsonSerializer<K> key, JsonSerializer<V> value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public void updateLiveObjectFromJson(@Nullable M existingObject, JsonElement jsonElement) {
            if(existingObject == null)
                return;
            M deserialized = fromJson(jsonElement);
            if(deserialized == null)
                return;
            existingObject.clear();
            existingObject.putAll(deserialized);
        }

        @Override
        public JsonElement toJson(M wrapped) {
            boolean isStringKeys = wrapped.keySet().stream().anyMatch(k -> k instanceof String);
            if (isStringKeys) {
                JsonObjectBuilder jsonObjectBuilder = JsonObjectBuilder.create();

                wrapped.forEach((k, v) -> {
                    JsonElement keyElement = key.toJson(k);
                    JsonElement valueElement = value.toJson(v);
                    jsonObjectBuilder.add(keyElement.getAsString(), valueElement);
                });
                return jsonObjectBuilder.build();
            } else {
                JsonArrayBuilder jsonArrayBuilder = JsonArrayBuilder.create();

                wrapped.forEach((k, v) -> {
                    JsonElement keyElement = key.toJson(k);
                    JsonElement valueElement = value.toJson(v);
                    jsonArrayBuilder.add(JsonObjectBuilder.create().add("key", keyElement).add("value", valueElement));
                });
                return jsonArrayBuilder.build();
            }
        }

        @Override
        public @Nullable M fromJson(JsonElement jsonElement) {
            M map = supplyMap();
            if (jsonElement.isJsonArray()) {
                JsonArray jsonArray = jsonElement.getAsJsonArray();
                for (JsonElement element : jsonArray) {
                    JsonElement keyElement = element.getAsJsonObject().get("key");
                    JsonElement valueElement = element.getAsJsonObject().get("value");

                    K key = this.key.fromJson(keyElement);
                    V value = this.value.fromJson(valueElement);
                    map.put(key, value);
                }
            } else {
                JsonObject jsonObject = jsonElement.getAsJsonObject();
                for (String jsonKey : jsonObject.keySet()) {
                    K key = (K) jsonKey;
                    V value = this.value.fromJson(jsonObject.get(jsonKey));
                    map.put(key, value);
                }
            }
            return map;
        }

        protected abstract M supplyMap();

        @Override
        public String id() {
            return "mapping";
        }
    }

    class Types<T> implements JsonSerializer<T> {
        private final String id;
        private final Class<? extends T> type;
        private final java.util.Map<String, JsonSerializer<T>> types = new HashMap<>();

        public static <T> Types<T> create(String id, Class<? extends T> type) {
            return new Types<>(id, type);
        }

        private Types(String id, Class<? extends T> type) {
            this.id = id;
            this.type = type;
        }

        public <R extends T> Types<T> type(String id, JsonSerializer<R> variantSerializer) {
            types.put(id, (JsonSerializer<T>) variantSerializer);
            return this;
        }

        @Override
        public JsonElement toJson(T wrapped) {
            JsonObjectBuilder jsonObjectBuilder = JsonObjectBuilder.create();

            String type = types.entrySet().stream().filter(stringVariantEntry -> stringVariantEntry.getValue().getType().equals(wrapped.getClass()))
                .map(java.util.Map.Entry::getKey).findAny().orElseThrow(() -> new IllegalStateException("Types Serializer " + id + " could not find a variant for type " + wrapped.getClass()));

            jsonObjectBuilder.add("type", type);
            jsonObjectBuilder.add(type, types.get(type).toJson(wrapped));
            return jsonObjectBuilder.build();
        }

        @Override
        public @Nullable T fromJson(JsonElement jsonElement) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (!jsonObject.has("type"))
                return null;

            String type = jsonObject.get("type").getAsString();
            if (!types.containsKey(type))
                throw new IllegalArgumentException("The type " + type + " is not known in this Selection Serializer");
            if (!jsonObject.has(type))
                throw new IllegalArgumentException("The type " + type + " is known to this Selection Serializer. However the type is not specified in the Json");
            return types.get(type).fromJson(jsonObject.get(type));
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

    class Selection<T> implements JsonSerializer<T> {
        private final String id;
        private final Class<? extends T> type;

        public static <T> Selection<T> create(String id, Class<? extends T> type) {
            return new Selection<>(id, type);
        }

        private final java.util.Map<String, Variant<T>> variants = new HashMap<>();
        private String standardVariantID;
        private Variant<T> standardVariant;

        private Selection(String id, Class<? extends T> type) {
            this.id = id;
            this.type = type;
        }

        public <R extends T> Selection<T> variant(String id, JsonSerializer<R> variantSerializer, R variantObject) {
            Variant<T> variant = (Variant<T>) new Variant<>(variantSerializer, variantObject);
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
        public JsonElement toJson(T wrapped) {
            JsonObjectBuilder jsonObjectBuilder = JsonObjectBuilder.create();

            variants.forEach((s, variant) -> jsonObjectBuilder.add(s, variant.serializer().toJson(variant.variant())));

            String type;
            if (wrapped == null) {
                type = variants.keySet().stream().findAny().orElseThrow(() -> new IllegalStateException("Selection Serializer " + id + " need at least one selectable element"));

                if (standardVariantID != null && standardVariant != null) {
                    jsonObjectBuilder.add(standardVariantID, standardVariant.serializer().toJson(standardVariant.variant()));
                    type = standardVariantID;
                }

            } else {
                type = variants.entrySet().stream().filter(stringVariantEntry -> stringVariantEntry.getValue().serializer().getType().equals(wrapped.getClass()) || stringVariantEntry.getValue().serializer().getType().isAssignableFrom(wrapped.getClass()))
                    .map(java.util.Map.Entry::getKey).findAny().orElseThrow(() -> new IllegalStateException("Selection Serializer " + id + " could not find a variant for type " + wrapped.getClass()));

                jsonObjectBuilder.add(type, variants.get(type).serializer().toJson(wrapped));
            }

            jsonObjectBuilder.add("type", type);
            return jsonObjectBuilder.build();
        }

        @Override
        public @Nullable T fromJson(JsonElement jsonElement) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (!jsonObject.has("type"))
                return null;

            String type = jsonObject.get("type").getAsString();
            if (!variants.containsKey(type))
                throw new IllegalArgumentException("The type " + type + " is not known in this Selection Serializer");
            if (!jsonObject.has(type))
                throw new IllegalArgumentException("The type " + type + " is known to this Selection Serializer. However the type is not specified in the Json");
            return variants.get(type).serializer().fromJson(jsonObject.get(type));
        }

        @Override
        public String id() {
            return id;
        }

        @Override
        public Class<? extends T> getType() {
            return type;
        }

        private record Variant<T>(JsonSerializer<T> serializer, T variant) {
        }
    }

    class Dummy<T> implements JsonSerializer<T> {
        public static <T> Dummy<T> create(T defaultValue) {
            return new Dummy<>(defaultValue);
        }

        private final T defaultValue;

        public Dummy(T defaultValue) {
            this.defaultValue = defaultValue;
        }

        @Override
        public JsonElement toJson(T wrapped) {
            return new JsonPrimitive("");
        }

        @Override
        public @Nullable T fromJson(JsonElement jsonElement) {
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

    class Null<T> implements JsonSerializer<T> {
        private final Class<? extends T> type;

        public static <T> Null<T> create(Class<? extends T> type) {
            return new Null<>(type);
        }

        private Null(Class<? extends T> type) {
            this.type = type;
        }

        @Override
        public JsonElement toJson(T wrapped) {
            return new JsonPrimitive("null");
        }

        @Override
        public @Nullable T fromJson(JsonElement jsonElement) {
            return null;
        }

        public boolean isNull(JsonElement jsonElement) {
            return jsonElement.isJsonPrimitive() && jsonElement.getAsString().equals("null");
        }

        @Override
        public void updateLiveObjectFromJson(@Nullable T existingObject, JsonElement jsonElement) {}

        @Override
        public String id() {
            return "null";
        }

        @Override
        public Class<? extends T> getType() {
            return type;
        }
    }
}
