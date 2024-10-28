package model;

import de.verdox.vserializer.generic.Serializer;

public interface GroundVehicle {
    Serializer<GroundVehicle> VEHICLE_SELECTION_SERIALIZER = Serializer.Selection.create("vehicle_selection", GroundVehicle.class)
            .variant("car", Car.SERIALIZER, new Car())
            .variant("motorbike", Motorbike.SERIALIZER, new Motorbike());
    int getWheels();
}
