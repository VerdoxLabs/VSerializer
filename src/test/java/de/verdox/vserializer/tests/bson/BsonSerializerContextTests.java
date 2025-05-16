package de.verdox.vserializer.tests.bson;

import de.verdox.vserializer.bson.BsonSerializerContext;
import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.tests.test.SerializerContextTests;

public class BsonSerializerContextTests extends SerializerContextTests {
    private static final SerializationContext SERIALIZATION_CONTEXT = new BsonSerializerContext();

    @Override
    public SerializationContext context() {
        return SERIALIZATION_CONTEXT;
    }
}
