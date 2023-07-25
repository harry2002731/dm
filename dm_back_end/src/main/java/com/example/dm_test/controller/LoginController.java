package com.example.dm_test.controller;

import com.example.dm_test.entity.User;
import com.example.dm_test.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public String getLoginInfo(@RequestBody User user)
    {
        logger.info("Received login info");
        String username = user.getUsername();
        String password = loginService.getMD5Hash(user.getPassword());
        return loginService.checkLogin(username, password);
    }
}
