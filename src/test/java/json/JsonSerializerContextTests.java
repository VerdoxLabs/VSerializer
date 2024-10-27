package json;

import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.json.JsonSerializerContext;
import test.SerializerContextTests;

public class JsonSerializerContextTests extends SerializerContextTests {
    @Override
    public SerializationContext context() {
        return new JsonSerializerContext();
    }
}
