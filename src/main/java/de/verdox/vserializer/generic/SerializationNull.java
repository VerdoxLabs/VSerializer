package de.verdox.vserializer.generic;

public interface SerializationNull extends SerializationElement {
    @Override
    default boolean isNull() {
        return true;
    }
}
