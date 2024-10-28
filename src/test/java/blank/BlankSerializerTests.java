package blank;

import de.verdox.vserializer.blank.BlankSerializationContext;
import de.verdox.vserializer.generic.SerializationContext;
import test.SerializerTests;

public class BlankSerializerTests extends SerializerTests {
    @Override
    public SerializationContext context() {
        return new BlankSerializationContext();
    }
}
