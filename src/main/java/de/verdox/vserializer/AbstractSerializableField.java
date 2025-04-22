package de.verdox.vserializer;

import de.verdox.vserializer.generic.SerializationContainer;
import de.verdox.vserializer.generic.Serializer;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/**
 * A serializable field of an object
 *
 * @param <T> The parent object
 * @param <R> The field type
 */
public abstract class AbstractSerializableField<T, R> {
    protected final String fieldName;
    protected final Serializer<R> serializer;
    protected final Function<T, R> getter;

    public AbstractSerializableField(@Nullable String fieldName, Serializer<R> serializer, Function<T, R> getter) {
        this.fieldName = fieldName;
        this.serializer = serializer;
        this.getter = getter;
    }

    public AbstractSerializableField(Serializer<R> serializer, Function<T, R> getter) {
        this(null, serializer, getter);
    }

    public T readAndSet(@Nullable T parent, SerializationContainer container) {
        if (parent == null) {
            return null;
        }
        R value = read(container);
        return setValueOnObject(parent, value);
    }

    /**
     * Writes a wrapped object to a serialization container
     *
     * @param serializationContainer the container
     * @param wrapped                the wrapped element
     */
    public abstract void write(SerializationContainer serializationContainer, T wrapped);

    /**
     * Reads a wrapped object from a serialization container
     *
     * @param serializationContainer the container
     * @return the wrapped element
     */
    public abstract R read(SerializationContainer serializationContainer);

    /**
     * Updates the value on the object.
     * The function either returns the provided object or a newly created object if an immutable pattern creates a new object due to the set method.
     *<p>
     * @param object the parent object
     * @param value the new field value
     * @return a new object with updates values or the same object.
     */
    @Nullable
    public abstract T setValueOnObject(T object, R value);

    /**
     * Reads the field from the provided object
     * @param object the object
     * @return the return value
     */
    public R readValueFromObject(T object) {
        return getter.apply(object);
    }
}
