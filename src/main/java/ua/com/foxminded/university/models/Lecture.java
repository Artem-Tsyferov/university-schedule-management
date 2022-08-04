package ua.com.foxminded.university.models;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "lectures")
public class Lecture {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @OneToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;
    @OneToOne
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;
    @OneToOne
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;
    @Column(name = "room_number")
    private Integer roomNumber;
    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @Column(name = "time_of_start")
    private LocalTime timeOfStart;
    @Column(name = "time_of_end")
    private LocalTime timeOfEnd;

    public Lecture() {
    }

    public Lecture(Integer id, Course course, Teacher teacher, Group group, Integer roomNumber, LocalDate date, LocalTime timeOfStart, LocalTime timeOfEnd) {
        this.id = id;
        this.course = course;
        this.teacher = teacher;
        this.group = group;
        this.roomNumber = roomNumber;
        this.date = date;
        this.timeOfStart = timeOfStart;
        this.timeOfEnd = timeOfEnd;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTimeOfStart() {
        return timeOfStart;
    }

    public void setTimeOfStart(LocalTime timeOfStart) {
        this.timeOfStart = timeOfStart;
    }

    public LocalTime getTimeOfEnd() {
        return timeOfEnd;
    }

    public void setTimeOfEnd(LocalTime timeOfEnd) {
        this.timeOfEnd = timeOfEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return Objects.equals(id, lecture.id) && Objects.equals(course, lecture.course) && Objects.equals(teacher, lecture.teacher) && Objects.equals(group, lecture.group) && Objects.equals(roomNumber, lecture.roomNumber) && Objects.equals(date, lecture.date) && Objects.equals(timeOfStart, lecture.timeOfStart) && Objects.equals(timeOfEnd, lecture.timeOfEnd);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, course, teacher, group, roomNumber, date, timeOfStart, timeOfEnd);
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "id=" + id +
                ", course=" + course +
                ", teacher=" + teacher +
                ", group=" + group +
                ", roomNumber=" + roomNumber +
                ", date=" + date +
                ", timeOfStart=" + timeOfStart +
                ", timeOfEnd=" + timeOfEnd +
                '}';
    }
}
