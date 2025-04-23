package de.verdox.vserializer.generic;

import de.verdox.vserializer.AbstractSerializableField;

public interface SerializableFieldBuilder<T, S> {
    AbstractSerializableField<T, S> build(Serializer<T> serializer);
}
