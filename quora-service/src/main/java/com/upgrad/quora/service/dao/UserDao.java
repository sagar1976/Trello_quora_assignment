package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    //method to create new user
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    //method to get user by username
    public UserEntity getUserByUserName(final String username) {
        try {
            return entityManager.createNamedQuery("userByUserName", UserEntity.class).setParameter("userName", username).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //method to get user by email
    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    //method to get user id by passing Uuid, as this is required to set id for update operations.
    public UserEntity getUserId(String userUuid){
        try{
            return entityManager.createNamedQuery("userByUserId", UserEntity.class).setParameter("uuid", userUuid).getSingleResult();
        } catch (NoResultException nre ){
            return  null;
        }
    }

    //Important method to store auth token when user has logged in. This token is used by user for future transactions.
    public UserAuthEntity createAuthToken(final UserAuthEntity userAuthTokenEntity) {
        entityManager.persist(userAuthTokenEntity);
        return userAuthTokenEntity;
    }

    //method to get token details as requested
    public UserAuthEntity getUserAuthToken(String accesstoken){
        try{
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class).setParameter("accessToken", accesstoken).getSingleResult();
        } catch (NoResultException nre ){
            return  null;
        }
    }

    //method to update user details
    public void updateUser(final UserEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }

    //method to stamp logout time for the user who has logged out.
    public void updateLogoutTime(final UserAuthEntity userAuthEntity) {
        entityManager.merge(userAuthEntity);
    }

    public void deleteUser(long userId){
        UserEntity user = entityManager.find(UserEntity.class,userId);
        entityManager.remove(user);
    }
}