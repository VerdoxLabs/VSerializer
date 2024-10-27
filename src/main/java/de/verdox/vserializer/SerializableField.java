package de.verdox.vserializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import de.verdox.vserializer.json.JsonSerializer;
import de.verdox.vserializer.util.gson.JsonObjectBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A serializable field of an object
 *
 * @param <T> The parent object
 * @param <R> The field type
 */
public class SerializableField<T, R> {
    private final String fieldName;
    private final JsonSerializer<R> serializer;
    private final Function<T, R> getter;
    private final BiConsumer<T, Object> setter;

    public SerializableField(@Nullable String fieldName, JsonSerializer<R> serializer, Function<T, R> getter, @Nullable BiConsumer<T, R> setter) {
        this.fieldName = fieldName;
        this.serializer = serializer;
        this.getter = getter;
        this.setter = (t, o) -> {
            if (setter != null)
                setter.accept(t, (R) o);
        };
    }

    public SerializableField(@Nullable String fieldName, JsonSerializer<R> serializer, Function<T, R> getter){
        this(fieldName, serializer, getter, null);
    }

    public SerializableField(JsonSerializer<R> serializer, Function<T, R> getter){
        this(null, serializer, getter, null);
    }

    public JsonObjectBuilder write(JsonObjectBuilder jsonObjectBuilder, T wrapped) {
        try {
            R fieldValue = getter.apply(wrapped);

            JsonElement serialized;
            if (fieldValue == null && !serializer.acceptsNullValues())
                serialized = JsonSerializer.Null.create(serializer.getType()).toJson(null);
            else
                serialized = serializer.toJson(fieldValue);

            jsonObjectBuilder.add(fieldName == null ? serializer.id() : fieldName, serialized).build();
            return jsonObjectBuilder;
        } catch (Throwable e) {
            throw new RuntimeException("An error occurred in the SerializableField " + fieldName + " while writing with the serializer " + serializer.id() + " of type " + serializer.getType(), e);
        }
    }

    public R read(JsonObject jsonObject) {
        JsonElement serialized = jsonObject.get(fieldName == null ? serializer.id() : fieldName);
        if (JsonSerializer.Null.create(serializer.getType()).isNull(serialized))
            return null;
        return serializer.fromJson(serialized);
    }

    public BiConsumer<T, Object> setter() {
        return setter;
    }

    public Function<T, R> getter() {
        return getter;
    }
}
