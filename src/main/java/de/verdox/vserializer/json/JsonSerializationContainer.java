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
        return getContext().toElement(jsonChild);
    }

    @Override
    public boolean contains(String key) {
        return jsonElement.getAsJsonObject().has(key);
    }

    @Override
    public void set(String key, SerializationElement serializationElement) {
        getJsonElement().add(key, ((JsonSerializationElement) getContext().convert(serializationElement, false)).jsonElement);
    }

    @Override
    public void remove(String key) {
        getJsonElement().remove(key);
    }

    @Override
    public JsonObject getJsonElement() {
        return super.getJsonElement().getAsJsonObject();
    }
}
