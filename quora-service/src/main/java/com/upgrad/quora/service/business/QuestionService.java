package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    //Service method to create question
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(final String authorization, QuestionEntity questionEntity ) throws AuthorizationFailedException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);//Exceptions are used to filters out user who have not signed in or have signed out.
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post a question");
        }

        UserEntity user = userAuthEntity.getUser();
        questionEntity.setUser(user);

        questionDao.createQuestion(questionEntity);

        return questionEntity;

    }

    //This method collects all the questions posted by all users.
    public List<QuestionEntity> getAllQuestions(final String authorization) throws AuthorizationFailedException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions");
        }
        QuestionEntity questionEntity = new QuestionEntity();
        return questionDao.getQuestions();
    }

    //This method gets the Uuids for the questions which are to be retreived.
    public List<QuestionEntity> getAllQuestionsUuid(final String authorization) throws AuthorizationFailedException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions");
        }
        QuestionEntity questionEntity = new QuestionEntity();
        return questionDao.getQuestionsUuid();
    }

    //This method retreives details of all questions by user, means user who has posted those questions.
    public List<QuestionEntity> getAllQuestionsByUser(final String authorization, final String userId) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions posted by a specific user");
        }

        UserEntity checkUserIfExists = userDao.getUserId(userId);
        if(checkUserIfExists == null){
            throw new UserNotFoundException("USR-001","User with entered uuid whose question details are to be seen does not exist");
        }

        List<QuestionEntity> checkIfResultExists = questionDao.getQuestionsByUser(checkUserIfExists.getId());//This is specially added as if user havent posted any question it was retreiving empty records
        if (checkIfResultExists.isEmpty()){
            throw new UserNotFoundException("USR-001","User havent posted any question");
        }

        return checkIfResultExists;
    }

    public List<QuestionEntity> getAllQuestionsUuidByUser(final String authorization, final String userId) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to get all questions posted by a specific user");
        }

        UserEntity checkUserIfExists = userDao.getUserId(userId);
        if(checkUserIfExists == null){
            throw new UserNotFoundException("USR-001","User with entered uuid whose question details are to be seen does not exist");
        }

        return questionDao.getQuestionsUuidByUser(checkUserIfExists.getId());
    }

    //This method is used to edit question
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity questionEdit(final String authorization, final QuestionEntity questionEntity) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to edit the question");
        }

        QuestionEntity checkQuestionUuid = questionDao.getIdByUuid(questionEntity.getUuid());

        if(checkQuestionUuid == null){
            throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");
        }

        if((checkQuestionUuid.getUser() != userAuthEntity.getUser())){
            throw new AuthorizationFailedException("ATHR-003","Only the question owner can edit the question");
        }

        questionEntity.setUser(userAuthEntity.getUser());
        questionEntity.setId(checkQuestionUuid.getId());

        QuestionEntity updatedQuestion = questionDao.updateQuestion(questionEntity);

        return updatedQuestion;

    }

    //This method checks various criteria like owner of question etc before deleting the question
    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(final QuestionEntity questionEntity, final String authorization) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to delete a question");
        }

        QuestionEntity checkQuestionUuid = questionDao.getIdByUuid(questionEntity.getUuid());

        if(checkQuestionUuid == null){
            throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist");
        }

        UserEntity user = userAuthEntity.getUser();
        String role = user.getRole();
        if(role.equals("nonadmin")&& (checkQuestionUuid.getUser() != userAuthEntity.getUser())){
            throw new AuthorizationFailedException("ATHR-003","Only the question owner or admin can delete the question");
        }

        questionEntity.setUser(userAuthEntity.getUser());
        questionEntity.setId(checkQuestionUuid.getId());

        questionDao.deleteQuestion(questionEntity.getId());

        return questionEntity;
    }

    //Method to get question by sending Uuid
    public QuestionEntity getQuestionById(final String authorization, final String questionId) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }

        if(userAuthEntity.getLogoutAt() != null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to retreive answers to questions");
        }

        return questionDao.getIdByUuid(questionId);

    }
}
