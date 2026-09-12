package com.adwait.aitrading.controller;

import com.adwait.aitrading.config.JwtTokenProvider;
import com.adwait.aitrading.model.TwoFactorOtp;
import com.adwait.aitrading.model.User;
import com.adwait.aitrading.repository.UserRepository;
import com.adwait.aitrading.response.AuthResponse;
import com.adwait.aitrading.service.*;
import com.adwait.aitrading.utils.OtpUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private TwoFactorOtpService twoFactorOtpService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @Autowired
    private WatchlistService watchlistService;

    @Autowired
    private WalletService walletService;

    @Autowired
    private VerificationCodeService verificationCodeService;

    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) throws Exception {

        User is_email_exist = userRepository.findByEmail(user.getEmail());
        if(is_email_exist != null){
            throw new Exception("Email is already used with another account.");
        }

        User new_user = new User();

        new_user.setFullName(user.getFullName());
        new_user.setEmail(user.getEmail());
        new_user.setPassword(passwordEncoder.encode(user.getPassword()));
        new_user.setMobile(user.getMobile());

        User saved_user = userRepository.save(new_user);

        // Originally was not present. line added later
        watchlistService.createWatchlist(saved_user);

        Authentication auth = new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt_token = jwtTokenProvider.generateToken(auth);

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt_token);
        response.setStatus(true);
        response.setMessage("Registration successful.");

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) throws Exception {

        String username = user.getEmail();
        String password = user.getPassword();

        Authentication auth = authenticate(username, password );

        SecurityContextHolder.getContext().setAuthentication(auth);
        String jwt_token = jwtTokenProvider.generateToken(auth);

        // User auth_user = userRepository.findByEmail(username);
        User auth_user = userService.findUserProfileByEmail(username);

        if(user.getTwoFactorAuth().isEnabled()){
            AuthResponse response = new AuthResponse();
            response.setMessage("Two factor authentication is enabled.");
            response.setTwoFactorAuthEnabled(true);

            String otp = OtpUtils.generateOtp();

            TwoFactorOtp oldTwoFactorOtp = twoFactorOtpService.findByUser(auth_user.getId());
            if(oldTwoFactorOtp != null){
                twoFactorOtpService.deleteTwoFactorOtp(oldTwoFactorOtp);
            }

            TwoFactorOtp newTwoFactorOtp = twoFactorOtpService.createTwoFactorOtp(auth_user, otp, jwt_token);

            emailService.sendVerificationOtpEmail(username, otp);

            response.setSession(newTwoFactorOtp.getId());
            return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
        }

        AuthResponse response = new AuthResponse();
        response.setJwt(jwt_token);
        // response.setStatus(true);
        response.setMessage("Login success.");
        // This line was added later
        return new ResponseEntity<>(response, HttpStatus.OK);
        //return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    private Authentication authenticate(String userName, String password) {

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(userName);

        if(userDetails == null){
            throw new BadCredentialsException("Invalid username");
        }

        /*if(!password.equals(userDetails.getPassword())){
            throw new BadCredentialsException("invalid password");
        }*/
        // Better
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
    }

    @PostMapping("/two-factor/otp/{otp}")
    public ResponseEntity<AuthResponse> verifySigninOtp(@PathVariable String otp,
                                                        @RequestParam String id) throws Exception {

        TwoFactorOtp two_factor_otp = twoFactorOtpService.findById(id);

        if(twoFactorOtpService.verifyTwoFactorOtp(two_factor_otp, otp)){

            AuthResponse res = new AuthResponse();
            res.setMessage("Two factor authentication is verified.");
            res.setTwoFactorAuthEnabled(true);
            res.setJwt(two_factor_otp.getJwt());

            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        throw new Exception("Invalid otp");
    }
}
