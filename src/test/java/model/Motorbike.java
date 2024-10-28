package model;

import de.verdox.vserializer.generic.Serializer;
import de.verdox.vserializer.generic.SerializerBuilder;

public class Motorbike implements GroundVehicle {
    public static final Serializer<Motorbike> SERIALIZER = SerializerBuilder.createObjectToPrimitiveSerializer("motorbike", Motorbike.class, Serializer.Primitive.STRING, motorbike -> "motorbike", s -> new Motorbike());
    @Override
    public int getWheels() {
        return 2;
    }
}
