package bson;

import com.google.gson.JsonObject;
import de.verdox.vserializer.bson.BsonSerializationContainer;
import de.verdox.vserializer.bson.BsonSerializerContext;
import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.json.JsonSerializationElement;
import de.verdox.vserializer.util.gson.JsonObjectBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import test.SerializerTests;

import java.io.IOException;

public class BsonSerializerTests extends SerializerTests {
    private static final SerializationContext SERIALIZATION_CONTEXT = new BsonSerializerContext();

    @Override
    public SerializationContext context() {
        return SERIALIZATION_CONTEXT;
    }

/*    @Test
    public void testConversionOfInvalidKeys() throws IOException {

        JsonObject jsonObject = JsonObjectBuilder.create()
                .add("invalid.key", "invalid_value")
                .add("$invalid.key", "invalid_value")
                .add("invalid.key2", JsonObjectBuilder.create()
                        .add("$invalid.key", "invalid_value"))
                .build();

        BsonSerializerContext bsonSerializerContext = (BsonSerializerContext) context();
        JsonSerializationElement jsonSerializationElement = bsonSerializerContext.toElement(jsonObject);
    }*/

    @Test
    public void testToBsonConformKey() {
        String convertedKey = BsonSerializationContainer.toBsonConformKey("invalid.key");
        Assertions.assertEquals("invalid___dot___key", convertedKey);
    }
}
