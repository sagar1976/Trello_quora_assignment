package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestion(QuestionEntity questionEntity){
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public List<QuestionEntity> getQuestions(){

        try{
            Query q = entityManager.createNativeQuery("SELECT u.content FROM Question u");
            List<QuestionEntity> content = q.getResultList();
            return  content;

        } catch (NoResultException nre ){
            return  null;
        }
    }

    public List<QuestionEntity> getQuestionsUuid(){

        try{
            Query q = entityManager.createNativeQuery("SELECT u.uuid FROM Question u");
            List<QuestionEntity> content = q.getResultList();
            return  content;

        } catch (NoResultException nre ){
            return  null;
        }
    }


    public List<QuestionEntity> getQuestionsByUser(long userId){

        try{
            Query q = entityManager.createNativeQuery("SELECT u.content FROM Question u WHERE u.user_id = ?").setParameter(1, userId);
            List<QuestionEntity> content = q.getResultList();
            return  content;

        } catch (NoResultException nre ){
            return  null;
        }
    }

    public List<QuestionEntity> getQuestionsUuidByUser(long userId){

        try{
            Query q = entityManager.createNativeQuery("SELECT u.uuid FROM Question u WHERE u.user_id = ?").setParameter(1, userId);
            List<QuestionEntity> content = q.getResultList();
            return  content;

        } catch (NoResultException nre ){
            return  null;
        }
    }

    public QuestionEntity getIdByUuid(final String Uuid){

        try{
            return entityManager.createNamedQuery("questionByUuid", QuestionEntity.class).setParameter("uuid", Uuid).getSingleResult();
        } catch (NoResultException nre ){
            return  null;
        }
    }

    public QuestionEntity updateQuestion(QuestionEntity questionEntity){

        entityManager.merge(questionEntity);
        return questionEntity;
    }

    public void deleteQuestion(Integer questionId){
        QuestionEntity questionEntity = entityManager.find(QuestionEntity.class, questionId);
        entityManager.remove(questionEntity);
    }
}
