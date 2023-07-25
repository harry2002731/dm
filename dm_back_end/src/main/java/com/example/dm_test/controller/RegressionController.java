package com.example.dm_test.controller;


import com.example.dm_test.entity.Iris;
import com.example.dm_test.entity.RegressionData;
import com.example.dm_test.service.RegressionService;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/regression")
public class RegressionController {
    private static final Logger logger = LoggerFactory.getLogger(RegressionController.class);

    @Autowired
    private RegressionService regressionService;

    @GetMapping("/get_data")
    public List<RegressionData> getRegressionData()
    {
        return regressionService.getRegressionData();
    }

    @GetMapping("/poly_res")
    public double[] getRegressionResult(@RequestParam(defaultValue = "2") int degree)
    {
        logger.info("Received request params: {}",degree);
        return regressionService.performPolyRegression(degree);
    }

    @GetMapping("/linear_test")
    public double[] getSimpleRegressionResult()
    {
        logger.info("Received call");
        return regressionService.performSimpleRegression();
    }

    @GetMapping("/ransac")
    public double[] getRansacResult()
    {
        logger.info("Received call");
        return regressionService.performRANSAC();
    }

    @GetMapping("/poly_res/two")
    public double[] get2RegressionResult()
    {
        logger.info("Received request params: {}",2);
        return regressionService.performPolyRegression(2);
    }

    @GetMapping("/poly_res/three")
    public double[] get3RegressionResult()
    {
        logger.info("Received request params: {}",3);
        return regressionService.performPolyRegression(3);
    }

    @GetMapping("/poly_res/four")
    public double[] get4RegressionResult()
    {
        logger.info("Received request params: {}",4);
        return regressionService.performPolyRegression(4);
    }

    @GetMapping("/poly_res/five")
    public double[] get5RegressionResult()
    {
        logger.info("Received request params: {}",5);
        return regressionService.performPolyRegression(5);
    }

    @GetMapping("/all")
    public List<double[]> getAllData()
    {
        List<double[]> res = new ArrayList<>();
        double[] simple = regressionService.performSimpleRegression();
        res.add(simple);
        for (int i = 2; i<= 5; i++)
        {
            double[] poly = regressionService.performPolyRegression(i);
            res.add(poly);
        }
        res.add(regressionService.performRANSAC());
        return res;
    }

    @GetMapping("/page")
    public PageInfo<RegressionData> getAllPageData(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize)
    {
        logger.info("Received request with pageNum: {}, pageSize: {}", pageNum, pageSize);
        return regressionService.findPagedRegressionData(pageNum, pageSize);
    }
}
