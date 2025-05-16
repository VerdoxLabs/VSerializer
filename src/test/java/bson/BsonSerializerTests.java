package bson;

import de.verdox.vserializer.bson.BsonSerializerContext;
import de.verdox.vserializer.generic.SerializationContext;
import test.SerializerTests;

public class BsonSerializerTests extends SerializerTests {
    private static final SerializationContext SERIALIZATION_CONTEXT = new BsonSerializerContext();

    @Override
    public SerializationContext context() {
        return SERIALIZATION_CONTEXT;
    }
}
