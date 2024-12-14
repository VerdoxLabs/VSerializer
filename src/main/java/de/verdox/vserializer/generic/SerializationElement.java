package de.verdox.vserializer.generic;

/**
 * Represents a serialized element that holds some information.
 */
public interface SerializationElement {
    /**
     * Casts this element into a bool value
     *
     * @return the value
     */
    boolean getAsBoolean();

    /**
     * Casts this element into a string value
     *
     * @return the value
     */
    String getAsString();

    /**
     * Casts this element into a char value
     *
     * @return the value
     */
    char getAsCharacter();

    /**
     * Casts this element into a number value
     *
     * @return the value
     */
    Number getAsNumber();

    /**
     * Casts this element into a double value
     *
     * @return the value
     */
    double getAsDouble();

    /**
     * Casts this element into a float value
     *
     * @return the value
     */
    float getAsFloat();

    /**
     * Casts this element into a long value
     *
     * @return the value
     */
    long getAsLong();

    /**
     * Casts this element into an int value
     *
     * @return the value
     */
    int getAsInt();

    /**
     * Casts this element into a short value
     *
     * @return the value
     */
    short getAsShort();

    /**
     * Casts this element into a byte value
     *
     * @return the value
     */
    byte getAsByte();

    /**
     * Gets this element as a {@link SerializationElement}
     *
     * @return the value
     */
    default SerializationElement getAsElement() {
        return this;
    }

    /**
     * Gets this element as a {@link SerializationContainer}.
     * Before using this method check if it is supported by using {@link #isContainer()}.
     * If it is not supported the method will throw an {@link UnsupportedOperationException}
     *
     * @return the value
     * @throws UnsupportedOperationException if the element is not a container
     */
    default SerializationContainer getAsContainer() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets this element as a {@link SerializationArray}.
     * Before using this method check if it is supported by using {@link #isArray()}.
     * If it is not supported the method will throw an {@link UnsupportedOperationException}
     *
     * @return the value
     * @throws UnsupportedOperationException if the element is not an array
     */
    default SerializationArray getAsArray() {
        throw new UnsupportedOperationException();
    }

    /**
     * Gets this element as a {@link SerializationPrimitive}.
     * Before using this method check if it is supported by using {@link #isPrimitive()}.
     * If it is not supported the method will throw an {@link UnsupportedOperationException}
     *
     * @return the value
     * @throws UnsupportedOperationException if the element is not a primitive
     */
    default SerializationPrimitive getAsPrimitive() {
        throw new UnsupportedOperationException();
    }

    /**
     * Checks if this serialization element is a {@link SerializationPrimitive}
     *
     * @return true if it is
     */
    default boolean isPrimitive() {
        return false;
    }

    /**
     * Checks if this serialization element is a {@link SerializationContainer}
     *
     * @return true if it is
     */
    default boolean isContainer() {
        return false;
    }

    /**
     * Checks if this serialization element is a {@link SerializationArray}
     *
     * @return true if it is
     */
    default boolean isArray() {
        return false;
    }

    /**
     * Checks if this serialization element is a {@link SerializationNull}
     *
     * @return true if it is
     */
    default boolean isNull() {
        return false;
    }

    /**
     * Returns the context of this serialization element
     *
     * @return the context
     */
    SerializationContext getContext();
}
