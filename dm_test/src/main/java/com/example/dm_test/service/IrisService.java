package com.example.dm_test.service;

import com.example.dm_test.entity.Iris;
import com.example.dm_test.mapper.IrisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class IrisService {
    @Autowired
    private IrisMapper irisMapper;

    public List<Iris> getAllIris(){
        return irisMapper.getAllIris();
    }
}
