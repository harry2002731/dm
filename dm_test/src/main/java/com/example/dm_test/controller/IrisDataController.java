package com.example.dm_test.controller;
import com.example.dm_test.entity.Iris;
import com.example.dm_test.mapper.IrisMapper;
import com.example.dm_test.service.IrisService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
//@RequestMapping("/api/data")
public class IrisDataController {
    private static final Logger logger = LoggerFactory.getLogger(IrisDataController.class);
    private final IrisMapper irisMapper;

    @Autowired
    public IrisDataController(IrisMapper irisMapper) {
        this.irisMapper = irisMapper;
    }

    @GetMapping("/api/data")
    public List<Iris> getData(){
        return irisMapper.getAllIris();
    }

    @Autowired
    private IrisService irisService;

    @GetMapping("/iris_data")
    public PageInfo<Iris> getAllUsers(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize)
    {
        logger.info("Received request with pageNum: {}, pageSize: {}", pageNum, pageSize);
        return irisService.findAll(pageNum, pageSize);
    }
}
