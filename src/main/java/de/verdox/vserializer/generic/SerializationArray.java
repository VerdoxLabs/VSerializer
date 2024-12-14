package de.verdox.vserializer.generic;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Represents a serialized array of elements.
 */
public interface SerializationArray extends SerializationElement, Iterable<SerializationElement> {
    /**
     * Returns the length of the serialized array
     *
     * @return the length
     */
    int length();

    /**
     * Returns the element at a specific index in the array.
     *
     * @param index the index
     * @return the element found at the index
     * @throws IndexOutOfBoundsException when the index is less than 0 or greater or equal than {@link #length()}
     */
    SerializationElement get(int index);

    /**
     * Adds an element to the end of the array and grows the {@link #length()} by one
     *
     * @param serializationElement the element
     */
    void add(SerializationElement serializationElement);

    /**
     * Sets the element at a specific index.
     *
     * @param index                the index
     * @param serializationElement the element
     * @throws IndexOutOfBoundsException when the index is less than 0 or greater or equal than {@link #length()}
     */
    void set(int index, SerializationElement serializationElement);

    /**
     * Removes the element from a specific index
     *
     * @param index the index
     * @return the removed element
     * @throws IndexOutOfBoundsException when the index is less than 0 or greater or equal than {@link #length()}
     */
    SerializationElement remove(int index);

    /**
     * Adds all elements from a
     * @param serializationArray
     */
    default void addAll(SerializationArray serializationArray) {
        for (SerializationElement element : serializationArray) {
            add(element);
        }
    }


    @Override
    default SerializationArray getAsArray() {
        return this;
    }

    @Override
    default boolean isArray() {
        return true;
    }

    /**
     * Returns a stream consisting of the array elements
     *
     * @return the stream
     */
    default Stream<SerializationElement> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    /**
     * Checks whether the array consists of bool values
     *
     * @return the result of the check
     */
    default boolean isBoolArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isBoolean())
                return false;
        }
        return true;
    }

    /**
     * Checks whether the array consists of char values
     *
     * @return the result of the check
     */
    default boolean isCharArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isCharacter())
                return false;
        }
        return true;
    }

    /**
     * Checks whether the array consists of string values
     *
     * @return the result of the check
     */
    default boolean isStringArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isString())
                return false;
        }
        return true;
    }

    /**
     * Checks whether the array consists of byte values
     *
     * @return the result of the check
     */
    default boolean isByteArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isByte())
                return false;
        }
        return true;
    }

    /**
     * Checks whether the array consists of short values
     *
     * @return the result of the check
     */
    default boolean isShortArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isShort())
                return false;
        }
        return true;
    }

    /**
     * Checks whether the array consists of int values
     *
     * @return the result of the check
     */
    default boolean isIntArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isInteger())
                return false;
        }
        return true;
    }

    /**
     * Checks whether the array consists of long values
     *
     * @return the result of the check
     */
    default boolean isLongArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isLong())
                return false;
        }
        return true;
    }

    /**
     * Checks whether the array consists of float values
     *
     * @return the result of the check
     */
    default boolean isFloatArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isFloat())
                return false;
        }
        return true;
    }

    /**
     * Checks whether the array consists of double values
     *
     * @return the result of the check
     */
    default boolean isDoubleArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isDouble())
                return false;
        }
        return true;
    }

    /**
     * Checks whether the array consists of array values
     *
     * @return the result of the check
     */
    default boolean isArrayOfArrays() {
        for (SerializationElement element : this) {
            if (!element.isArray())
                return false;
        }
        return true;
    }

    /**
     * Checks whether the array consists of container values
     *
     * @return the result of the check
     */
    default boolean isContainerArray() {
        for (SerializationElement element : this) {
            if (!element.isContainer())
                return false;
        }
        return true;
    }

    /**
     * Casts this array into a bool array
     *
     * @return the array
     */
    default boolean[] getAsBooleanArray() {
        boolean[] array = new boolean[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsBoolean();
        }
        return array;
    }

    /**
     * Casts this array into a char array
     *
     * @return the array
     */
    default char[] getAsCharArray() {
        char[] array = new char[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsCharacter();
        }
        return array;
    }

    /**
     * Casts this array into a byte array
     *
     * @return the array
     */
    default byte[] getAsByteArray() {
        byte[] array = new byte[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsByte();
        }
        return array;
    }

    /**
     * Casts this array into a short array
     *
     * @return the array
     */
    default short[] getAsShortArray() {
        short[] array = new short[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsShort();
        }
        return array;
    }

    /**
     * Casts this array into an int array
     *
     * @return the array
     */
    default int[] getAsIntArray() {
        int[] array = new int[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsInt();
        }
        return array;
    }

    /**
     * Casts this array into a long array
     *
     * @return the array
     */
    default long[] getAsLongArray() {
        long[] array = new long[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsLong();
        }
        return array;
    }

    /**
     * Casts this array into a float array
     *
     * @return the array
     */
    default float[] getAsFloatArray() {
        float[] array = new float[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsFloat();
        }
        return array;
    }

    /**
     * Casts this array into a double array
     *
     * @return the array
     */
    default double[] getAsDoubleArray() {
        double[] array = new double[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsDouble();
        }
        return array;
    }

    /**
     * Casts this array into a string array
     *
     * @return the array
     */
    default String[] getAsStringArray() {
        return stream().map(SerializationElement::getAsString).toArray(String[]::new);
    }

    /**
     * Casts this array into an array of arrays
     *
     * @return the array
     */
    default SerializationArray[] getAsArrayOfArrays() {
        return stream().map(SerializationElement::getAsArray).toArray(SerializationArray[]::new);
    }

    /**
     * Casts this array into a container array
     *
     * @return the array
     */
    default SerializationContainer[] getAsContainerArray() {
        return stream().map(SerializationElement::getAsContainer).toArray(SerializationContainer[]::new);
    }
}
