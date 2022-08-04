package ua.com.foxminded.university.models;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "first_name")
    @NotEmpty(message = "First name can not be empty")
    private String firstName;
    @Column(name = "last_name")
    @NotEmpty(message = "Last name can not be empty")
    private String lastName;
    @Column(name = "personnel_number")
    @Min(value = 1, message = "personnel number should be greater than 0")
    @NotNull(message = "Personnel number can not be empty")
    private Integer personnelNumber;
    @ManyToMany
    @JoinTable(
            name = "courses_teachers",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    private List<Course> courses;

    public Teacher() {
    }

    public static final class TeacherBuilder {

        private final Teacher teacher = new Teacher();

        public static TeacherBuilder newTeacher() {
            return new TeacherBuilder();
        }

        public TeacherBuilder withId(Integer id) {
            teacher.setId(new Integer(id));
            return this;
        }

        public TeacherBuilder withFirstName(String firstName) {
            teacher.setFirstName(new String(firstName));
            return this;
        }

        public TeacherBuilder withLastName(String lastName) {
            teacher.setLastName(new String(lastName));
            return this;
        }

        public TeacherBuilder withPersonnelNumber(Integer personnelNumber) {
            teacher.setPersonnelNumber(new Integer(personnelNumber));
            return this;
        }

        public TeacherBuilder withCourses(List<Course> courses) {
            teacher.setCourses(new ArrayList<>(courses));
            return this;
        }

        public Teacher build() {
            Teacher teacher = new Teacher();
            teacher.setId(new Integer(this.teacher.getId()));
            teacher.setFirstName(new String(this.teacher.getFirstName()));
            teacher.setLastName(new String(this.teacher.getLastName()));
            teacher.setPersonnelNumber(new Integer(this.teacher.getPersonnelNumber()));
            if (this.teacher.getCourses() != null) {
                teacher.setCourses(new ArrayList<>(this.teacher.getCourses()));
            } else {
                teacher.setCourses(null);
            }
            return teacher;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName.concat(" ").concat(lastName);
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getPersonnelNumber() {
        return personnelNumber;
    }

    public void setPersonnelNumber(Integer personnelNumber) {
        this.personnelNumber = personnelNumber;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id) && Objects.equals(firstName, teacher.firstName) && Objects.equals(lastName, teacher.lastName) && Objects.equals(personnelNumber, teacher.personnelNumber) && Objects.equals(courses, teacher.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, personnelNumber, courses);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", personnelNumber=" + personnelNumber +
                '}';
    }
}
