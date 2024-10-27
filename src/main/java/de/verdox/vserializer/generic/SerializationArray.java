package de.verdox.vserializer.generic;

public interface SerializationArray extends SerializationElement, Iterable<SerializationElement> {
    int length();

    SerializationElement get(int index);

    void add(SerializationElement serializationElement);

    void set(int index, SerializationElement serializationElement);

    SerializationElement remove(int index);

    void addAll(SerializationArray serializationArray);

    @Override
    default SerializationArray getAsArray(){
        return this;
    }

    @Override
    default boolean isArray() {
        return true;
    }
}
