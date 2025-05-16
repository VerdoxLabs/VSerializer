package de.verdox.vserializer.tests.model;

import de.verdox.vserializer.generic.Serializer;
import de.verdox.vserializer.generic.SerializerBuilder;

public class Car implements GroundVehicle {
    public static final Serializer<Car> SERIALIZER = SerializerBuilder.createObjectToPrimitiveSerializer("car", Car.class, Serializer.Primitive.STRING, car -> "car", s -> new Car());
    @Override
    public int getWheels() {
        return 4;
    }
}
