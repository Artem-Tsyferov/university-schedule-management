package ua.com.foxminded.university.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ua.com.foxminded.university.models.Course;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CustomCourseSerializer extends StdSerializer<List<Course>> {

    public CustomCourseSerializer() {
        this(null);
    }

    public CustomCourseSerializer(Class<List<Course>> t) {
        super(t);
    }

    @Override
    public void serialize(List<Course> courses, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<String> names = courses.stream().map(Course::getName).collect(Collectors.toList());
        jsonGenerator.writeObject(names);
    }
}
