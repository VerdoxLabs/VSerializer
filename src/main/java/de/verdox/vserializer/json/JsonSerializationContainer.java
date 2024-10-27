package de.verdox.vserializer.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.verdox.vserializer.generic.SerializationContainer;
import de.verdox.vserializer.generic.SerializationElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class JsonSerializationContainer extends JsonSerializationElement implements SerializationContainer {
    public JsonSerializationContainer(JsonSerializerContext serializerContext, JsonObject jsonElement) {
        super(serializerContext, jsonElement);
    }

    @Override
    public Collection<String> getChildKeys() {
        return jsonElement.getAsJsonObject().keySet();
    }

    @Override
    public @NotNull SerializationElement get(String key) {
        JsonElement jsonChild = jsonElement.getAsJsonObject().get(key);
        if (jsonChild == null || jsonChild.isJsonNull())
            return new JsonSerializationNull(getContext());
        else if (jsonChild.isJsonObject())
            return new JsonSerializationContainer(getContext(), jsonChild.getAsJsonObject());
        else if (jsonChild.isJsonArray())
            return new JsonSerializationArray(getContext(), jsonChild.getAsJsonArray());
        else if (jsonChild.isJsonPrimitive())
            return new JsonSerializationPrimitive(getContext(), jsonChild.getAsJsonPrimitive());
        throw new RuntimeException("The child object with key " + key + " is not: container, array, primitive, null. This is a bug!");
    }

    @Override
    public boolean contains(String key) {
        return jsonElement.getAsJsonObject().has(key);
    }

    @Override
    public void set(String key, SerializationElement serializationElement) {
        getJsonElement().add(key, ((JsonSerializationElement) serializationElement).jsonElement);
    }

    @Override
    public JsonObject getJsonElement() {
        return super.getJsonElement().getAsJsonObject();
    }
}
