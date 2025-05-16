package de.verdox.vserializer.bson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.verdox.vserializer.generic.SerializationElement;
import de.verdox.vserializer.json.JsonSerializationContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Set;

public class BsonSerializationContainer extends JsonSerializationContainer {
    public BsonSerializationContainer(BsonSerializerContext serializerContext, JsonObject jsonElement) {
        super(serializerContext, convertKeys(jsonElement));
    }

    @Override
    public Collection<String> getChildKeys() {
        return super.getChildKeys().stream().map(BsonSerializationContainer::fromBsonConformKey).toList();
    }

    @Override
    public @NotNull SerializationElement get(String key) {
        return super.get(toBsonConformKey(key));
    }

    @Override
    public boolean contains(String key) {
        return super.contains(toBsonConformKey(key));
    }

    @Override
    public void set(String key, SerializationElement serializationElement) {
        super.set(toBsonConformKey(key), serializationElement);
    }

    @Override
    public void remove(String key) {
        super.remove(toBsonConformKey(key));
    }

    public static JsonObject convertKeys(JsonObject jsonObject) {
        for (String s : Set.copyOf(jsonObject.keySet())) {
            JsonElement removed = jsonObject.remove(s);
            if(removed.isJsonObject()) {
                removed = convertKeys(removed.getAsJsonObject());
            }
            jsonObject.add(toBsonConformKey(s), removed);
        }
        return jsonObject;
    }

    public static String toBsonConformKey(String key) {
        if (key.startsWith("$")) {
            key = "___dollar___" + key.substring(1);
        }
        return key.replace(".", "___dot___");
    }

    public static String fromBsonConformKey(String key) {
        if (key.startsWith("___dollar___")) {
            key = "$" + key.substring("___dollar___".length());
        }
        return key.replace("___dot___", ".");
    }
}
