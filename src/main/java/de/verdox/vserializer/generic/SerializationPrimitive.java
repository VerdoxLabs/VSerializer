package de.verdox.vserializer.generic;

public interface SerializationPrimitive extends SerializationElement {
    boolean isBoolean();

    boolean isNumber();

    boolean isString();

    default boolean isCharacter(){
        return isString() && getAsString().length() == 1;
    }

    default boolean isByte(){
        return isNumber() && getAsNumber() instanceof Byte;
    }

    default boolean isShort(){
        return isNumber() && getAsNumber() instanceof Short;
    }

    default boolean isInteger(){
        return isNumber() && getAsNumber() instanceof Integer;
    }

    default boolean isLong(){
        return isNumber() && getAsNumber() instanceof Long;
    }

    default boolean isFloat(){
        return isNumber() && getAsNumber() instanceof Float;
    }

    default boolean isDouble(){
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
