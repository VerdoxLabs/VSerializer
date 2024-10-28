package de.verdox.vserializer.generic;

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

    @Override
    default boolean isArray() {
        return true;
    }
}
