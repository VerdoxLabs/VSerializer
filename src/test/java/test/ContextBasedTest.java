package test;

import de.verdox.vserializer.generic.SerializationContext;

public abstract class ContextBasedTest {
    /**
     * Creates a new {@link SerializationContext} object
     * @return a new serialization context object
     */
    public abstract SerializationContext context();
}
