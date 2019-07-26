package com.upgrad.quora.service.entity;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "answer", schema ="public")
@NamedQueries(
        {
                @NamedQuery(name = "answerByUuid", query = "select u from AnswerEntity u where u.uuid =:uuid")
        }
)
public class AnswerEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "UUID")
    @Size(max = 200)
    private String uuid;

    @Column(name = "ans")
    @Size(max = 255)
    private String answer;

    @Column(name = "DATE")
    private Timestamp date;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "USER_ID")
    private UserEntity user;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "QUESTION_ID")
    private QuestionEntity question;

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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public QuestionEntity getQuestion() {
        return question;
    }

    public void setQuestion(QuestionEntity question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnswerEntity)) return false;
        AnswerEntity that = (AnswerEntity) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getUuid(), that.getUuid()) &&
                Objects.equals(getAnswer(), that.getAnswer()) &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getUser(), that.getUser()) &&
                Objects.equals(getQuestion(), that.getQuestion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUuid(), getAnswer(), getDate(), getUser(), getQuestion());
    }

    @Override
    public String toString() {
        return "AnswerEntity{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", answer='" + answer + '\'' +
                ", date=" + date +
                ", user=" + user +
                ", question=" + question +
                '}';
    }
}