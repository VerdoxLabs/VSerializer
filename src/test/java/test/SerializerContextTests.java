package test;

import de.verdox.vserializer.SerializableField;
import de.verdox.vserializer.generic.SerializationArray;
import de.verdox.vserializer.generic.SerializationContainer;
import de.verdox.vserializer.generic.SerializationElement;
import de.verdox.vserializer.generic.Serializer;
import model.Gender;
import model.Job;
import model.Person;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public abstract class SerializerContextTests extends ContextBasedTest {

    @Test
    public void testAllSerializersOfSameTypeAreEqual() {
        Assertions.assertEquals(context(), context());
    }

    @Test
    public void testSerializerConversionSimple() {
        Person person = new Person("Hans", 23, Gender.MALE);
        SerializationElement element = Person.SERIALIZER.serialize(context(), person);

        SerializationElement afterConversion = context().convert(element, true);
        Assertions.assertEquals(element, afterConversion);
    }


    @Test
    public void testSerializationElementTestSimpleIsNull(){
        SerializationElement container = context().createNull();
        Assertions.assertFalse(container.isContainer());
        Assertions.assertFalse(container.isPrimitive());
        Assertions.assertFalse(container.isArray());
        Assertions.assertTrue(container.isNull());
    }
    @Test
    public void testSerializationElementTestSimpleIsContainer(){
        SerializationElement container = context().createContainer();
        Assertions.assertTrue(container.isContainer());
        Assertions.assertFalse(container.isPrimitive());
        Assertions.assertFalse(container.isArray());
        Assertions.assertFalse(container.isNull());
    }

    @Test
    public void testSerializationElementTestSimpleIsArray(){
        SerializationElement array = context().createArray();
        Assertions.assertFalse(array.isContainer());
        Assertions.assertFalse(array.isPrimitive());
        Assertions.assertTrue(array.isArray());
        Assertions.assertFalse(array.isNull());
    }

    @Test
    public void testSerializationElementTestSimpleIsPrimitive(){
        SerializationElement primitive = context().create(1);
        Assertions.assertFalse(primitive.isContainer());
        Assertions.assertTrue(primitive.isPrimitive());
        Assertions.assertFalse(primitive.isArray());
        Assertions.assertFalse(primitive.isNull());
    }

    @Test
    public void testSerializationElementTestChildIsContainer(){
        SerializationContainer container = context().createContainer();

        container.set("child", context().createContainer());
        SerializationElement child = container.get("child");

        Assertions.assertTrue(child.isContainer());
        Assertions.assertFalse(child.isPrimitive());
        Assertions.assertFalse(child.isArray());
        Assertions.assertFalse(child.isNull());
    }

    @Test
    public void testSerializationElementTestChildIsArray(){
        SerializationContainer container = context().createContainer();

        container.set("child", context().createArray());
        SerializationElement child = container.get("child");

        Assertions.assertFalse(child.isContainer());
        Assertions.assertFalse(child.isPrimitive());
        Assertions.assertTrue(child.isArray());
        Assertions.assertFalse(child.isNull());
    }

    @Test
    public void testSerializationElementTestChildIsPrimitive(){
        SerializationContainer container = context().createContainer();

        container.set("child", context().create(1));
        SerializationElement child = container.get("child");

        Assertions.assertFalse(child.isContainer());
        Assertions.assertTrue(child.isPrimitive());
        Assertions.assertFalse(child.isArray());
        Assertions.assertFalse(child.isNull());
    }

    @Test
    public void testSerializationElementTestChildIsNull(){
        SerializationContainer container = context().createContainer();

        SerializationElement child = container.get("child");

        Assertions.assertFalse(child.isContainer());
        Assertions.assertFalse(child.isPrimitive());
        Assertions.assertFalse(child.isArray());
        Assertions.assertTrue(child.isNull());
    }

    @Test
    public void testSerializationElementTestChildFromArrayIsContainer(){
        SerializationArray array = context().createArray(context().createContainer());
        SerializationElement child = array.get(0);

        Assertions.assertTrue(child.isContainer());
        Assertions.assertFalse(child.isPrimitive());
        Assertions.assertFalse(child.isArray());
        Assertions.assertFalse(child.isNull());
    }

    @Test
    public void testSerializationElementTestChildFromArrayIsArray(){
        SerializationArray array = context().createArray(context().createArray());
        SerializationElement child = array.get(0);

        Assertions.assertFalse(child.isContainer());
        Assertions.assertFalse(child.isPrimitive());
        Assertions.assertTrue(child.isArray());
        Assertions.assertFalse(child.isNull());
    }

    @Test
    public void testSerializationElementTestChildFromArrayIsPrimitive(){
        SerializationArray array = context().createArray(context().create(1));
        SerializationElement child = array.get(0);

        Assertions.assertFalse(child.isContainer());
        Assertions.assertTrue(child.isPrimitive());
        Assertions.assertFalse(child.isArray());
        Assertions.assertFalse(child.isNull());
    }

    @Test
    public void testSerializationElementTestChildFromArrayIsNull(){
        SerializationArray array = context().createArray(context().createNull());
        SerializationElement child = array.get(0);

        Assertions.assertFalse(child.isContainer());
        Assertions.assertFalse(child.isPrimitive());
        Assertions.assertFalse(child.isArray());
        Assertions.assertTrue(child.isNull());
    }

    @Test
    public void testSerializationSameTypeInContainerPrimitive(){
        SerializationContainer container = context().createContainer();
        SerializationElement childBeforeInsertion = context().create(1);

        container.set("child", childBeforeInsertion);
        Assertions.assertEquals(childBeforeInsertion, container.get("child"));
    }

    @Test
    public void testSerializationSameTypeInContainerNull(){
        SerializationContainer container = context().createContainer();
        SerializationElement childBeforeInsertion = context().createNull();

        container.set("child", childBeforeInsertion);
        Assertions.assertEquals(childBeforeInsertion, container.get("child"));
    }

    @Test
    public void testSerializationSameTypeInContainerContainer(){
        SerializationContainer container = context().createContainer();
        SerializationElement childBeforeInsertion = context().createContainer();

        container.set("child", childBeforeInsertion);
        Assertions.assertEquals(childBeforeInsertion, container.get("child"));
    }

    @Test
    public void testSerializationSameTypeInContainerArray(){
        SerializationContainer container = context().createContainer();
        SerializationElement childBeforeInsertion = context().createArray();

        container.set("child", childBeforeInsertion);
        Assertions.assertEquals(childBeforeInsertion, container.get("child"));
    }

    @Test
    public void testIsNullContainer(){
        Assertions.assertFalse(Serializer.Null.isNull(context().createContainer()));
    }

    @Test
    public void testIsNullPrimitive(){
        Assertions.assertFalse(Serializer.Null.isNull(context().create(0)));
    }

    @Test
    public void testIsNullArray(){
        Assertions.assertFalse(Serializer.Null.isNull(context().createArray(0)));
    }

    @Test
    public void testIsNulForNull(){
        Assertions.assertTrue(Serializer.Null.isNull(context().createNull()));
    }

    @Test
    public void testIsNulForField(){
        Person person = new Person("Hans", 20, Gender.MALE);
        SerializationContainer serializationElement = Person.SERIALIZER.serialize(context(), person).getAsContainer();
        SerializableField<Person, Job> jobField = new SerializableField<>("job", Job.SERIALIZER, Person::getJob, Person::setJob);

        Assertions.assertNull(jobField.read(serializationElement));
    }
}
