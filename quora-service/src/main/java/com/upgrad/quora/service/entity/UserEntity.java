package com.upgrad.quora.service.entity;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "userByEmail", query = "select u from UserEntity u where u.email =:email"),
                @NamedQuery(name = "userByUserName", query = "select u from UserEntity u where u.userName =:userName"),
                @NamedQuery(name = "userByUserId", query = "select u from UserEntity u where u.uuid =:uuid")
        }
)
public class UserEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @Column(name = "UUID")
    @Size(max = 64)
    private String uuid;

    @Column(name = "FIRSTNAME")
    @NotNull
    @Size(max = 200)
    private String firstName;

    @Column(name = "LASTNAME")
    @NotNull
    @Size(max = 200)
    private String lastName;

    @Column(name = "USERNAME")
    @NotNull
    @Size(max = 200)
    private String userName;

    @Column(name = "EMAIL")
    @NotNull
    @Size(max = 200)
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "SALT")
    @NotNull
    @Size(max = 200)
    private String salt;

    @Column(name = "COUNTRY")
    @NotNull
    @Size(max = 200)
    private String country;

    @Column(name = "ABOUTME")
    @Size(max = 200)
    private String aboutme;

    @Column(name = "DOB")
    @NotNull
    private String dob;

    @Column(name = "ROLE")
    private String role;

    @Column(name = "CONTACTNUMBER")
    @NotNull
    @Size(max = 50)
    private String contactnumber;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAboutme() {
        return aboutme;
    }

    public void setAboutme(String aboutme) {
        this.aboutme = aboutme;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContactnumber() {
        return contactnumber;
    }

    public void setContactnumber(String contactnumber) {
        this.contactnumber = contactnumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        UserEntity that = (UserEntity) o;
        return getId() == that.getId() &&
                getUuid().equals(that.getUuid()) &&
                getFirstName().equals(that.getFirstName()) &&
                getLastName().equals(that.getLastName()) &&
                getUserName().equals(that.getUserName()) &&
                getEmail().equals(that.getEmail()) &&
                getPassword().equals(that.getPassword()) &&
                getSalt().equals(that.getSalt()) &&
                getCountry().equals(that.getCountry()) &&
                getAboutme().equals(that.getAboutme()) &&
                getDob().equals(that.getDob()) &&
                getRole().equals(that.getRole()) &&
                getContactnumber().equals(that.getContactnumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUuid(), getFirstName(), getLastName(), getUserName(), getEmail(), getPassword(), getSalt(), getCountry(), getAboutme(), getDob(), getRole(), getContactnumber());
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", country='" + country + '\'' +
                ", aboutme='" + aboutme + '\'' +
                ", dob='" + dob + '\'' +
                ", role='" + role + '\'' +
                ", contactnumber='" + contactnumber + '\'' +
                '}';
    }
}
