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
}
