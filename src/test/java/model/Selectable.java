package model;

import de.verdox.vserializer.SerializableField;
import de.verdox.vserializer.generic.Serializer;
import de.verdox.vserializer.generic.SerializerBuilder;

public record Selectable(String name, boolean value) {
    public static final Serializer<Selectable> SERIALIZER = SerializerBuilder.create("selectable", Selectable.class)
            .constructor(
                    new SerializableField<>("name", Serializer.Primitive.STRING, Selectable::name),
                    new SerializableField<>("value", Serializer.Primitive.BOOLEAN, Selectable::value),
                    Selectable::new
            )
            .build();
}
