package com.example.dm_test.controller;
import com.example.dm_test.entity.Iris;
import com.example.dm_test.entity.StatsData;
import com.example.dm_test.mapper.IrisMapper;
import com.example.dm_test.service.*;
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

    @Autowired
    private RegressionService regressionService;

    @Autowired
    private AprioriService aprioriService;



    @GetMapping("/api/user/page")
    public PageInfo<Iris> getAllUsers(@RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize)
    {
        logger.info("Received request with pageNum: {}, pageSize: {}", pageNum, pageSize);
        return irisService.findAll(pageNum, pageSize);
    }

//    @PostMapping(value = "/api/post_test")
//    public String getClassificationResult(@RequestBody Iris dataForClassification)
//    {
//        logger.info("Received post request");
//        float attribute1 = dataForClassification.getSepL();
//        float attribute2 = dataForClassification.getSepW();
//        float attribute3 = dataForClassification.getPetL();
//        float attribute4 = dataForClassification.getPetW();
//        return classificationService.performClassification(attribute1,attribute2,attribute3,attribute4);
//    }

//    @GetMapping("/api/clu_test")
//    public List<ClusterRes> getClusteringresult(@RequestParam(defaultValue = "3") int K_num, @RequestParam(defaultValue = "1") int dataset)
//    {
//        logger.info("Received request with K_num: {} , dataset choose {}", K_num, dataset);
//        return clusteringService.performKmeansClustering(K_num, dataset);
//    }

//    @GetMapping("/api/dbscan")
//    public List<ClusterRes> getDBSCANRes(@RequestParam int dataset, @RequestParam double epsilon, @RequestParam int minPts)
//    {
//        return clusteringService.performDBSCAN(dataset,epsilon,minPts);
//    }

//    @GetMapping("/api/reg_test")
//    public double[] getRegressionResult(@RequestParam(defaultValue = "2") int degree)
//    {
//        logger.info("Received request params: {}",degree);
//        return regressionService.performPolyRegression(degree);
//    }

//    @GetMapping("/api/apri")
//    public List<double[]> getApriData(@RequestParam(defaultValue = "0.4") double support, @RequestParam(defaultValue = "0.5") double confidence)
//    {
//        List<AprioriData> aprioriDataList = aprioriService.getAllApriori();
//        double support_processed = support / aprioriDataList.size();
//        List<String> transactions = new ArrayList<>();
//        for (AprioriData aprioriData : aprioriDataList)
//        {
//            String transaction = "";
//            float bread = aprioriData.getBread();
//            float egg = aprioriData.getEggs();
//            float coke = aprioriData.getCoke();
//            float cereal = aprioriData.getCereal();
//            float milk = aprioriData.getMilk();
//
//            if (milk == 1) {
//                transaction += "milk,";
//            }
//            if (bread == 1) {
//                transaction += "bread,";
//            }
//            if (egg == 1) {
//                transaction += "eggs,";
//            }
//            if (coke == 1) {
//                transaction += "coke,";
//            }
//            if (cereal == 1) {
//                transaction += "cereal,";
//            }
//
//            // 移除最后一个逗号
//            if (!transaction.isEmpty()) {
//                transaction = transaction.substring(0, transaction.length() - 1);
//                transactions.add(transaction);
//            }
//
//            System.out.println(transaction);
//        }
//        NAprioriService nAprioriService1 = new NAprioriService(transactions);
//        logger.info("Received request support: {}, confidence : {}",support_processed, confidence);
//        return nAprioriService1.performApriori(support, confidence);
//    }

//    @GetMapping("/api/tree_visualization")
//    public String getImageUrl(@RequestParam() int height, @RequestParam() int leaves)
//    {
//        logger.info("received params: height {}, leaves {}", height, leaves);
//        return classificationService.getTreeVisualization(height,leaves);
//    }

//    @GetMapping("/api/static_test")
//    public String getPathURL() {
//        return classificationService.getPath();
//    }

    @PostMapping("/api/update")
    public void updateIrisData(@RequestBody List<Iris> dataForUpdate)
    {
        for (Iris iris : dataForUpdate) {
            irisMapper.updateIris(iris);
        }

    }

    @GetMapping("/api/stat")
    public List<StatsData> getStats()
    {
        return irisService.getStats();
    }




}
