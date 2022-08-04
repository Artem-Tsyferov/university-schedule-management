package ua.com.foxminded.university.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ua.com.foxminded.university.models.Teacher;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CustomTeacherSerializer extends StdSerializer<List<Teacher>> {

    public CustomTeacherSerializer() {
        this(null);
    }

    public CustomTeacherSerializer(Class<List<Teacher>> t) {
        super(t);
    }

    @Override
    public void serialize(List<Teacher> teachers, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        List<String> names = teachers.stream().map(Teacher::getFullName).collect(Collectors.toList());
        jsonGenerator.writeObject(names);
    }
}
