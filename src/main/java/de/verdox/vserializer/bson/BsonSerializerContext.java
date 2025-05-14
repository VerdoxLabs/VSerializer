package de.verdox.vserializer.bson;

import com.google.gson.JsonObject;
import de.verdox.vserializer.json.JsonSerializationContainer;
import de.verdox.vserializer.json.JsonSerializerContext;

public class BsonSerializerContext extends JsonSerializerContext {
    @Override
    public JsonSerializationContainer createContainer() {
        return new BsonSerializationContainer(this, new JsonObject());
    }
}
