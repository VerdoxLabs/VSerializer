package de.verdox.vserializer.generic;

import java.io.File;
import java.io.IOException;

public interface SerializationContext {
    SerializationPrimitive create(boolean value);

    SerializationPrimitive create(char value);

    SerializationPrimitive create(String value);

    SerializationPrimitive create(Number value);

    SerializationPrimitive create(byte value);

    SerializationPrimitive create(short value);

    SerializationPrimitive create(int value);

    SerializationPrimitive create(long value);

    SerializationPrimitive create(float value);

    SerializationPrimitive create(double value);

    SerializationArray createArray(int length);

    SerializationArray createArray(SerializationElement... elements);

    SerializationContainer createContainer();

    SerializationNull createNull();

    void writeToFile(SerializationElement element, File file) throws IOException;
    SerializationElement readFromFile(File file) throws IOException;

    /**
     * Converts a {@link SerializationElement} into a {@link SerializationElement} in this {@link SerializationContext}
     *
     * @param element the serialization element from another serializer context
     * @param force whether the conversion should happen, regardless of the context already being right
     * @return the element in this serializer context
     */
    default SerializationElement convert(SerializationElement element, boolean force) {
        if(!force && element.getContext().equals(this))
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
