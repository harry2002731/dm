package com.example.dm_test.controller;


import com.example.dm_test.entity.ClusterRes;
import com.example.dm_test.service.ClusteringService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/clustering")
public class ClusteringController {
    private static final Logger logger = LoggerFactory.getLogger(ClusteringController.class);

    @Autowired
    private ClusteringService clusteringService;

    @GetMapping("/kmeans")
    public List<ClusterRes> getkmeansRes(@RequestParam(defaultValue = "3") int K_num, @RequestParam(defaultValue = "1") int dataset)
    {
        logger.info("Received request with K_num: {} , dataset choose {}", K_num, dataset);
        return clusteringService.performKmeansClustering(K_num, dataset);
    }

    @GetMapping("/dbscan")
    public List<ClusterRes> getDBSCANRes(@RequestParam int dataset, @RequestParam double epsilon, @RequestParam int minPts)
    {
        return clusteringService.performDBSCAN(dataset,epsilon,minPts);
    }
}
