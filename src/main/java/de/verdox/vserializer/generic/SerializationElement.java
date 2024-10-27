package de.verdox.vserializer.generic;

public interface SerializationElement {
    boolean getAsBoolean();

    String getAsString();

    char getAsCharacter();

    Number getAsNumber();

    double getAsDouble();

    float getAsFloat();

    long getAsLong();

    int getAsInt();

    short getAsShort();

    byte getAsByte();

    default SerializationElement getAsElement(){
        return this;
    }

    default SerializationContainer getAsContainer() {
        throw new UnsupportedOperationException();
    }

    default SerializationArray getAsArray() {
        throw new UnsupportedOperationException();
    }

    default SerializationPrimitive getAsPrimitive() {
        throw new UnsupportedOperationException();
    }

    default boolean isPrimitive(){
        return false;
    }

    default boolean isContainer(){
        return false;
    }

    default boolean isArray(){
        return false;
    }

    default boolean isNull(){
        return false;
    }

    SerializationContext getContext();
}
