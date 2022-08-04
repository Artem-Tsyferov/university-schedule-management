package ua.com.foxminded.university.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ua.com.foxminded.university.models.Group;

import java.io.IOException;

public class CustomGroupSerializer extends StdSerializer<Group> {

    public CustomGroupSerializer() {
        this(null);
    }

    public CustomGroupSerializer(Class<Group> t) {
        super(t);
    }

    @Override
    public void serialize(Group group, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeObject(group.getName());
    }
}
