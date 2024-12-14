package de.verdox.vserializer.blank;

import de.verdox.vserializer.generic.SerializationContainer;
import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.SerializationElement;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Objects;

/**
 * A blank implementation of a serialization container.
 * Check out {@link BlankSerializationElement} for further information
 */
public class BlankSerializationContainer extends BlankSerializationElement implements SerializationContainer {
    private final LinkedHashMap<String, SerializationElement> map = new LinkedHashMap<>();

    /**
     * Standard constructor accepting the serialization context for this container
     *
     * @param serializationContext the context
     */
    public BlankSerializationContainer(SerializationContext serializationContext) {
        super(serializationContext);
    }

    @Override
    public Collection<String> getChildKeys() {
        return map.keySet();
    }

    @Override
    public @NotNull SerializationElement get(String key) {
        return map.getOrDefault(key.toLowerCase(Locale.ROOT), getContext().createNull());
    }

    @Override
    public boolean contains(String key) {
        return map.containsKey(key.toLowerCase(Locale.ROOT));
    }

    @Override
    public void set(String key, SerializationElement serializationElement) {
        map.put(key.toLowerCase(Locale.ROOT), getContext().convert(serializationElement, false));
    }

    @Override
    public void remove(String key) {
        map.remove(key.toLowerCase(Locale.ROOT));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlankSerializationContainer that = (BlankSerializationContainer) o;
        return Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(map);
    }
}
