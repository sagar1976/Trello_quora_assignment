package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getIdByUuid(final String Uuid) {//getting user id by passing Uuid of answer

        try {
            return entityManager.createNamedQuery("answerByUuid", AnswerEntity.class).setParameter("uuid", Uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public AnswerEntity updateAnswer(AnswerEntity answerEntity) {

        entityManager.merge(answerEntity);
        return answerEntity;
    }

    public void deleteAnswer(Integer answerId) {
        AnswerEntity answerEntity = entityManager.find(AnswerEntity.class, answerId);
        entityManager.remove(answerEntity);
    }


    public List<AnswerEntity> getAllAnswer(long questionId) {//getting answer/s details or content for the required question

        try{
            Query q = entityManager.createNativeQuery("SELECT u.ans FROM Answer u JOIN Question t on u.question_id = t.id WHERE u.question_id = ?").setParameter(1, questionId);
            List<AnswerEntity> ans = q.getResultList();
            return ans;

        } catch (NoResultException nre ){
            return  null;
        }
    }

    public List<AnswerEntity> getAllAnswerId(long questionId) {//getting answers uuid by passing question Id

        try{
            Query q = entityManager.createNativeQuery("SELECT u.uuid FROM Answer u JOIN Question t on u.question_id = t.id WHERE u.question_id = ?").setParameter(1, questionId);
            List<AnswerEntity> ans = q.getResultList();
            return ans;

        } catch (NoResultException nre ){
            return  null;
        }
    }
}
