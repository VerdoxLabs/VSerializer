package de.verdox.vserializer.generic;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface SerializationContainer extends SerializationElement {
    Collection<String> getChildKeys();

    @NotNull
    SerializationElement get(String key);

    boolean contains(String key);

    void set(String key, SerializationElement serializationElement);

    @Override
    default SerializationContainer getAsContainer(){
        return this;
    }

    @Override
    default boolean isContainer() {
        return true;
    }
}
