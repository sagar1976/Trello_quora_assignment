package com.upgrad.quora.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_auth", schema ="public")
@NamedQueries({
        @NamedQuery(name = "userAuthTokenByAccessToken", query = "select ut from UserAuthEntity ut where ut.accessToken =:accessToken")
})
public class UserAuthEntity implements Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "uuid")
    @Size(max = 200)
    private String uuid;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @Column(name = "ACCESS_TOKEN")
    @NotNull
    @Size(max = 500)
    private String accessToken;

    @Column(name = "EXPIRES_AT")
    @NotNull
    private ZonedDateTime expiresAt;

    @Column(name = "LOGOUT_AT")
    private ZonedDateTime logoutAt;

    @Column(name = "LOGIN_AT")
    @NotNull
    private ZonedDateTime loginAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ZonedDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(ZonedDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public ZonedDateTime getLogoutAt() {
        return logoutAt;
    }

    public void setLogoutAt(ZonedDateTime logoutAt) {
        this.logoutAt = logoutAt;
    }

    public ZonedDateTime getLoginAt() {
        return loginAt;
    }

    public void setLoginAt(ZonedDateTime loginAt) {
        this.loginAt = loginAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAuthEntity)) return false;
        UserAuthEntity that = (UserAuthEntity) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getUuid(), that.getUuid()) &&
                Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getAccessToken(), that.getAccessToken()) &&
                Objects.equals(getExpiresAt(), that.getExpiresAt()) &&
                Objects.equals(getLogoutAt(), that.getLogoutAt()) &&
                Objects.equals(getLoginAt(), that.getLoginAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUuid(), getUser(), getAccessToken(), getExpiresAt(), getLogoutAt(), getLoginAt());
    }

    @Override
    public String toString() {
        return "UserAuthEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", user=" + user +
                ", accessToken='" + accessToken + '\'' +
                ", expiresAt=" + expiresAt +
                ", logoutAt=" + logoutAt +
                ", loginAt=" + loginAt +
                '}';
    }
}
