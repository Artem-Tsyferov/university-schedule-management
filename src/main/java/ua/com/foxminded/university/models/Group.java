package ua.com.foxminded.university.models;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    @NotEmpty(message = "Name can not be empty")
    private String name;
    @OneToMany(mappedBy = "group")
    private List<Student> students;

    public Group() {
    }

    public static final class GroupBuilder {

        private final Group group = new Group();

        public static GroupBuilder newGroup() {
            return new GroupBuilder();
        }

        public GroupBuilder withId(Integer id) {
            group.setId(new Integer(id));
            return this;
        }

        public GroupBuilder withName(String name) {
            group.setName(new String(name));
            return this;
        }

        public GroupBuilder withStudents(List<Student> students) {
            group.setStudents(new ArrayList<>(students));
            return this;
        }

        public Group build() {
            Group group = new Group();
            group.setId(new Integer(this.group.getId()));
            group.setName(new String(this.group.getName()));
            if (this.group.getStudents() != null) {
                group.setStudents(new ArrayList<>(this.group.getStudents()));
            } else {
                group.setStudents(null);
            }
            return group;
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(name, group.name) && Objects.equals(students, group.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, students);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", students=" + students +
                '}';
    }
}
