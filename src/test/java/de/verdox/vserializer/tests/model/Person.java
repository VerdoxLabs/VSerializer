package de.verdox.vserializer.tests.model;

import de.verdox.vserializer.SerializableField;
import de.verdox.vserializer.generic.Serializer;
import de.verdox.vserializer.generic.SerializerBuilder;

import java.util.Objects;

public class Person {
    public static final Serializer<Person> SERIALIZER = SerializerBuilder.create("person", Person.class)
            .constructor(
                    new SerializableField<>("name", Serializer.Primitive.STRING, Person::getName, Person::setName),
                    new SerializableField<>("age", Serializer.Primitive.INTEGER, Person::getAge, Person::setAge),
                    new SerializableField<>("gender", Serializer.Enum.create("gender", Gender.class), Person::getGender),
                    Person::new
                    )
            .withField("job", Job.SERIALIZER, Person::getJob, Person::setJob)
            .build();

    private String name;
    private int age;
    private final Gender gender;
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

    public void setJob(Job job) {
        this.job = job;
    }


    public Job getJob() {
        return job;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name) && gender == person.gender && Objects.equals(job, person.job);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, gender, job);
    }
}
