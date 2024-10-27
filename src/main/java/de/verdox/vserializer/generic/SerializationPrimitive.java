package de.verdox.vserializer.generic;

public interface SerializationPrimitive extends SerializationElement {
    boolean isBoolean();

    boolean isNumber();

    boolean isString();

    @Override
    default SerializationPrimitive getAsPrimitive() {
        return this;
    }

    @Override
    default boolean isPrimitive() {
        return true;
    }
}
