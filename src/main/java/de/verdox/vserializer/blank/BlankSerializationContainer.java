package de.verdox.vserializer.blank;

import de.verdox.vserializer.generic.SerializationContainer;
import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.SerializationElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Objects;

public class BlankSerializationContainer<C extends SerializationContext> extends BlankSerializationElement<C> implements SerializationContainer {
    private final LinkedHashMap<String, SerializationElement> map = new LinkedHashMap<>();
    public BlankSerializationContainer(C serializationContext) {
        super(serializationContext);
    }

    @Override
    public Collection<String> getChildKeys() {
        return map.keySet();
    }

    @Override
    public @NotNull SerializationElement get(String key) {
        return map.getOrDefault(key, getContext().createNull());
    }

    @Override
    public boolean contains(String key) {
        return map.containsKey(key);
    }

    @Override
    public void set(String key, SerializationElement serializationElement) {
        map.put(key, getContext().convert(serializationElement, false));
    }

    @Override
    public void remove(String key) {
        map.remove(key);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlankSerializationContainer<?> that = (BlankSerializationContainer<?>) o;
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
