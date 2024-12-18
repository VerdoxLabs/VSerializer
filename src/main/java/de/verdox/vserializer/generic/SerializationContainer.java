package de.verdox.vserializer.generic;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;

/**
 * Representing a container that consists of serialization elements stored in a key-value pair manner.
 */
public interface SerializationContainer extends SerializationElement {
    /**
     * Returns all child keys of the container
     *
     * @return the child keys
     */
    Collection<String> getChildKeys();

    /**
     * Returns the element that is mapped to the specified key
     *
     * @param key the key
     * @return the element or null if no element was mapped to this key
     */
    @NotNull
    SerializationElement get(String key);

    /**
     * Returns true if there is a mapping for the specified key
     *
     * @param key the key
     * @return true if there is a mapping
     */
    boolean contains(String key);

    /**
     * Puts a new element into the container with a key
     *
     * @param key                  the key
     * @param serializationElement the element
     */
    void set(String key, SerializationElement serializationElement);

    /**
     * Removes the element from the container that is mapped to the specified key
     *
     * @param key the key
     */
    void remove(String key);

    @Override
    default SerializationContainer getAsContainer() {
        return this;
    }

    @Override
    default boolean isContainer() {
        return true;
    }

    default void set(String key, boolean value) {
        set(key, getContext().create(value));
    }

    default void set(String key, byte value) {
        set(key, getContext().create(value));
    }

    default void set(String key, short value) {
        set(key, getContext().create(value));
    }

    default void set(String key, int value) {
        set(key, getContext().create(value));
    }

    default void set(String key, long value) {
        set(key, getContext().create(value));
    }

    default void set(String key, float value) {
        set(key, getContext().create(value));
    }

    default void set(String key, double value) {
        set(key, getContext().create(value));
    }

    default void set(String key, char value) {
        set(key, getContext().create(value));
    }

    default void set(String key, String value) {
        set(key, getContext().create(value));
    }

    default void set(String key, boolean[] values) {
        SerializationArray array = getContext().createArray(values.length);
        for (var value : values) {
            array.add(getContext().create(value));
        }
        set(key, array);
    }

    default void set(String key, byte[] values) {
        SerializationArray array = getContext().createArray(values.length);
        for (var value : values) {
            array.add(getContext().create(value));
        }
        set(key, array);
    }

    default void set(String key, short[] values) {
        SerializationArray array = getContext().createArray(values.length);
        for (var value : values) {
            array.add(getContext().create(value));
        }
        set(key, array);
    }

    default void set(String key, int[] values) {
        SerializationArray array = getContext().createArray(values.length);
        for (var value : values) {
            array.add(getContext().create(value));
        }
        set(key, array);
    }

    default void set(String key, long[] values) {
        SerializationArray array = getContext().createArray(values.length);
        for (var value : values) {
            array.add(getContext().create(value));
        }
        set(key, array);
    }

    default void set(String key, float[] values) {
        SerializationArray array = getContext().createArray(values.length);
        for (var value : values) {
            array.add(getContext().create(value));
        }
        set(key, array);
    }

    default void set(String key, double[] values) {
        SerializationArray array = getContext().createArray(values.length);
        for (var value : values) {
            array.add(getContext().create(value));
        }
        set(key, array);
    }

    default void set(String key, SerializationElement[] values) {
        SerializationArray array = getContext().createArray(values);
        set(key, array);
    }

    default void set(String key, Collection<? extends SerializationElement> values) {
        SerializationArray array = getContext().createArray(values.toArray(SerializationElement[]::new));
        set(key, array);
    }

    default void setStringList(String key, Collection<String> list) {
        set(key, list.stream().map(s -> getContext().create(s)).toList());
    }

    default void setIntList(String key, Collection<Integer> list) {
        set(key, list.stream().map(s -> getContext().create(s)).toList());
    }

    default List<String> getStringList(String key) {
        return get(key).getAsArray().stream().map(SerializationElement::getAsString).toList();
    }

    default List<Integer> getIntList(String key) {
        return get(key).getAsArray().stream().map(SerializationElement::getAsInt).toList();
    }

    default List<SerializationElement> getElementList(String key) {
        return get(key).getAsArray().stream().toList();
    }

    default List<SerializationContainer> getContainerList(String key) {
        return get(key).getAsArray().stream().map(SerializationElement::getAsContainer).toList();
    }

    default int size() {
        return getChildKeys().size();
    }

    default boolean isEmpty() {
        return getChildKeys().isEmpty();
    }
}
