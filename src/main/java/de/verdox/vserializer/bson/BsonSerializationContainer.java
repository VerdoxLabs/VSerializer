package de.verdox.vserializer.bson;

import com.google.gson.JsonObject;
import de.verdox.vserializer.generic.SerializationElement;
import de.verdox.vserializer.json.JsonSerializationContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class BsonSerializationContainer extends JsonSerializationContainer {
    public BsonSerializationContainer(BsonSerializerContext serializerContext, JsonObject jsonElement) {
        super(serializerContext, jsonElement);
    }

    @Override
    public Collection<String> getChildKeys() {
        return super.getChildKeys().stream().map(this::fromBsonConformKey).toList();
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

    private String toBsonConformKey(String key) {
        if (key.startsWith("$")) {
            key = "___dollar___" + key.substring(1);
        }
        return key.replace(".", "___dot___");
    }

    private String fromBsonConformKey(String key) {
        if (key.startsWith("___dollar___")) {
            key = "$" + key.substring("___dollar___".length());
        }
        return key.replace("___dot___", ".");
    }
}
