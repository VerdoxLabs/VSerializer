package de.verdox.vserializer.generic;

import com.google.gson.reflect.TypeToken;
import de.verdox.vserializer.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class SerializerBuilder<T> {
    private final String id;
    private final Class<? extends T> type;

    public static <T> SerializerBuilder<T> create(String id, Class<? extends T> type) {
        return new SerializerBuilder<>(id, type);
    }

    public static <T> SerializerBuilder<T> create(String id, TypeToken<T> typeToken) {
        return new SerializerBuilder<>(id, ((Class<? extends T>) typeToken.getRawType()));
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
    private ConstructorSerializer constructorSerializer;

    private SerializerBuilder(String id, Class<? extends T> type) {
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
                SerializationContainer container = constructorSerializer.serialize(serializationContext, object).getAsContainer();
                fields.forEach((s, serializableField) -> {
                    try{
                        serializableField.write(container, object);
                    }
                    catch (Throwable e){
                        throw new RuntimeException("There was an error in the serializer with the id " + id() + " while serializing the field " + s, e);
                    }
                });
                return container;
            }

            @Override
            public T deserialize(SerializationElement serializedElement) {
                SerializationContainer container = serializedElement.getAsContainer();
                T wrapped = constructorSerializer.deserializer.apply(serializedElement);
                fields.forEach((s, serializableField) -> {
                    try {
                        Object value = serializableField.read(container);
                        if (serializableField.setter() != null)
                            serializableField.setter().accept(wrapped, value);
                    } catch (Throwable e) {
                        throw new RuntimeException("There was an error in the serializer with the id " + id() + " while deserializing the field " + s, e);
                    }
                });
                return wrapped;
            }

            @Override
            public void updateLiveObjectFromJson(@Nullable T existingObject, SerializationElement serializedElement) {
                SerializationContainer container = serializedElement.getAsContainer();
                for (SerializableField<T, ?> field : constructorSerializer.getFields()) {
                    if (field.setter() != null)
                        field.setter().accept(existingObject, field.read(container));
                }
                fields.forEach((s, serializableField) -> {
                    Object value = serializableField.read(container);
                    if (serializableField.setter() != null)
                        serializableField.setter().accept(existingObject, value);
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

    public SerializerBuilder<T> constructor(Supplier<T> constructor) {
        this.constructorSerializer = new ConstructorSerializer(this.id, jsonElement -> constructor.get());
        return this;
    }

    public <R1> SerializerBuilder<T> constructor(
            SerializableField<T, R1> field1,
            Function<R1, T> constructor) {
        this.constructorSerializer = new ConstructorSerializer(this.id, serializationElement -> {
            SerializationContainer container = serializationElement.getAsContainer();
            R1 r1 = field1.read(container);
            return constructor.apply(r1);
        }, field1);
        return this;
    }

    public <R1, R2> SerializerBuilder<T> constructor(
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            BiFunction<R1, R2, T> constructor) {
        this.constructorSerializer = new ConstructorSerializer(this.id, serializationElement -> {
            SerializationContainer container = serializationElement.getAsContainer();
            R1 r1 = field1.read(container);
            R2 r2 = field2.read(container);
            return constructor.apply(r1, r2);
        }, field1, field2);
        return this;
    }

    public <R1, R2, R3> SerializerBuilder<T> constructor(
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            SerializableField<T, R3> field3,
            Function3<R1, R2, R3, T> constructor) {
        this.constructorSerializer = new ConstructorSerializer(this.id, serializationElement -> {
            SerializationContainer container = serializationElement.getAsContainer();
            R1 r1 = field1.read(container);
            R2 r2 = field2.read(container);
            R3 r3 = field3.read(container);
            return constructor.apply(r1, r2, r3);
        }, field1, field2, field3);
        return this;
    }

    public <R1, R2, R3, R4> SerializerBuilder<T> constructor(
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            SerializableField<T, R3> field3,
            SerializableField<T, R4> field4,
            Function4<R1, R2, R3, R4, T> constructor) {
        this.constructorSerializer = new ConstructorSerializer(this.id, serializationElement -> {
            SerializationContainer container = serializationElement.getAsContainer();
            R1 r1 = field1.read(container);
            R2 r2 = field2.read(container);
            R3 r3 = field3.read(container);
            R4 r4 = field4.read(container);
            return constructor.apply(r1, r2, r3, r4);
        }, field1, field2, field3, field4);
        return this;
    }

    public <R1, R2, R3, R4, R5> SerializerBuilder<T> constructor(
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            SerializableField<T, R3> field3,
            SerializableField<T, R4> field4,
            SerializableField<T, R5> field5,
            Function5<R1, R2, R3, R4, R5, T> constructor) {
        this.constructorSerializer = new ConstructorSerializer(this.id, serializationElement -> {
            SerializationContainer container = serializationElement.getAsContainer();
            R1 r1 = field1.read(container);
            R2 r2 = field2.read(container);
            R3 r3 = field3.read(container);
            R4 r4 = field4.read(container);
            R5 r5 = field5.read(container);
            return constructor.apply(r1, r2, r3, r4, r5);
        }, field1, field2, field3, field4, field5);
        return this;
    }

    public <R1, R2, R3, R4, R5, R6> SerializerBuilder<T> constructor(
            SerializableField<T, R1> field1,
            SerializableField<T, R2> field2,
            SerializableField<T, R3> field3,
            SerializableField<T, R4> field4,
            SerializableField<T, R5> field5,
            SerializableField<T, R6> field6,
            Function6<R1, R2, R3, R4, R5, R6, T> constructor) {
        this.constructorSerializer = new ConstructorSerializer(this.id, serializationElement -> {
            SerializationContainer container = serializationElement.getAsContainer();
            R1 r1 = field1.read(container);
            R2 r2 = field2.read(container);
            R3 r3 = field3.read(container);
            R4 r4 = field4.read(container);
            R5 r5 = field5.read(container);
            R6 r6 = field6.read(container);
            return constructor.apply(r1, r2, r3, r4, r5, r6);
        }, field1, field2, field3, field4, field5, field6);
        return this;
    }

    private class ConstructorSerializer implements Serializer<T> {
        private final String id;
        private final Function<SerializationElement, T> deserializer;
        private final SerializableField<T, ?>[] fields;

        @SafeVarargs
        private ConstructorSerializer(String id, Function<SerializationElement, T> deserializer, SerializableField<T, ?>... fields) {
            this.id = id;
            this.deserializer = deserializer;
            this.fields = fields;
        }

        public SerializableField<T, ?>[] getFields() {
            return fields;
        }

        @Override
        public SerializationElement serialize(SerializationContext serializationContext, T object) {
            SerializationContainer container = serializationContext.createContainer();
            for (SerializableField<T, ?> field : this.fields)
                field.write(container, object);
            return container;
        }

        @Override
        public T deserialize(SerializationElement serializedElement) {
            try {
                return deserializer.apply(serializedElement);
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
