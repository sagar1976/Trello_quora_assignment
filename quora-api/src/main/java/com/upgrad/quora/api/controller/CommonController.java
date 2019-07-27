package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserAdminBusinessService;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private UserAdminBusinessService userAdminBusinessService;

    //Controller method to get user details using User Id
    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUser(@PathVariable("userId") final String userUuid,
                                                       @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        String [] bearerToken = authorization.split("Bearer ");
        final UserAuthEntity userAuthEntity = userAdminBusinessService.getUser(userUuid, bearerToken[1]);
        UserEntity user = userAuthEntity.getUser();

        UserDetailsResponse userDetailsResponse = new UserDetailsResponse().firstName(user.getFirstName()).lastName(user.getLastName()).userName(user.getUserName())
                .emailAddress(user.getEmail()).country(user.getCountry()).aboutMe(user.getAboutme()).dob(user.getDob())
                .contactNumber(user.getContactnumber());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);

    }
}
