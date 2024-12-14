package de.verdox.vserializer.generic;

/**
 * Indicates that this element serializes / deserializes from its methods.
 * The object can either save its data into a given container or can read data from a given container and update its state accordingly
 */
public interface Serializable {
    void save(SerializationContainer container, SerializationContext context);
    void load(SerializationContainer container, SerializationContext context);
}
