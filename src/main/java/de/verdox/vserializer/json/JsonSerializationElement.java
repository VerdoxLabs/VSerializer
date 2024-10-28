package de.verdox.vserializer.json;

import com.google.gson.JsonElement;
import de.verdox.vserializer.generic.SerializationArray;
import de.verdox.vserializer.generic.SerializationContainer;
import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.SerializationElement;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

@ApiStatus.Experimental
public class JsonSerializationElement implements SerializationElement {
    private final JsonSerializerContext serializerContext;
    protected final JsonElement jsonElement;

    JsonSerializationElement(JsonSerializerContext serializerContext, JsonElement jsonElement) {
        this.serializerContext = serializerContext;
        this.jsonElement = jsonElement;
    }

    @Override
    public boolean getAsBoolean() {
        return jsonElement.getAsBoolean();
    }

    @Override
    public String getAsString() {
        return jsonElement.getAsString();
    }

    @Override
    public char getAsCharacter() {
        return jsonElement.getAsCharacter();
    }

    @Override
    public Number getAsNumber() {
        return jsonElement.getAsNumber();
    }

    @Override
    public double getAsDouble() {
        return jsonElement.getAsDouble();
    }

    @Override
    public float getAsFloat() {
        return jsonElement.getAsFloat();
    }

    @Override
    public long getAsLong() {
        return jsonElement.getAsLong();
    }

    @Override
    public int getAsInt() {
        return jsonElement.getAsInt();
    }

    @Override
    public short getAsShort() {
        return jsonElement.getAsShort();
    }

    @Override
    public byte getAsByte() {
        return jsonElement.getAsByte();
    }

    @Override
    public JsonSerializationElement getAsElement() {
        return this;
    }

    @Override
    public SerializationContainer getAsContainer() {
        return new JsonSerializationContainer(getContext(), jsonElement.getAsJsonObject());
    }

    @Override
    public SerializationArray getAsArray() {
        return new JsonSerializationArray(getContext(), jsonElement.getAsJsonArray());
    }

    @Override
    public JsonSerializerContext getContext() {
        return serializerContext;
    }

    public JsonElement getJsonElement() {
        return jsonElement;
    }

    @Override
    public String toString() {
        return "JsonSerializationElement{" +
                "jsonElement=" + jsonElement +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JsonSerializationElement that = (JsonSerializationElement) o;
        return Objects.equals(jsonElement, that.jsonElement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(jsonElement);
    }
}
