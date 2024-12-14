package de.verdox.vserializer.generic;

/**
 * Represents a serialized primitive.
 */
public interface SerializationPrimitive extends SerializationElement {
    /**
     * Returns true if the primitive is a boolean
     *
     * @return true if it is
     */
    boolean isBoolean();

    /**
     * Returns true if the primitive is a number
     *
     * @return true if it is
     */
    boolean isNumber();

    /**
     * Returns true if the primitive is a string
     *
     * @return true if it is
     */
    boolean isString();

    /**
     * Returns true if the primitive is a character
     *
     * @return true if it is
     */
    default boolean isCharacter() {
        return isString() && getAsString().length() == 1;
    }

    /**
     * Returns true if the primitive is a byte
     *
     * @return true if it is
     */
    default boolean isByte() {
        return isNumber() && getAsNumber() instanceof Byte;
    }

    /**
     * Returns true if the primitive is a short
     *
     * @return true if it is
     */
    default boolean isShort() {
        return isNumber() && getAsNumber() instanceof Short;
    }

    /**
     * Returns true if the primitive is an integer
     *
     * @return true if it is
     */
    default boolean isInteger() {
        return isNumber() && getAsNumber() instanceof Integer;
    }

    /**
     * Returns true if the primitive is a long
     *
     * @return true if it is
     */
    default boolean isLong() {
        return isNumber() && getAsNumber() instanceof Long;
    }

    /**
     * Returns true if the primitive is a float
     *
     * @return true if it is
     */
    default boolean isFloat() {
        return isNumber() && getAsNumber() instanceof Float;
    }

    /**
     * Returns true if the primitive is a double
     *
     * @return true if it is
     */
    default boolean isDouble() {
        return isNumber() && getAsNumber() instanceof Double;
    }

    @Override
    default SerializationPrimitive getAsPrimitive() {
        return this;
    }

    @Override
    default boolean isPrimitive() {
        return true;
    }
}
