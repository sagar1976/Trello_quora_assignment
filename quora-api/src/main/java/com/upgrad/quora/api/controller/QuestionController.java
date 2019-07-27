package com.upgrad.quora.api.controller;


import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AuthenticationService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.business.UserAdminBusinessService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private UserAdminBusinessService userAdminBusinessService;


    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion(@RequestHeader("authorization") final String authorization, final QuestionRequest questionRequest) throws AuthorizationFailedException {

        String [] bearerToken = authorization.split("Bearer ");
        final QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());
        Date date= new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        questionEntity.setDate(ts);

        QuestionEntity questionCreated = questionService.createQuestion(bearerToken[1], questionEntity);

        UserEntity user = questionCreated.getUser();
        QuestionResponse questionResponse = new QuestionResponse().id(user.getUuid()).status("QUESTION CREATED");

        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.CREATED);

    }

    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDetailsResponse> getAllQuestions(@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {

        String [] bearerToken = authorization.split("Bearer ");
        List<QuestionEntity> allQuestions = questionService.getAllQuestions(bearerToken[1]);

        List<QuestionEntity> questionUuid = questionService.getAllQuestionsUuid(bearerToken[1]);

        QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().id(questionUuid.toString()).content(allQuestions.toString());

        return new ResponseEntity<QuestionDetailsResponse>(questionDetailsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDetailsResponse> getAllQuestionsByUser(@PathVariable("userId") final String userId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {

        String [] bearerToken = authorization.split("Bearer ");
        List<QuestionEntity> allQuestions = questionService.getAllQuestionsByUser(bearerToken[1], userId);

        List<QuestionEntity> allQuestionsUuid = questionService.getAllQuestionsUuidByUser(bearerToken[1], userId);

        QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse().id(allQuestionsUuid.toString()).content(allQuestions.toString());

        return new ResponseEntity<QuestionDetailsResponse>(questionDetailsResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionEditResponse> editQuestion(@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization, final QuestionEditRequest questionEditRequest) throws AuthorizationFailedException, InvalidQuestionException {

        String [] bearerToken = authorization.split("Bearer ");
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(questionId);
        questionEntity.setContent(questionEditRequest.getContent());
        Date date= new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        questionEntity.setDate(ts);

        QuestionEntity questionEdited = questionService.questionEdit(bearerToken[1], questionEntity);
        UserEntity user = questionEdited.getUser();
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(user.getUuid()).status("QUESTION EDITED");

        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE, path ="/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion(@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {

        String [] bearerToken = authorization.split("Bearer ");
        QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(questionId);

        QuestionEntity deletedQuestion = questionService.deleteQuestion(questionEntity, bearerToken[1]);

        UserEntity user = deletedQuestion.getUser();
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse().id(user.getUuid()).status("QUESTION DELETED");

        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);

    }
}
