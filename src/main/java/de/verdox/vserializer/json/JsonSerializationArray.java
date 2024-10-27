package de.verdox.vserializer.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import de.verdox.vserializer.generic.SerializationArray;
import de.verdox.vserializer.generic.SerializationElement;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class JsonSerializationArray extends JsonSerializationElement implements SerializationArray {
    public JsonSerializationArray(JsonSerializerContext serializerContext, JsonArray jsonElement) {
        super(serializerContext, jsonElement);
    }

    @Override
    public int length() {
        return jsonElement.getAsJsonArray().size();
    }

    @Override
    public JsonSerializationElement get(int index) {
        return new JsonSerializationElement(getContext(), jsonElement.getAsJsonArray().get(index));
    }

    @Override
    public void add(SerializationElement serializationElement) {
        jsonElement.getAsJsonArray().add(((JsonSerializationElement) serializationElement).jsonElement);
    }

    @Override

    public void set(int index, SerializationElement serializationElement) {
        jsonElement.getAsJsonArray().set(index, ((JsonSerializationElement) serializationElement).jsonElement);
    }

    @Override
    public SerializationElement remove(int index) {
        return new JsonSerializationElement(getContext(), jsonElement.getAsJsonArray().remove(index));
    }

    @Override
    public void addAll(SerializationArray serializationArray) {
        for (SerializationElement serializationElement : serializationArray) {
            add(serializationElement);
        }
    }

    @NotNull
    @Override
    public Iterator<SerializationElement> iterator() {
        Iterator<JsonElement> iterator = jsonElement.getAsJsonArray().iterator();
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return iterator.hasNext();
            }

            @Override
            public SerializationElement next() {
                return new JsonSerializationElement(getContext(), iterator.next());
            }

            @Override
            public void remove() {
                iterator.remove();
            }
        };
    }

    @Override
    public JsonArray getJsonElement() {
        return super.getJsonElement().getAsJsonArray();
    }
}
