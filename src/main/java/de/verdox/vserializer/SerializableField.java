package de.verdox.vserializer;

import de.verdox.vserializer.generic.SerializationContainer;
import de.verdox.vserializer.generic.SerializationElement;
import de.verdox.vserializer.generic.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A serializable field of an object
 *
 * @param <T> The parent object
 * @param <R> The field type
 */
public class SerializableField<T, R> extends AbstractSerializableField<T, R> {

    /**
     * Used for immutable fields
     * @param fieldName the field name
     * @param serializer the field type serializer
     * @param getter the getter
     * @return the field
     * @param <T> the parent object type
     * @param <R> the field type
     */
    public static <T, R> AbstractSerializableField<T, R> finalField(String fieldName, Serializer<R> serializer, Function<T, R> getter) {
        return new SerializableField<>(fieldName, serializer, getter);
    }

    /**
     * Used for mutable fields
     * @param fieldName the field name
     * @param serializer the field type serializer
     * @param getter the getter
     * @param setter the setter
     * @return the field
     * @param <T> the parent object type
     * @param <R> the field type
     */
    public static <T, R> AbstractSerializableField<T, R> create(String fieldName, Serializer<R> serializer, Function<T, R> getter, BiConsumer<T, R> setter) {
        return new SerializableField<>(fieldName, serializer, getter, (t, r) -> {
            setter.accept(t, r);
            return t;
        });
    }

    /**
     * Used for fields that are immutable but can be changed with a builder pattern
     * @param fieldName the field name
     * @param serializer the field type serializer
     * @param getter the getter
     * @param setter the setter
     * @return the field
     * @param <T> the parent object type
     * @param <R> the field type
     */
    public static <T, R> AbstractSerializableField<T, R> builderField(String fieldName, Serializer<R> serializer, Function<T, R> getter, BiFunction<T, R, T> setter) {
        return new SerializableField<>(fieldName, serializer, getter, setter);
    }

    @Nullable
    private BiFunction<T, R, T> setter = null;

    /**
     * Creates a new serializable field
     * @deprecated Deprecated in favor of static factory methods
     */
    @Deprecated
    public SerializableField(@Nullable String fieldName, Serializer<R> serializer, Function<T, R> getter, @Nullable BiFunction<T, R, T> setter) {
        super(fieldName, serializer, getter);
        this.setter = setter;
    }

    /**
     * Creates a new serializable field
     * @deprecated Deprecated in favor of static factory methods
     */
    @Deprecated
    public SerializableField(@Nullable String fieldName, Serializer<R> serializer, Function<T, R> getter, @Nullable BiConsumer<T, R> setter) {
        super(fieldName, serializer, getter);
        if(setter != null) {
            this.setter = (t, r) -> {
                setter.accept(t, r);
                return t;
            };
        }
    }

    /**
     * Creates a new serializable field
     * @deprecated Deprecated in favor of static factory methods
     */
    @Deprecated
    public SerializableField(@Nullable String fieldName, Serializer<R> serializer, Function<T, R> getter) {
        super(fieldName, serializer, getter);
    }

    /**
     * Creates a new serializable field
     * @deprecated Deprecated in favor of static factory methods
     */
    @Deprecated
    public SerializableField(Serializer<R> serializer, Function<T, R> getter) {
        super(serializer, getter);
    }

    @Override
    public void write(SerializationContainer serializationContainer, T wrapped) {
        try {
            R fieldValue = getter.apply(wrapped);

            SerializationElement serialized;
            if (fieldValue == null && !serializer.acceptsNullValues())
                serialized = Serializer.Null.create(serializer.getType()).serialize(serializationContainer.getContext(), null);
            else
                serialized = serializer.serialize(serializationContainer.getContext(), fieldValue);

            serializationContainer.set(fieldName == null ? serializer.id() : fieldName, serialized);
        } catch (Throwable e) {
            throw new RuntimeException("An error occurred in the SerializableField " + fieldName + " while writing with the serializer " + serializer.id() + " of type " + serializer.getType(), e);
        }
    }

    @Override
    public R read(SerializationContainer serializationContainer) {
        try {
            SerializationElement serialized = serializationContainer.get(fieldName == null ? serializer.id() : fieldName);
            if (Serializer.Null.isNull(serialized)) {
                return serializer.defaultValue();
            }
            return serializer.deserialize(serialized);
        } catch (Throwable e) {
            throw new RuntimeException("An error occurred in the SerializableField " + fieldName + " while reading with the serializer " + serializer.id() + " of type " + serializer.getType(), e);
        }
    }

    @Override
    public @Nullable T setValueOnObject(T object, R value) {
        if (setter != null) {
            return setter.apply(object, value);
        }
        return object;
    }
}
