package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
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
public class AnswerController {

    @Autowired
    private AnswerService answerService;

    @Autowired
    private QuestionService questionService;

    //Controller method to create answer for specific question and  with given question id (Uuid) we have to create answers for the given questions.
    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}/answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization, final AnswerRequest answerRequest) throws AuthorizationFailedException, InvalidQuestionException {

        String [] bearerToken = authorization.split("Bearer ");

        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setAnswer(answerRequest.getAnswer());
        Date date= new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        answerEntity.setDate(ts);

        AnswerEntity createdAnswer = answerService.createAnswer(bearerToken[1], answerEntity, questionId);

        UserEntity user = createdAnswer.getUser();

        AnswerResponse answerResponse = new AnswerResponse().id(user.getUuid()).status("ANSWER CREATED");

        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    //Controller method to edit specific answer and response should contain edited answer
    @RequestMapping(method = RequestMethod.PUT, path = "/answer/edit/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerEditResponse> editAnswer(@PathVariable("answerId") final String answerId, @RequestHeader("authorization") final String authorization, final AnswerEditRequest answerEditRequest) throws AuthorizationFailedException, AnswerNotFoundException {

        String [] bearertoken = authorization.split("Bearer ");
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setUuid(answerId);
        answerEntity.setAnswer(answerEditRequest.getContent());
        Date date= new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        answerEntity.setDate(ts);

        AnswerEntity editAnswer = answerService.editAnswer(bearertoken[1], answerId, answerEntity);

        UserEntity user = editAnswer.getUser();
        AnswerEditResponse answerEditResponse = new AnswerEditResponse().id(user.getUuid()).status("ANSWER EDITED");

        return new ResponseEntity<AnswerEditResponse>(answerEditResponse, HttpStatus.OK);

    }

    //Controller method to delete particular answer
    @RequestMapping(method = RequestMethod.DELETE, path = "/answer/delete/{answerId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@PathVariable("answerId") final String answerId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {

        String [] bearerToken = authorization.split("Bearer ");
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setUuid(answerId);

        AnswerEntity deletedAnswer = answerService.deleteAnswer(bearerToken[1], answerEntity);

        UserEntity user = deletedAnswer.getUser();

        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse().id(user.getUuid()).status("ANSWER DELETED");

        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }


    //Controller method to find answers for given question ID whioh is Uuid of question
    @RequestMapping(method = RequestMethod.GET, path ="answer/all/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerDetailsResponse> getAllAnswers(@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization ) throws AuthorizationFailedException, InvalidQuestionException {

        String [] bearerToken = authorization.split("Bearer ");

        List<AnswerEntity> getAllAnswersDetails = answerService.getAllAnswers(bearerToken[1], questionId);//Service bean to used to get all details of answers

        List<AnswerEntity> getAllAnswersId = answerService.getAllAnswersId(bearerToken[1], questionId);//Service bean used to get uuids of all answers

        QuestionEntity getQuestionContent = questionService.getQuestionById(bearerToken[1], questionId);//Service bean used to get question details

        AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse().id(getAllAnswersId.toString()).questionContent(getQuestionContent.getContent().toString()).answerContent(getAllAnswersDetails.toString());

        return new ResponseEntity<AnswerDetailsResponse>(answerDetailsResponse, HttpStatus.OK);

    }
}
