package com.example.dm_test.controller;
import com.example.dm_test.entity.Iris;
import com.example.dm_test.mapper.IrisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class IrisDataController {
    private final IrisMapper irisMapper;

    @Autowired
    public IrisDataController(IrisMapper irisMapper) {
        this.irisMapper = irisMapper;
    }

    @GetMapping
    public List<Iris> getData(){
        return irisMapper.getAllIris();
    }
}
