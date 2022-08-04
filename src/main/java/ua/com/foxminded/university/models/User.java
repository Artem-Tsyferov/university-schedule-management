package ua.com.foxminded.university.models;

import ua.com.foxminded.university.models.enums.UserRole;
import ua.com.foxminded.university.service.impl.EncryptionService;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "login")
    @NotEmpty(message = "Login can not be empty")
    private String login;
    @Column(name = "password")
    @NotEmpty(message = "Password can not be empty")
    private String password;
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Column(name = "person_id")
    @Min(value = 1, message = "personnel number should be greater than 0")
    @NotNull(message = "Personnel number can not be empty")
    private Integer personId;

    public User() {
    }

    public User(Integer id, String login, String password, UserRole role, Integer personId) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.role = role;
        this.personId = personId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setEncryptedPassword(String password) {
        this.password = EncryptionService.encrypt(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public Integer getPersonId() {
        return personId;
    }

    public void setPersonId(Integer personId) {
        this.personId = personId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(login, user.login) && Objects.equals(password, user.password) && role == user.role && Objects.equals(personId, user.personId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, role, personId);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", role=" + role +
                ", personId=" + personId +
                '}';
    }
}
