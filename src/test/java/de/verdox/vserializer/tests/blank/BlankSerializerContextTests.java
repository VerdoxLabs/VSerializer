package de.verdox.vserializer.tests.blank;

import de.verdox.vserializer.blank.BlankSerializationContext;
import de.verdox.vserializer.generic.SerializationContext;
import de.verdox.vserializer.tests.test.SerializerContextTests;

public class BlankSerializerContextTests extends SerializerContextTests {
    @Override
    public SerializationContext context() {
        return new BlankSerializationContext();
    }
}
