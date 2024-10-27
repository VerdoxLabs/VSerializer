package de.verdox.vserializer.json;

import com.google.gson.JsonPrimitive;
import de.verdox.vserializer.generic.SerializationPrimitive;

public class JsonSerializationPrimitive extends JsonSerializationElement implements SerializationPrimitive {
    public JsonSerializationPrimitive(JsonSerializerContext serializerContext, JsonPrimitive jsonElement) {
        super(serializerContext, jsonElement);
    }

    @Override
    public boolean getAsBoolean() {
        return getJsonElement().getAsBoolean();
    }

    @Override
    public String getAsString() {
        return getJsonElement().getAsString();
    }

    @Override
    public char getAsCharacter() {
        return getJsonElement().getAsCharacter();
    }

    @Override
    public Number getAsNumber() {
        return getJsonElement().getAsNumber();
    }

    @Override
    public double getAsDouble() {
        return getJsonElement().getAsDouble();
    }

    @Override
    public float getAsFloat() {
        return getJsonElement().getAsFloat();
    }

    @Override
    public long getAsLong() {
        return getJsonElement().getAsLong();
    }

    @Override
    public int getAsInt() {
        return getJsonElement().getAsInt();
    }

    @Override
    public short getAsShort() {
        return getJsonElement().getAsShort();
    }

    @Override
    public byte getAsByte() {
        return getJsonElement().getAsByte();
    }

    @Override
    public boolean isBoolean() {
        return getJsonElement().isBoolean();
    }

    @Override
    public boolean isNumber() {
        return getJsonElement().isNumber();
    }

    @Override
    public boolean isString() {
        return getJsonElement().isString();
    }

    @Override
    public JsonPrimitive getJsonElement() {
        return super.getJsonElement().getAsJsonPrimitive();
    }
}
