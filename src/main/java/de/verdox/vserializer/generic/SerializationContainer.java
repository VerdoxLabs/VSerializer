package de.verdox.vserializer.generic;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

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

    default int size() {
        return getChildKeys().size();
    }

    default boolean isEmpty() {
        return getChildKeys().isEmpty();
    }
}
