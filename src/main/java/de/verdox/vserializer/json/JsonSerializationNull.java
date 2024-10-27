package de.verdox.vserializer.json;

import com.google.gson.JsonNull;
import de.verdox.vserializer.generic.SerializationNull;

public class JsonSerializationNull extends JsonSerializationElement implements SerializationNull {
    public JsonSerializationNull(JsonSerializerContext serializerContext) {
        super(serializerContext, JsonNull.INSTANCE);
    }
}
