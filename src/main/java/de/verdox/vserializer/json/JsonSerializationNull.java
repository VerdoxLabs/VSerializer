package de.verdox.vserializer.json;

import com.google.gson.JsonNull;
import de.verdox.vserializer.generic.SerializationNull;

public class JsonSerializationNull extends JsonSerializationElement implements SerializationNull {
    JsonSerializationNull(JsonSerializerContext serializerContext) {
        super(serializerContext, JsonNull.INSTANCE);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof JsonSerializationNull;
    }
}
