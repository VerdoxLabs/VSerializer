package de.verdox.vserializer.generic;

import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;

@ApiStatus.Experimental
public interface SerializationContainer extends SerializationElement {
    Collection<String> getChildKeys();

    SerializationElement get(String key);

    boolean contains(String key);
}
