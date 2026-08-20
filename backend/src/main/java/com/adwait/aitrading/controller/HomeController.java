package com.adwait.aitrading.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.adwait.aitrading.response.ApiResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@RestController
public class HomeController {
    /*
    @GetMapping
    public String home(){

        return "welcome to home route!";
    }

    @GetMapping("/api")
    public String secure(){

        return "You have accessed the secure route!";
    }
    */

    @GetMapping("/")
    public ResponseEntity<ApiResponse> homeController(){


        ApiResponse res=new ApiResponse(
                "Welcome to crypto trading platform - everything is working fine.",
                true
        );
        return new ResponseEntity<ApiResponse>(res,HttpStatus.ACCEPTED);
    }
}
