package de.verdox.vserializer.generic;

import java.io.File;
import java.io.IOException;

/**
 * A serialization context implements the representation of the various serialization elements.
 */
public interface SerializationContext {
    /**
     * Creates a context specific primitive from a primitive value
     *
     * @param value the primitive value
     * @return the serialized primitive
     */
    SerializationPrimitive create(boolean value);

    /**
     * Creates a context specific primitive from a primitive value
     *
     * @param value the primitive value
     * @return the serialized primitive
     */
    SerializationPrimitive create(char value);

    /**
     * Creates a context specific primitive from a primitive value
     *
     * @param value the primitive value
     * @return the serialized primitive
     */
    SerializationPrimitive create(String value);

    /**
     * Creates a context specific primitive from a primitive value
     *
     * @param value the primitive value
     * @return the serialized primitive
     */
    SerializationPrimitive create(Number value);

    /**
     * Creates a context specific primitive from a primitive value
     *
     * @param value the primitive value
     * @return the serialized primitive
     */
    SerializationPrimitive create(byte value);

    /**
     * Creates a context specific primitive from a primitive value
     *
     * @param value the primitive value
     * @return the serialized primitive
     */
    SerializationPrimitive create(short value);

    /**
     * Creates a context specific primitive from a primitive value
     *
     * @param value the primitive value
     * @return the serialized primitive
     */
    SerializationPrimitive create(int value);

    /**
     * Creates a context specific primitive from a primitive value
     *
     * @param value the primitive value
     * @return the serialized primitive
     */
    SerializationPrimitive create(long value);

    /**
     * Creates a context specific primitive from a primitive value
     *
     * @param value the primitive value
     * @return the serialized primitive
     */
    SerializationPrimitive create(float value);

    /**
     * Creates a context specific primitive from a primitive value
     *
     * @param value the primitive value
     * @return the serialized primitive
     */
    SerializationPrimitive create(double value);

    /**
     * Creates context specific element for a primitive array
     */
    default SerializationArray create(boolean[] array) {
        SerializationArray serializationArray = createArray();
        for (boolean value : array) {
            serializationArray.add(create(value));
        }
        return serializationArray;
    }

    /**
     * Creates context specific element for a primitive array
     */
    default SerializationArray create(byte[] array) {
        SerializationArray serializationArray = createArray();
        for (byte value : array) {
            serializationArray.add(create(value));
        }
        return serializationArray;
    }

    /**
     * Creates context specific element for a primitive array
     */
    default SerializationArray create(short[] array) {
        SerializationArray serializationArray = createArray();
        for (short value : array) {
            serializationArray.add(create(value));
        }
        return serializationArray;
    }

    /**
     * Creates context specific element for a primitive array
     */
    default SerializationArray create(int[] array) {
        SerializationArray serializationArray = createArray();
        for (int value : array) {
            serializationArray.add(create(value));
        }
        return serializationArray;
    }

    /**
     * Creates context specific element for a primitive array
     */
    default SerializationArray create(long[] array) {
        SerializationArray serializationArray = createArray();
        for (long value : array) {
            serializationArray.add(create(value));
        }
        return serializationArray;
    }

    /**
     * Creates context specific element for a primitive array
     */
    default SerializationArray create(float[] array) {
        SerializationArray serializationArray = createArray();
        for (float value : array) {
            serializationArray.add(create(value));
        }
        return serializationArray;
    }

    /**
     * Creates context specific element for a primitive array
     */
    default SerializationArray create(double[] array) {
        SerializationArray serializationArray = createArray();
        for (double value : array) {
            serializationArray.add(create(value));
        }
        return serializationArray;
    }

    /**
     * Creates a context specific array
     *
     * @param length the length of the array
     * @return the serialized primitive
     */
    SerializationArray createArray(int length);

    /**
     * Creates a context specific dynamic array with provided values as start values.
     *
     * @param elements the starting elements
     * @return the serialized primitive
     */
    SerializationArray createArray(SerializationElement... elements);

    /**
     * Creates a context specific container
     *
     * @return the container
     */
    SerializationContainer createContainer();

    /**
     * Creates a context specific null value
     *
     * @return the null value
     */
    SerializationNull createNull();

    /**
     * Writes a serialization element into a file
     *
     * @param element the element to save
     * @param file    the file
     * @throws IOException if any io exception happens
     */
    void writeToFile(SerializationElement element, File file) throws IOException;

    /**
     * Reads a serialization element from a file
     *
     * @param file the file
     * @return the element read from the file
     * @throws IOException if any io exception happens
     */
    SerializationElement readFromFile(File file) throws IOException;

    /**
     * Converts a {@link SerializationElement} from possibly another {@link SerializationContext} into a {@link SerializationElement} from this {@link SerializationContext}.
     *
     * @param element the serialization element from another serializer context
     * @param force   whether the conversion should happen, regardless of the context already being right
     * @return the element in this serializer context
     */
    default SerializationElement convert(SerializationElement element, boolean force) {
        if (!force && element.getContext().equals(this))
            return element;
        if (element.isNull())
            return createNull();
        else if (element.isPrimitive()) {
            SerializationPrimitive primitive = element.getAsPrimitive();
            if (primitive.isBoolean())
                return create(primitive.getAsBoolean());
            else if (primitive.isString())
                return create(primitive.getAsString());
            else if (primitive.isNumber())
                return create(primitive.getAsNumber());
        } else if (element.isArray()) {
            SerializationArray serializationArray = element.getAsArray();
            SerializationElement[] array = new SerializationElement[serializationArray.length()];
            int counter = 0;
            for (SerializationElement serializationElement : serializationArray) {
                array[counter++] = convert(serializationElement, force);
            }
            return createArray(array);
        } else if (element.isContainer()) {
            SerializationContainer containerWithRightContext = createContainer();
            SerializationContainer container = element.getAsContainer();
            for (String childKey : container.getChildKeys()) {
                SerializationElement child = container.get(childKey);
                containerWithRightContext.set(childKey, convert(child, force));
            }
            return containerWithRightContext;
        }
        throw new UnsupportedOperationException("The provided element is no container, array, primitive, or null. This is a bug!");
    }
}
