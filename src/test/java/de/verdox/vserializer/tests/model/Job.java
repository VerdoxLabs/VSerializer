package de.verdox.vserializer.tests.model;

import de.verdox.vserializer.SerializableField;
import de.verdox.vserializer.generic.Serializer;
import de.verdox.vserializer.generic.SerializerBuilder;

public record Job(String companyName, double salary) {
    public static final Serializer<Job> SERIALIZER = SerializerBuilder.create("job", Job.class)
            .constructor(
                    new SerializableField<>("companyName", Serializer.Primitive.STRING, Job::companyName),
                    new SerializableField<>("salary", Serializer.Primitive.DOUBLE, Job::salary),
                    Job::new
            )
            .build();
}
