package de.verdox.vserializer.json;

import com.google.gson.JsonElement;
import de.verdox.vserializer.generic.SerializationArray;
import de.verdox.vserializer.generic.SerializationContainer;
import de.verdox.vserializer.generic.SerializationElement;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Experimental
public class JsonSerializationElement implements SerializationElement {
    protected final JsonElement jsonElement;

    public JsonSerializationElement(JsonElement jsonElement) {
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
    public SerializationElement getAsElement() {
        return this;
    }

    @Override
    public SerializationContainer getAsContainer() {
        return new JsonSerializationContainer(jsonElement.getAsJsonObject());
    }

    @Override
    public SerializationArray getAsArray() {
        return new JsonSerializationArray(jsonElement.getAsJsonArray());
    }
}
