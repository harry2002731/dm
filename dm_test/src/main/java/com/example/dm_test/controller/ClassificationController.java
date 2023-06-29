package com.example.dm_test.controller;

import com.example.dm_test.entity.ClusterRes;
import com.example.dm_test.entity.Iris;
import com.example.dm_test.entity.ValidateData;
import com.example.dm_test.service.ClassificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/classification")
public class ClassificationController {

    private static final Logger logger = LoggerFactory.getLogger(ClassificationController.class);

    @Autowired
    private ClassificationService classificationService;

    @GetMapping("/classify_vali")
    public List<ValidateData> getValidationData()
    {
        return classificationService.validatePrecision();
    }

    @PostMapping(value = "/new_data_vali")
    public String getClassificationResult(@RequestBody Iris dataForClassification)
    {
        logger.info("Received post request");
        float attribute1 = dataForClassification.getSepL();
        float attribute2 = dataForClassification.getSepW();
        float attribute3 = dataForClassification.getPetL();
        float attribute4 = dataForClassification.getPetW();
        return classificationService.performClassification(attribute1,attribute2,attribute3,attribute4);
    }

    @GetMapping("/tree_visualization")
    public String getImageUrl(@RequestParam() int height, @RequestParam() int leaves)
    {
        logger.info("received params: height {}, leaves {}", height, leaves);
        return classificationService.getTreeVisualization(height,leaves);
    }

}
