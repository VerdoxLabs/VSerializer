package de.verdox.vserializer;

import de.verdox.vserializer.generic.SerializationContainer;
import de.verdox.vserializer.generic.SerializationElement;
import de.verdox.vserializer.generic.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A serializable field of an object
 *
 * @param <T> The parent object
 * @param <R> The field type
 */
public class SerializableField<T, R> {
    private final String fieldName;
    private final Serializer<R> serializer;
    private final Function<T, R> getter;
    private final BiConsumer<T, Object> setter;

    public SerializableField(@Nullable String fieldName, Serializer<R> serializer, Function<T, R> getter, @Nullable BiConsumer<T, R> setter) {
        this.fieldName = fieldName;
        this.serializer = serializer;
        this.getter = getter;
        this.setter = (t, o) -> {
            if (setter != null)
                setter.accept(t, (R) o);
        };
    }

    public SerializableField(@Nullable String fieldName, Serializer<R> serializer, Function<T, R> getter){
        this(fieldName, serializer, getter, null);
    }

    public SerializableField(Serializer<R> serializer, Function<T, R> getter){
        this(null, serializer, getter, null);
    }

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

    public R read(SerializationContainer serializationContainer) {
        SerializationElement serialized = serializationContainer.get(fieldName == null ? serializer.id() : fieldName);
        if (Serializer.Null.isNull(serialized))
            return null;
        return serializer.deserialize(serialized);
    }

    public BiConsumer<T, Object> setter() {
        return setter;
    }

    public Function<T, R> getter() {
        return getter;
    }
}
