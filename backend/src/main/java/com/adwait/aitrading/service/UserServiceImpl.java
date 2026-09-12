package com.adwait.aitrading.service;

import com.adwait.aitrading.config.JwtTokenProvider;
import com.adwait.aitrading.domain.VerificationType;
import com.adwait.aitrading.exception.UserException;
import com.adwait.aitrading.model.TwoFactorAuth;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // Standard constructor injection
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public User findUserProfileByJwt(String jwt) throws UserException {
        String email = jwtTokenProvider.getEmailFromToken(jwt);
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new UserException("User does not exist with email: "+ email);
        }
        return user;
    }

    @Override
    public User findUserProfileByEmail(String email) throws UserException {
        User user = userRepository.findByEmail(email);

        if(user == null){
            throw new UserException("User does not exist with username: "+ email);
        }
        return user;
    }

    @Override
    public User findUserProfileById(Long userId) throws UserException {
        Optional<User> user = userRepository.findById(userId);

        if(user.isEmpty()){
            throw new UserException("User not found with id: "+ userId);
        }
        return user.get();
    }
    @Override
    public User verifyUser(User user) {
        user.setVerified(true);
        return userRepository.save(user);
    }
    @Override
    public User enableTwoFactorAuthentication(VerificationType verifyType, String sendTo, User user) {

        TwoFactorAuth twoFactorAuth = new TwoFactorAuth();
        twoFactorAuth.setEnabled(true);
        twoFactorAuth.setSendTo(verifyType);

        user.setTwoFactorAuth(twoFactorAuth);

        return userRepository.save(user);
    }


    @Override
    public User updatePassword(User user, String newPwd) {
        user.setPassword(passwordEncoder.encode(newPwd));

        return userRepository.save(user);
    }
}