package de.verdox.vserializer.generic;

import com.google.common.reflect.TypeToken;
import de.verdox.vserializer.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class SerializerBuilder<T> {
    private final String id;
    private final Class<T> type;

    public static <T> SerializerBuilder<T> create(String id, Class<T> type) {
        return new SerializerBuilder<>(id, type);
    }

    public static <T> SerializerBuilder<T> create(Class<T> type) {
        return new SerializerBuilder<>(type.getSimpleName(), type);
    }

    public static <T> SerializerBuilder<T> create(String id, TypeToken<T> typeToken) {
        return create(id, ((Class<T>) typeToken.getRawType()));
    }

    public static <T> SerializerBuilder<T> create(TypeToken<T> typeToken) {
        return create((Class<T>) typeToken.getRawType());
    }

    /**
     * Used to create a Serializer that takes complex objects and serializes them into a primitive type.
     * This is useful when you don't want to serialize the object itself but only a reference to it (e.g. by referencing a registry key).
     *
     * @param id                  The id of the serializer
     * @param type                the complex object type
     * @param primitiveSerializer The primitive serializer
     * @param objectToPrimitive   the object to primitive function
     * @param primitiveToObject   the primitive to object function
     * @param <T>                 the complex object type
     * @param <P>                 the primitive type
     * @return the new serializer
     */
    public static <T, P> Serializer<T> createObjectToPrimitiveSerializer(String id, Class<? extends T> type, Serializer.Primitive<P> primitiveSerializer, Function<T, P> objectToPrimitive, Function<P, T> primitiveToObject) {
        new SerializableField<String, String>(null, null, null);
        return new Serializer<>() {
            @Override
            public SerializationElement serialize(SerializationContext serializationContext, T object) {
                P primitive = objectToPrimitive.apply(object);
                return primitiveSerializer.serialize(serializationContext, primitive);
            }

            @Override
            public T deserialize(SerializationElement serializedElement) {
                P primitive = primitiveSerializer.deserialize(serializedElement);
                return primitiveToObject.apply(primitive);
            }

            @Override
            public String id() {
                return id;
            }

            @Override
            public Class<? extends T> getType() {
                return type;
            }
        };
    }

    private final Map<String, SerializableField<T, ?>> fields = new HashMap<>();
    private ConstructorSerializer<T> constructorSerializer;

    private SerializerBuilder(String id, Class<T> type) {
        this.id = id;
        this.type = type;
    }

    public <R> SerializerBuilder<T> withField(String name, Serializer<R> serializer, @NotNull Function<T, R> getter, @NotNull BiConsumer<T, R> setter) {
        fields.put(name, new SerializableField<>(name, serializer, getter, setter));
        return this;
    }

    public Serializer<T> build() {
        final String id = this.id;
        if (this.constructorSerializer == null)
            throw new IllegalStateException("No constructor specified for json serializer");

        return new Serializer<>() {
            @Override
            public SerializationElement serialize(SerializationContext serializationContext, T object) {
                Objects.requireNonNull(constructorSerializer, "The serializer was not built correctly. Cannot proceed");
                Objects.requireNonNull(fields, "The serializer was not built correctly. Cannot proceed");
                SerializationContainer container = constructorSerializer.serialize(serializationContext, object).getAsContainer();
                fields.forEach((s, serializableField) -> {
                    try {
                        serializableField.write(container, object);
                    } catch (Throwable e) {
                        throw new RuntimeException("There was an error in the serializer with the id " + id() + " while serializing the field " + s, e);
                    }
                });
                return container;
            }

            @Override
            public T deserialize(SerializationElement serializedElement) {
                if (serializedElement == null) {
                    return null;
                }
                SerializationContainer container = serializedElement.getAsContainer();
                Objects.requireNonNull(constructorSerializer, "The serializer was not built correctly. Cannot proceed");
                Objects.requireNonNull(constructorSerializer.deserializer, "The serializer has no deserialization function");
                T wrapped = constructorSerializer.deserializer.apply(this, serializedElement);

                for (java.util.Map.Entry<String, SerializableField<T, ?>> stringSerializableFieldEntry : fields.entrySet()) {
                    String fieldId = stringSerializableFieldEntry.getKey();
                    SerializableField<T, ?> serializableField = stringSerializableFieldEntry.getValue();
                    try {
                        wrapped = serializableField.readAndSet(wrapped, container);
                    } catch (Throwable e) {
                        throw new RuntimeException("There was an error in the serializer with the id " + id() + " while deserializing the field " + fieldId, e);
                    }
                }
                return wrapped;
            }

            @Override
            public void updateLiveObjectFromJson(@Nullable T existingObject, SerializationElement serializedElement) {
                SerializationContainer container = serializedElement.getAsContainer();
                for (SerializableField<T, ?> field : constructorSerializer.getFields()) {
                    field.readAndSet(existingObject, container);
                }
                fields.forEach((s, serializableField) -> {
                    serializableField.readAndSet(existingObject, container);
                });
            }

            @Override
            public String id() {
                return id;
            }

            @Override
            public Class<? extends T> getType() {
                return type;
            }
        };
    }

    // NO FIELDS

    public SerializerBuilder<T> constructor(Supplier<T> constructor) {
        this.constructorSerializer = new ConstructorSerializer<T>(type, this.id, (serializer, serializationElement) -> constructor.get());
        return this;
    }

    // 1 FIELD

    public <R1> SerializerBuilder<T> constructor(
            SerializableFieldBuilder<T, R1> field1,
            Function<R1, T> constructor
    ) {
        this.constructorSerializer = new ConstructorSerializer<>(type, this.id, (serializer, serializationElement) -> {
            SerializationContainer container = serializationElement.getAsContainer();
            R1 r1 = field1.build(serializer).read(container);
            return Objects.requireNonNull(constructor, "Constructor function cannot be null.").apply(r1);
        }, field1);
        return this;
    }

    public <R1> SerializerBuilder<T> constructor(
            SerializableField<T, R1> field1,
            Function<R1, T> constructor
    ) {
        return constructor(
                s -> field1,
                constructor
        );
    }

    public <R1> SerializerBuilder<T> constructor(
            Function<R1, T> constructor,
            SerializableFieldBuilder<T, R1> field1
    ) {
        return constructor(field1, constructor);
    }

    public <R1> SerializerBuilder<T> constructor(
            Function<R1, T> constructor,
            SerializableField<T, R1> field1
    ) {
        return constructor(
                s -> field1,
                constructor
        );
    }

    // 2 FIELDS

    public <R1, R2> SerializerBuilder<T> constructor(
            SerializableFieldBuilder<T, R1> field1,
            SerializableFieldBuilder<T, R2> field2,
            BiFunction<R1, R2, T> constructor
    ) {
        this.constructorSerializer = new ConstructorSerializer<>(type, this.id, (serializer, serializationElement) -> {
            SerializationContainer container = serializationElement.getAsContainer();
            R1 r1 = field1.build(serializer).read(container);
            R2 r2 = field2.build(serializer).read(container);
            return Objects.requireNonNull(constructor, "Constructor function cannot be null.").apply(r1, r2);
        }, field1, field2);
        return this;
    }

    public <R1, R2> SerializerBuilder<T> constructor(
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            BiFunction<R1, R2, T> constructor
    ) {
        return constructor(
                s -> field1,
                s -> field2,
                constructor
        );
    }

    public <R1, R2> SerializerBuilder<T> constructor(
            BiFunction<R1, R2, T> constructor,
            SerializableFieldBuilder<T, R1> field1,
            SerializableFieldBuilder<T, R2> field2
    ) {
        return constructor(field1, field2, constructor);
    }

    public <R1, R2> SerializerBuilder<T> constructor(
            BiFunction<R1, R2, T> constructor,
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2
    ) {
        return constructor(
                s -> field1,
                s -> field2,
                constructor
        );
    }

    // 3 FIELDS

    public <R1, R2, R3> SerializerBuilder<T> constructor(
            SerializableFieldBuilder<T, R1> field1,
            SerializableFieldBuilder<T, R2> field2,
            SerializableFieldBuilder<T, R3> field3,
            Function3<R1, R2, R3, T> constructor
    ) {
        this.constructorSerializer = new ConstructorSerializer<>(type, this.id, (serializer, serializationElement) -> {
            SerializationContainer container = serializationElement.getAsContainer();
            R1 r1 = field1.build(serializer).read(container);
            R2 r2 = field2.build(serializer).read(container);
            R3 r3 = field3.build(serializer).read(container);
            return Objects.requireNonNull(constructor, "Constructor function cannot be null.").apply(r1, r2, r3);
        }, field1, field2, field3);
        return this;
    }

    public <R1, R2, R3> SerializerBuilder<T> constructor(
            Function3<R1, R2, R3, T> constructor,
            SerializableFieldBuilder<T, R1> field1,
            SerializableFieldBuilder<T, R2> field2,
            SerializableFieldBuilder<T, R3> field3
    ) {
        return constructor(field1, field2, field3, constructor);
    }

    public <R1, R2, R3> SerializerBuilder<T> constructor(
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            SerializableField<T, R3> field3,
            Function3<R1, R2, R3, T> constructor) {
        return constructor(
                s -> field1,
                s -> field2,
                s -> field3,
                constructor
        );
    }

    public <R1, R2, R3> SerializerBuilder<T> constructor(
            Function3<R1, R2, R3, T> constructor,
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            SerializableField<T, R3> field3
    ) {
        return constructor(
                s -> field1,
                s -> field2,
                s -> field3,
                constructor
        );
    }

    // 4 FIELDS

    public <R1, R2, R3, R4> SerializerBuilder<T> constructor(
            SerializableFieldBuilder<T, R1> field1,
            SerializableFieldBuilder<T, R2> field2,
            SerializableFieldBuilder<T, R3> field3,
            SerializableFieldBuilder<T, R4> field4,
            Function4<R1, R2, R3, R4, T> constructor
    ) {
        this.constructorSerializer = new ConstructorSerializer<>(type, this.id, (serializer, serializationElement) -> {
            SerializationContainer container = serializationElement.getAsContainer();
            R1 r1 = field1.build(serializer).read(container);
            R2 r2 = field2.build(serializer).read(container);
            R3 r3 = field3.build(serializer).read(container);
            R4 r4 = field4.build(serializer).read(container);
            return Objects.requireNonNull(constructor, "Constructor function cannot be null.").apply(r1, r2, r3, r4);
        }, field1, field2, field3, field4);
        return this;
    }

    public <R1, R2, R3, R4> SerializerBuilder<T> constructor(
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            SerializableField<T, R3> field3,
            SerializableField<T, R4> field4,
            Function4<R1, R2, R3, R4, T> constructor
    ) {
        return constructor(
                s -> field1,
                s -> field2,
                s -> field3,
                s -> field4,
                constructor
        );
    }

    public <R1, R2, R3, R4> SerializerBuilder<T> constructor(
            Function4<R1, R2, R3, R4, T> constructor,
            SerializableFieldBuilder<T, R1> field1,
            SerializableFieldBuilder<T, R2> field2,
            SerializableFieldBuilder<T, R3> field3,
            SerializableFieldBuilder<T, R4> field4
    ) {
        return constructor(field1, field2, field3, field4, constructor);
    }

    public <R1, R2, R3, R4> SerializerBuilder<T> constructor(
            Function4<R1, R2, R3, R4, T> constructor,
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            SerializableField<T, R3> field3,
            SerializableField<T, R4> field4
    ) {
        return constructor(
                s -> field1,
                s -> field2,
                s -> field3,
                s -> field4,
                constructor
        );
    }

    // 5 FIELDS

    public <R1, R2, R3, R4, R5> SerializerBuilder<T> constructor(
            SerializableFieldBuilder<T, R1> field1,
            SerializableFieldBuilder<T, R2> field2,
            SerializableFieldBuilder<T, R3> field3,
            SerializableFieldBuilder<T, R4> field4,
            SerializableFieldBuilder<T, R5> field5,
            Function5<R1, R2, R3, R4, R5, T> constructor) {
        this.constructorSerializer = new ConstructorSerializer<>(type, this.id, (serializer, serializationElement) -> {
            SerializationContainer container = serializationElement.getAsContainer();
            R1 r1 = field1.build(serializer).read(container);
            R2 r2 = field2.build(serializer).read(container);
            R3 r3 = field3.build(serializer).read(container);
            R4 r4 = field4.build(serializer).read(container);
            R5 r5 = field5.build(serializer).read(container);
            return Objects.requireNonNull(constructor, "Constructor function cannot be null.").apply(r1, r2, r3, r4, r5);
        }, field1, field2, field3, field4, field5);
        return this;
    }

    public <R1, R2, R3, R4, R5> SerializerBuilder<T> constructor(
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            SerializableField<T, R3> field3,
            SerializableField<T, R4> field4,
            SerializableField<T, R5> field5,
            Function5<R1, R2, R3, R4, R5, T> constructor
    ) {
        return constructor(
                s -> field1,
                s -> field2,
                s -> field3,
                s -> field4,
                s -> field5,
                constructor
        );
    }

    public <R1, R2, R3, R4, R5> SerializerBuilder<T> constructor(
            Function5<R1, R2, R3, R4, R5, T> constructor,
            SerializableFieldBuilder<T, R1> field1,
            SerializableFieldBuilder<T, R2> field2,
            SerializableFieldBuilder<T, R3> field3,
            SerializableFieldBuilder<T, R4> field4,
            SerializableFieldBuilder<T, R5> field5
    ) {
        return constructor(field1, field2, field3, field4, field5, constructor);
    }

    public <R1, R2, R3, R4, R5> SerializerBuilder<T> constructor(
            Function5<R1, R2, R3, R4, R5, T> constructor,
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            SerializableField<T, R3> field3,
            SerializableField<T, R4> field4,
            SerializableField<T, R5> field5
    ) {
        return constructor(
                s -> field1,
                s -> field2,
                s -> field3,
                s -> field4,
                s -> field5,
                constructor
        );
    }

    // 6 FIELDS

    public <R1, R2, R3, R4, R5, R6> SerializerBuilder<T> constructor(
            SerializableFieldBuilder<T, R1> field1,
            SerializableFieldBuilder<T, R2> field2,
            SerializableFieldBuilder<T, R3> field3,
            SerializableFieldBuilder<T, R4> field4,
            SerializableFieldBuilder<T, R5> field5,
            SerializableFieldBuilder<T, R6> field6,
            Function6<R1, R2, R3, R4, R5, R6, T> constructor) {
        this.constructorSerializer = new ConstructorSerializer<>(type, this.id, (serializer, serializationElement) -> {
            SerializationContainer container = serializationElement.getAsContainer();
            R1 r1 = field1.build(serializer).read(container);
            R2 r2 = field2.build(serializer).read(container);
            R3 r3 = field3.build(serializer).read(container);
            R4 r4 = field4.build(serializer).read(container);
            R5 r5 = field5.build(serializer).read(container);
            R6 r6 = field6.build(serializer).read(container);
            return Objects.requireNonNull(constructor, "Constructor function cannot be null.").apply(r1, r2, r3, r4, r5, r6);
        }, field1, field2, field3, field4, field5, field6);
        return this;
    }

    public <R1, R2, R3, R4, R5, R6> SerializerBuilder<T> constructor(
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            SerializableField<T, R3> field3,
            SerializableField<T, R4> field4,
            SerializableField<T, R5> field5,
            SerializableField<T, R6> field6,
            Function6<R1, R2, R3, R4, R5, R6, T> constructor
    ) {
        return constructor(
                s -> field1,
                s -> field2,
                s -> field3,
                s -> field4,
                s -> field5,
                s -> field6,
                constructor
        );
    }

    public <R1, R2, R3, R4, R5, R6> SerializerBuilder<T> constructor(
            Function6<R1, R2, R3, R4, R5, R6, T> constructor,
            SerializableFieldBuilder<T, R1> field1,
            SerializableFieldBuilder<T, R2> field2,
            SerializableFieldBuilder<T, R3> field3,
            SerializableFieldBuilder<T, R4> field4,
            SerializableFieldBuilder<T, R5> field5,
            SerializableFieldBuilder<T, R6> field6
    ) {
        return constructor(field1, field2, field3, field4, field5, field6, constructor);
    }

    public <R1, R2, R3, R4, R5, R6> SerializerBuilder<T> constructor(
            Function6<R1, R2, R3, R4, R5, R6, T> constructor,
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            SerializableField<T, R3> field3,
            SerializableField<T, R4> field4,
            SerializableField<T, R5> field5,
            SerializableField<T, R6> field6
    ) {
        return constructor(
                s -> field1,
                s -> field2,
                s -> field3,
                s -> field4,
                s -> field5,
                s -> field6,
                constructor
        );
    }

    // 7 FIELDS

    public <R1, R2, R3, R4, R5, R6, R7> SerializerBuilder<T> constructor(
            SerializableFieldBuilder<T, R1> field1,
            SerializableFieldBuilder<T, R2> field2,
            SerializableFieldBuilder<T, R3> field3,
            SerializableFieldBuilder<T, R4> field4,
            SerializableFieldBuilder<T, R5> field5,
            SerializableFieldBuilder<T, R6> field6,
            SerializableFieldBuilder<T, R7> field7,
            Function7<R1, R2, R3, R4, R5, R6, R7, T> constructor) {
        this.constructorSerializer = new ConstructorSerializer<>(type, this.id, (serializer, serializationElement) -> {
            SerializationContainer container = serializationElement.getAsContainer();
            R1 r1 = field1.build(serializer).read(container);
            R2 r2 = field2.build(serializer).read(container);
            R3 r3 = field3.build(serializer).read(container);
            R4 r4 = field4.build(serializer).read(container);
            R5 r5 = field5.build(serializer).read(container);
            R6 r6 = field6.build(serializer).read(container);
            R7 r7 = field7.build(serializer).read(container);
            return Objects.requireNonNull(constructor, "Constructor function cannot be null.").apply(r1, r2, r3, r4, r5, r6, r7);
        }, field1, field2, field3, field4, field5, field6, field7);
        return this;
    }

    public <R1, R2, R3, R4, R5, R6, R7> SerializerBuilder<T> constructor(
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            SerializableField<T, R3> field3,
            SerializableField<T, R4> field4,
            SerializableField<T, R5> field5,
            SerializableField<T, R6> field6,
            SerializableField<T, R7> field7,
            Function7<R1, R2, R3, R4, R5, R6, R7, T> constructor
    ) {
        return constructor(
                s -> field1,
                s -> field2,
                s -> field3,
                s -> field4,
                s -> field5,
                s -> field6,
                s -> field7,
                constructor
        );
    }

    public <R1, R2, R3, R4, R5, R6, R7> SerializerBuilder<T> constructor(
            Function7<R1, R2, R3, R4, R5, R6, R7, T> constructor,
            SerializableFieldBuilder<T, R1> field1,
            SerializableFieldBuilder<T, R2> field2,
            SerializableFieldBuilder<T, R3> field3,
            SerializableFieldBuilder<T, R4> field4,
            SerializableFieldBuilder<T, R5> field5,
            SerializableFieldBuilder<T, R6> field6,
            SerializableFieldBuilder<T, R7> field7
    ) {
        return constructor(field1, field2, field3, field4, field5, field6, field7, constructor);
    }

    public <R1, R2, R3, R4, R5, R6, R7> SerializerBuilder<T> constructor(
            Function7<R1, R2, R3, R4, R5, R6, R7, T> constructor,
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            SerializableField<T, R3> field3,
            SerializableField<T, R4> field4,
            SerializableField<T, R5> field5,
            SerializableField<T, R6> field6,
            SerializableField<T, R7> field7
    ) {
        return constructor(
                s -> field1,
                s -> field2,
                s -> field3,
                s -> field4,
                s -> field5,
                s -> field6,
                s -> field7,
                constructor
        );
    }

    public static class ConstructorSerializer<T> implements Serializer<T> {
        private final Class<T> type;
        private final String id;
        private final BiFunction<Serializer<T>, SerializationElement, T> deserializer;
        private final SerializableField<T, ?>[] fields;

        @SafeVarargs
        private ConstructorSerializer(Class<T> type, String id, BiFunction<Serializer<T>, SerializationElement, T> deserializer, SerializableFieldBuilder<T, ?>... fields) {
            this.type = type;
            this.id = id;
            this.deserializer = deserializer;
            this.fields = Arrays.stream(fields).map(tSerializableFieldBuilder -> tSerializableFieldBuilder.build(this)).toArray(SerializableField[]::new);
        }

        public SerializableField<T, ?>[] getFields() {
            return fields;
        }

        @Override
        public SerializationElement serialize(SerializationContext serializationContext, T object) {
            try {
                SerializationContainer container = serializationContext.createContainer();
                for (SerializableField<T, ?> field : this.fields) {
                    field.write(container, object);
                }
                return container;
            } catch (Throwable e) {
                throw new RuntimeException("There was an error serializing " + id, e);
            }
        }

        @Override
        public T deserialize(SerializationElement serializedElement) {
            try {
                return deserializer.apply(this, serializedElement);
            } catch (Throwable e) {
                throw new RuntimeException("There was an error deserializing " + id, e);
            }
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

}
