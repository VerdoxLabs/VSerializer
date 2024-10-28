package blank;

import de.verdox.vserializer.blank.BlankSerializationContext;
import de.verdox.vserializer.generic.SerializationContext;
import test.SerializerContextTests;

public class BlankSerializerContextTests extends SerializerContextTests {
    @Override
    public SerializationContext context() {
        return new BlankSerializationContext();
    }
}
