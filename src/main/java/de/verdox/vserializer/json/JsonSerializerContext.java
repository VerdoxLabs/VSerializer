package de.verdox.vserializer.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.SerializationElement;
import de.verdox.vserializer.generic.SerializationNull;
import de.verdox.vserializer.util.gson.JsonUtil;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

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
        return createContainer(new JsonObject());
    }

    public JsonSerializationContainer createContainer(JsonObject jsonObject) {
        return new JsonSerializationContainer(this, jsonObject);
    }

    public JsonSerializationArray createArray(JsonArray jsonElements) {
        return new JsonSerializationArray(this, jsonElements);
    }

    @Override
    public SerializationNull createNull() {
        return new JsonSerializationNull(this);
    }

    @Override
    public void writeToFile(SerializationElement serializationElement, File file) throws IOException {
        if (serializationElement instanceof JsonSerializationElement jsonSerializationElement && jsonSerializationElement.jsonElement.isJsonObject()) {
            JsonUtil.writeJsonObjectToFile(jsonSerializationElement.jsonElement.getAsJsonObject(), file);
        } else {
            throw new IllegalArgumentException("The provided serialization element was not created by a json context.");
        }
    }

    @Override
    public SerializationElement readFromFile(File file) throws IOException {
        return toElement(JsonUtil.readJsonFromFile(file));
    }

    public String toJsonString(SerializationElement serializationElement) {
        if (serializationElement instanceof JsonSerializationElement jsonSerializationElement) {
            return JsonUtil.toJsonString(jsonSerializationElement.jsonElement);
        }
        throw new IllegalArgumentException("The provided serialization element was not deserialized by a json context.");
    }

    public SerializationElement fromJsonString(String jsonString) {
        return toElement(JsonUtil.readFromString(jsonString));
    }

    public JsonSerializationElement toElement(JsonElement jsonElement) {
        if (jsonElement == null || jsonElement.isJsonNull()) {
            return new JsonSerializationNull(this);
        } else if (jsonElement.isJsonObject()) {
            return new JsonSerializationContainer(this, jsonElement.getAsJsonObject());
        } else if (jsonElement.isJsonArray()) {
            return new JsonSerializationArray(this, jsonElement.getAsJsonArray());
        } else if (jsonElement.isJsonPrimitive()) {
            return new JsonSerializationPrimitive(this, jsonElement.getAsJsonPrimitive());
        }
        throw new RuntimeException("The child object " + jsonElement + " is not: container, array, primitive, null. This is a bug!");
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof JsonSerializerContext;
    }
}
