package de.verdox.vserializer.bson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.verdox.vserializer.json.JsonSerializationContainer;
import de.verdox.vserializer.json.JsonSerializationElement;
import de.verdox.vserializer.json.JsonSerializerContext;

public class BsonSerializerContext extends JsonSerializerContext {
    @Override
    public JsonSerializationContainer createContainer() {
        return new BsonSerializationContainer(this, new JsonObject());
    }

    @Override
    public JsonSerializationElement toElement(JsonElement jsonElement) {
        if (jsonElement.isJsonObject())
            return new BsonSerializationContainer(this, jsonElement.getAsJsonObject());
        return super.toElement(jsonElement);
    }
}
