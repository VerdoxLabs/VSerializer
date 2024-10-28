package de.verdox.vserializer.blank;

import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.generic.SerializationNull;

public class BlankSerializationNull<C extends SerializationContext> extends BlankSerializationElement<C> implements SerializationNull {
    public BlankSerializationNull(C serializationContext) {
        super(serializationContext);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof BlankSerializationNull<?>;
    }
}
