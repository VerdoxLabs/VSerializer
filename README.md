## VSerializer ##
VSerializer is a library used to create context independent Serializers for Java Objects. 
Right now it only supports json serialization. However, new serialization contexts can be included easily.

### How to create a Serializer? ###

Let's assume we have written the java class Person, which looks like this.

```java
public class Person {
    private String name;
    private int age;
    private final Gender gender;
    @Nullable
    private Job job;

    public Person(String name, int age, Gender gender) {
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    public Gender getGender() {
        return gender;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setJob(@Nullable Job job) {
        this.job = job;
    }

    @Nullable
    public Job getJob() {
        return job;
    }
}
```

Our Person object can have a Job (but does not have to). However, we also want to serialize the Job. 
Thus, we create a serializer for the Job

```java
public record Job(String companyName, double salary) {
    public static final Serializer<Job> SERIALIZER = SerializerBuilder.create("job", Job.class)
            .constructor(
                    new SerializableField<>("companyName", Serializer.Primitive.STRING, Job::companyName),
                    new SerializableField<>("salary", Serializer.Primitive.DOUBLE, Job::salary),
                    Job::new
            )
            .build();
}
```

The rest of the fields are just primitives and enums. We create those serializers on the fly.
Now lets create the serializer for our person class.

```java
    public static final Serializer<Person> SERIALIZER = SerializerBuilder.create("person", Person.class)
            .constructor(
                    new SerializableField<>("name", Serializer.Primitive.STRING, Person::getName, Person::setName),
                    new SerializableField<>("age", Serializer.Primitive.INTEGER, Person::getAge, Person::setAge),
                    new SerializableField<>("gender", Serializer.Enum.create("gender", Gender.class), Person::getGender),
                    Person::new
                    )
            .withField("job", Job.SERIALIZER, Person::getJob, Person::setJob)
            .build();
```

You may also notice that there is a difference between constructor arguments and field arguments. 
While fields actively require a setter function, constructor fields don't.

### How to use a Serializer? ###

```java
SerializationContext context = new JsonSerializationContext();
Person person = new Person(...);
SerializationContainer serializationElement = Person.SERIALIZER.serialize(context, person).getAsContainer();

Person deserializedPerson = Person.SERIALIZER.deserialize(person).getAsContainer();
```

### I want to save my serialized data! ###
Sure, just use the SerializationContext in this case!
```java
        SerializationContext context = new JsonSerializationContext();
        Person person = new Person(...);
        SerializationContainer serializationElement = Person.SERIALIZER.serialize(context, person).getAsContainer();
        
        context.writeToFile(context, new File(...));
        context.readFromFile(new File(...))
```

### How can I use this dependency? ###
Right now this project lacks a distribution strategy. 
For now, it would be best practice to clone the repo and install the project as a dependency on your local maven repo. 
Alternatively, you can use jitpack.io.
