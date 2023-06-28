package com.example.dm_test.controller;

import com.example.dm_test.entity.ClusterRes;
import com.example.dm_test.service.ClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class ClassificationController {

    @Autowired
    private ClassificationService classificationService;
    @GetMapping("/api/classify_vali")
    public double[] getValidationData()
    {
        return classificationService.validatePrecision();
    }



}
