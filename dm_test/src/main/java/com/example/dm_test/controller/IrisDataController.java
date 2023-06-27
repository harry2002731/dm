package com.example.dm_test.controller;
import com.example.dm_test.entity.ClusterRes;
import com.example.dm_test.entity.Iris;
import com.example.dm_test.mapper.IrisMapper;
import com.example.dm_test.service.ClassificationService;
import com.example.dm_test.service.ClusteringService;
import com.example.dm_test.service.IrisService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
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

    @Autowired
    private ClassificationService classificationService;

    @Autowired
    private ClusteringService clusteringService;

    @GetMapping("/api/user/page")
    public PageInfo<Iris> getAllUsers(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize)
    {
        logger.info("Received request with pageNum: {}, pageSize: {}", pageNum, pageSize);
        return irisService.findAll(pageNum, pageSize);
    }

    @PostMapping(value = "/api/post_test")
    public String getClassificationResult(@RequestBody Iris dataForClassification)
    {
        logger.info("Received post request");
        float attribute1 = dataForClassification.getSepL();
        float attribute2 = dataForClassification.getSepW();
        float attribute3 = dataForClassification.getPetL();
        float attribute4 = dataForClassification.getPetW();
        return classificationService.performClassification(attribute1,attribute2,attribute3,attribute4);
    }

    @GetMapping("/api/clu_test")
    public List<ClusterRes> getClusteringresult(@RequestParam(defaultValue = "3") int K_num)
    {
        logger.info("Received request with K_num: {} ", K_num);
        return clusteringService.performClustering(K_num);
    }



}
