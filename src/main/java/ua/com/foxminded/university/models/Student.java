package ua.com.foxminded.university.models;


import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "students")
public class Student {

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
    @ManyToOne()
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    public Student() {
    }

    public Student(Integer id, String firstName, String lastName, Integer personnelNumber, Group group) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.personnelNumber = personnelNumber;
        this.group = group;
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

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(firstName, student.firstName) && Objects.equals(lastName, student.lastName) && Objects.equals(personnelNumber, student.personnelNumber) && Objects.equals(group, student.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, personnelNumber, group);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", personnelNumber=" + personnelNumber +
                '}';
    }
}
