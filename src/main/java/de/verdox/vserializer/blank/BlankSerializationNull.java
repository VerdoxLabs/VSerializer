package de.verdox.vserializer.blank;

import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.SerializationNull;

public class BlankSerializationNull extends BlankSerializationElement implements SerializationNull {
    public BlankSerializationNull(SerializationContext serializationContext) {
        super(serializationContext);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BlankSerializationNull;
    }
}
