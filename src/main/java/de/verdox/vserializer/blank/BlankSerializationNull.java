package de.verdox.vserializer.blank;

import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.SerializationNull;

/**
 * A blank implementation of a serialization null.
 * Check out {@link BlankSerializationElement} for further information
 */
public class BlankSerializationNull extends BlankSerializationElement implements SerializationNull {
    /**
     * The basic constructor
     *
     * @param serializationContext the context
     */
    public BlankSerializationNull(SerializationContext serializationContext) {
        super(serializationContext);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BlankSerializationNull;
    }

    @Override
    public String toString() {
        return "null";
    }
}
