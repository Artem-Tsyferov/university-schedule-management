package ua.com.foxminded.university.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ua.com.foxminded.university.models.Student;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CustomStudentSerializer extends StdSerializer<List<Student>> {

    public CustomStudentSerializer() {
        this(null);
    }

    public CustomStudentSerializer(Class<List<Student>> t) {
        super(t);
    }

    @Override
    public void serialize(List<Student> students, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<String> names = students.stream().map(Student::getFullName).collect(Collectors.toList());
        jsonGenerator.writeObject(names);
    }
}
