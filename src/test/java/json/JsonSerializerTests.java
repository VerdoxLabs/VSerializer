package json;

import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.json.JsonSerializerContext;
import test.SerializerTests;

public class JsonSerializerTests extends SerializerTests {
    private static final SerializationContext SERIALIZATION_CONTEXT = new JsonSerializerContext();

    @Override
    public SerializationContext context() {
        return SERIALIZATION_CONTEXT;
    }
}
