package com.adwait.aitrading.service;

import com.adwait.aitrading.domain.VerificationType;
import com.adwait.aitrading.exception.UserException;
import com.adwait.aitrading.model.User;

public interface UserService {

    User findUserProfileByJwt(String jwt) throws UserException;

    User findUserProfileByEmail(String email) throws UserException;

    User findUserProfileById(Long userId) throws UserException;

    User verifyUser(User user);

    User enableTwoFactorAuthentication(VerificationType verifyType,
                                              String sendTo,
                                              User user);

    User updatePassword(User user, String newPwd);

}
