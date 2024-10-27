package json;

import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.json.JsonSerializerContext;
import test.SerializerTest;

public class JsonSerializerTests extends SerializerTest {
    private static final SerializationContext SERIALIZATION_CONTEXT = new JsonSerializerContext();

    @Override
    public SerializationContext context() {
        return SERIALIZATION_CONTEXT;
    }
}
