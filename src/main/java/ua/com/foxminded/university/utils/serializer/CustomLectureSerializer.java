package ua.com.foxminded.university.utils.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import ua.com.foxminded.university.models.Lecture;

import java.io.IOException;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

public class CustomLectureSerializer extends StdSerializer<List<Lecture>> {

    public CustomLectureSerializer() {
        this(null);
    }

    public CustomLectureSerializer(Class<List<Lecture>> t) {
        super(t);
    }

    @Override
    public void serialize(List<Lecture> lectures, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        List<String> courseNames = lectures.stream().map(lecture -> lecture.getCourse().getName())
                .collect(Collectors.toList());

        List<String> groupNames = lectures.stream().map(lecture -> lecture.getGroup().getName())
                .collect(Collectors.toList());

        List<String> teacherNames = lectures.stream().map(lecture -> lecture.getTeacher().getFullName())
                .collect(Collectors.toList());

        List<Integer> roomNumbers = lectures.stream().map(Lecture::getRoomNumber).collect(Collectors.toList());

        List<LocalTime> timesOfStart = lectures.stream().map(Lecture::getTimeOfStart).collect(Collectors.toList());

        List<LocalTime> timesOfEnd = lectures.stream().map(Lecture::getTimeOfEnd).collect(Collectors.toList());

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("Course");
        jsonGenerator.writeObject(courseNames);
        jsonGenerator.writeFieldName("Group");
        jsonGenerator.writeObject(groupNames);
        jsonGenerator.writeFieldName("Teacher");
        jsonGenerator.writeObject(teacherNames);
        jsonGenerator.writeFieldName("Room Number");
        jsonGenerator.writeObject(roomNumbers);
        jsonGenerator.writeFieldName("Time of start");
        jsonGenerator.writeObject(timesOfStart);
        jsonGenerator.writeFieldName("Time of end");
        jsonGenerator.writeObject(timesOfEnd);
        jsonGenerator.writeEndObject();
    }
}
