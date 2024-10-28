package de.verdox.vserializer.blank;

import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.SerializationElement;

/**
 * A blank serialization element is not wrapping any serialization dependent object (like {@link com.google.gson.JsonElement}).
 * All information is stored inside the blank element.
 * This class is useful when you want to implement your own SerializationElements but want to do the conversion to serialization specific types at a later stage.
 *
 */
public abstract class BlankSerializationElement<C extends SerializationContext> implements SerializationElement {
    private final C serializationContext;

    BlankSerializationElement(C serializationContext){
        this.serializationContext = serializationContext;
    }

    @Override
    public boolean getAsBoolean() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public String getAsString() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public char getAsCharacter() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public Number getAsNumber() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public double getAsDouble() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public float getAsFloat() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public long getAsLong() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public int getAsInt() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public short getAsShort() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public byte getAsByte() {
        throw new UnsupportedOperationException(getClass().getSimpleName());
    }

    @Override
    public C getContext() {
        return serializationContext;
    }
}
