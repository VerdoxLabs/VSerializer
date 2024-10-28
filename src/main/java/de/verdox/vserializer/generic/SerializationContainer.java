package de.verdox.vserializer.generic;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface SerializationContainer extends SerializationElement {
    Collection<String> getChildKeys();

    @NotNull
    SerializationElement get(String key);

    boolean contains(String key);

    void set(String key, SerializationElement serializationElement);

    void remove(String key);

    @Override
    default SerializationContainer getAsContainer(){
        return this;
    }

    @Override
    default boolean isContainer() {
        return true;
    }

    default void set(String key, boolean value){
        set(key, getContext().create(value));
    }

    default void set(String key, byte value){
        set(key, getContext().create(value));
    }

    default void set(String key, short value){
        set(key, getContext().create(value));
    }

    default void set(String key, int value){
        set(key, getContext().create(value));
    }

    default void set(String key, long value){
        set(key, getContext().create(value));
    }

    default void set(String key, float value){
        set(key, getContext().create(value));
    }

    default void set(String key, double value){
        set(key, getContext().create(value));
    }

    default void set(String key, char value){
        set(key, getContext().create(value));
    }

    default void set(String key, String value){
        set(key, getContext().create(value));
    }

    default int size(){
        return getChildKeys().size();
    }

    default boolean isEmpty(){
        return getChildKeys().isEmpty();
    }
}
