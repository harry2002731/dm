package com.example.dm_test.controller;


import com.example.dm_test.service.RegressionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/regression")
public class RegressionController {
    private static final Logger logger = LoggerFactory.getLogger(RegressionController.class);

    @Autowired
    private RegressionService regressionService;


}
