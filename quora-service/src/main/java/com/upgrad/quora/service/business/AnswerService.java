package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AnswerService {

    @Autowired
    private AnswerDao answerDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(final String authorization, final AnswerEntity answerEntity, final String questionId) throws AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity checkQuestionExistence = questionDao.getIdByUuid(questionId);
        if(checkQuestionExistence == null){
            throw new InvalidQuestionException("QUES-001","The question entered is invalid");
        }

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if(userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post an answer");
        }

        answerEntity.setUser(userAuthEntity.getUser());

        answerEntity.setQuestion(checkQuestionExistence);

        return answerDao.createAnswer(answerEntity);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswer(final String authorization, final String answerId, final AnswerEntity answerEntity) throws AuthorizationFailedException, AnswerNotFoundException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to edit an answer");
        }

        AnswerEntity checkAnswerUuid = answerDao.getIdByUuid(answerEntity.getUuid());

        if(checkAnswerUuid == null){
            throw new AnswerNotFoundException("ANS-001'","Entered answer uuid does not exist");
        }

        if(checkAnswerUuid.getUser() != userAuthEntity.getUser()){
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");
        }

        answerEntity.setUser(userAuthEntity.getUser());
        answerEntity.setId(checkAnswerUuid.getId());
        answerEntity.setQuestion(checkAnswerUuid.getQuestion());

        AnswerEntity updateAnswer = answerDao.updateAnswer(answerEntity);

        return updateAnswer;

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(final String authorization, final AnswerEntity answerEntity) throws AuthorizationFailedException, AnswerNotFoundException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to delete an answer");
        }

        AnswerEntity checkAnswerUuid = answerDao.getIdByUuid(answerEntity.getUuid());

        if(checkAnswerUuid == null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }

        UserEntity user = userAuthEntity.getUser();
        String role = user.getRole();
        if(role.equals("nonadmin") && checkAnswerUuid.getUser() != userAuthEntity.getUser()){
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner or admin can delete the answer");
        }

        answerEntity.setUser(userAuthEntity.getUser());
        answerEntity.setId(checkAnswerUuid.getId());

        answerDao.deleteAnswer(answerEntity.getId());

        return answerEntity;

    }

    public List<AnswerEntity> getAllAnswers(final String authorization, final String questionId) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get the answers");
        }

        QuestionEntity checkIfQuestionExists = questionDao.getIdByUuid(questionId);

        if(checkIfQuestionExists == null){
            throw new InvalidQuestionException("QUES-001","The question with entered uuid whose details are to be seen does not exist");
        }

        return answerDao.getAllAnswer(checkIfQuestionExists.getId());

    }


    public List<AnswerEntity> getAllAnswersId(final String authorization, final String questionId) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get the answers");
        }

        QuestionEntity checkIfQuestionExists = questionDao.getIdByUuid(questionId);

        if(checkIfQuestionExists == null){
            throw new InvalidQuestionException("QUES-001","The question with entered uuid whose details are to be seen does not exist");
        }

        return answerDao.getAllAnswerId(checkIfQuestionExists.getId());

    }
}
