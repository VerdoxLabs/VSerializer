package de.verdox.vserializer.generic;

import de.verdox.vserializer.SerializableField;

public interface SerializableFieldBuilder<T, S> {
    SerializableField<T, S> build(Serializer<T> serializer);
}
