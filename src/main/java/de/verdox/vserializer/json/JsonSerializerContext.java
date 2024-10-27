package de.verdox.vserializer.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.verdox.vserializer.generic.*;
import de.verdox.vserializer.util.gson.JsonUtil;

import java.io.File;
import java.io.IOException;

public class JsonSerializerContext implements SerializationContext {
    @Override
    public JsonSerializationPrimitive create(boolean value) {
        return new JsonSerializationPrimitive(this, new JsonPrimitive(value));
    }

    @Override
    public JsonSerializationPrimitive create(char value) {
        return new JsonSerializationPrimitive(this, new JsonPrimitive(value));
    }

    @Override
    public JsonSerializationPrimitive create(String value) {
        return new JsonSerializationPrimitive(this, new JsonPrimitive(value));
    }

    @Override
    public JsonSerializationPrimitive create(Number value) {
        return new JsonSerializationPrimitive(this, new JsonPrimitive(value));
    }

    @Override
    public JsonSerializationPrimitive create(byte value) {
        return new JsonSerializationPrimitive(this, new JsonPrimitive(value));
    }

    @Override
    public JsonSerializationPrimitive create(short value) {
        return new JsonSerializationPrimitive(this, new JsonPrimitive(value));
    }

    @Override
    public JsonSerializationPrimitive create(int value) {
        return new JsonSerializationPrimitive(this, new JsonPrimitive(value));
    }

    @Override
    public JsonSerializationPrimitive create(long value) {
        return new JsonSerializationPrimitive(this, new JsonPrimitive(value));
    }

    @Override
    public JsonSerializationPrimitive create(float value) {
        return new JsonSerializationPrimitive(this, new JsonPrimitive(value));
    }

    @Override
    public JsonSerializationPrimitive create(double value) {
        return new JsonSerializationPrimitive(this, new JsonPrimitive(value));
    }

    @Override
    public JsonSerializationArray createArray(int length) {
        return new JsonSerializationArray(this, new JsonArray(length));
    }

    @Override
    public JsonSerializationArray createArray(SerializationElement... elements) {
        JsonSerializationArray jsonSerializationArray = new JsonSerializationArray(this, new JsonArray());
        for (SerializationElement element : elements)
            jsonSerializationArray.add(element);
        return jsonSerializationArray;
    }

    @Override
    public JsonSerializationContainer createContainer() {
        return new JsonSerializationContainer(this, new JsonObject());
    }

    @Override
    public SerializationNull createNull() {
        return new JsonSerializationNull(this);
    }

    @Override
    public void writeToFile(SerializationElement serializationElement, File file) throws IOException {
        if(serializationElement instanceof JsonSerializationElement jsonSerializationElement && jsonSerializationElement.jsonElement.isJsonObject())
            JsonUtil.writeJsonObjectToFile(jsonSerializationElement.jsonElement.getAsJsonObject(), file);
    }
}
