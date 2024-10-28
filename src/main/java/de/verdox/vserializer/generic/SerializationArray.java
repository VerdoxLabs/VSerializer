package de.verdox.vserializer.generic;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface SerializationArray extends SerializationElement, Iterable<SerializationElement> {
    int length();

    SerializationElement get(int index);

    void add(SerializationElement serializationElement);

    void set(int index, SerializationElement serializationElement);

    SerializationElement remove(int index);

    default void addAll(SerializationArray serializationArray){
        for (SerializationElement element : serializationArray) {
            add(element);
        }
    }

    @Override
    default SerializationArray getAsArray(){
        return this;
    }

    default Stream<SerializationElement> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    default boolean isArray() {
        return true;
    }



    default boolean isBoolArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isBoolean())
                return false;
        }
        return true;
    }

    default boolean isCharArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isCharacter())
                return false;
        }
        return true;
    }

    default boolean isStringArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isString())
                return false;
        }
        return true;
    }

    default boolean isByteArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isByte())
                return false;
        }
        return true;
    }

    default boolean isShortArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isShort())
                return false;
        }
        return true;
    }

    default boolean isIntArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isInteger())
                return false;
        }
        return true;
    }

    default boolean isFloatArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isFloat())
                return false;
        }
        return true;
    }

    default boolean isDoubleArray() {
        for (SerializationElement element : this) {
            if (!element.isPrimitive() || !element.getAsPrimitive().isDouble())
                return false;
        }
        return true;
    }

    default boolean isArrayOfArrays() {
        for (SerializationElement element : this) {
            if (!element.isArray())
                return false;
        }
        return true;
    }

    default boolean isContainerArray() {
        for (SerializationElement element : this) {
            if (!element.isContainer())
                return false;
        }
        return true;
    }

    default boolean[] getAsBooleanArray() {
        boolean[] array = new boolean[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsBoolean();
        }
        return array;
    }

    default char[] getAsCharArray() {
        char[] array = new char[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsCharacter();
        }
        return array;
    }

    default byte[] getAsByteArray() {
        byte[] array = new byte[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsByte();
        }
        return array;
    }

    default short[] getAsShortArray() {
        short[] array = new short[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsShort();
        }
        return array;
    }

    default int[] getAsIntArray() {
        int[] array = new int[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsInt();
        }
        return array;
    }

    default long[] getAsLongArray() {
        long[] array = new long[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsLong();
        }
        return array;
    }

    default float[] getAsFloatArray() {
        float[] array = new float[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsFloat();
        }
        return array;
    }

    default double[] getAsDoubleArray() {
        double[] array = new double[length()];
        int counter = 0;
        for (SerializationElement element : this) {
            array[counter++] = element.getAsDouble();
        }
        return array;
    }

    default String[] getAsStringArray() {
        return stream().map(SerializationElement::getAsString).toArray(String[]::new);
    }

    default SerializationArray[] getAsArrayOfArrays() {
        return stream().map(SerializationElement::getAsArray).toArray(SerializationArray[]::new);
    }

    default SerializationContainer[] getAsContainerArray() {
        return stream().map(SerializationElement::getAsContainer).toArray(SerializationContainer[]::new);
    }
}
