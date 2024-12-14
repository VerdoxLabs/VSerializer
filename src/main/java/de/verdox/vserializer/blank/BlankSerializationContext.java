package de.verdox.vserializer.blank;

import de.verdox.vserializer.generic.*;

import java.io.File;

/**
 * A blank implementation of a serialization context.
 * Check out {@link BlankSerializationElement} for further information
 */
public class BlankSerializationContext implements SerializationContext {
    @Override
    public SerializationPrimitive create(boolean value) {
        return new BlankSerializationPrimitive(this, value);
    }

    @Override
    public SerializationPrimitive create(char value) {
        return new BlankSerializationPrimitive(this, value);
    }

    @Override
    public SerializationPrimitive create(String value) {
        return new BlankSerializationPrimitive(this, value);
    }

    @Override
    public SerializationPrimitive create(Number value) {
        return new BlankSerializationPrimitive(this, value);
    }

    @Override
    public SerializationPrimitive create(byte value) {
        return new BlankSerializationPrimitive(this, value);
    }

    @Override
    public SerializationPrimitive create(short value) {
        return new BlankSerializationPrimitive(this, value);
    }

    @Override
    public SerializationPrimitive create(int value) {
        return new BlankSerializationPrimitive(this, value);
    }

    @Override
    public SerializationPrimitive create(long value) {
        return new BlankSerializationPrimitive(this, value);
    }

    @Override
    public SerializationPrimitive create(float value) {
        return new BlankSerializationPrimitive(this, value);
    }

    @Override
    public SerializationPrimitive create(double value) {
        return new BlankSerializationPrimitive(this, value);
    }

    @Override
    public SerializationArray createArray(int length) {
        return new BlankSerializationArray(this);
    }

    @Override
    public SerializationArray createArray(SerializationElement... elements) {
        return new BlankSerializationArray(this, elements);
    }

    @Override
    public SerializationContainer createContainer() {
        return new BlankSerializationContainer(this);
    }

    @Override
    public SerializationNull createNull() {
        return new BlankSerializationNull(this);
    }

    @Override
    public void writeToFile(SerializationElement element, File file) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SerializationElement readFromFile(File file) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BlankSerializationContext && obj.getClass().equals(this.getClass());
    }
}
