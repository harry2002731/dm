package com.example.dm_test.controller;

import com.example.dm_test.entity.AprioriData;
import com.example.dm_test.service.AprioriService;
import com.example.dm_test.service.NAprioriService;
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
@RequestMapping("/apriori")
public class AprioriController {
    private static final Logger logger = LoggerFactory.getLogger(AprioriController.class);

    @Autowired
    private AprioriService aprioriService;

    @GetMapping("/get_heatmap")
    public List<double[]> getApriData(@RequestParam(defaultValue = "0.4") double support, @RequestParam(defaultValue = "0.5") double confidence)
    {
        List<AprioriData> aprioriDataList = aprioriService.getAllApriori();
        double support_processed = support / aprioriDataList.size();
        List<String> transactions = new ArrayList<>();
        for (AprioriData aprioriData : aprioriDataList)
        {
            String transaction = "";
            float bread = aprioriData.getBread();
            float egg = aprioriData.getEggs();
            float coke = aprioriData.getCoke();
            float cereal = aprioriData.getCereal();
            float milk = aprioriData.getMilk();

            if (milk == 1) {
                transaction += "milk,";
            }
            if (bread == 1) {
                transaction += "bread,";
            }
            if (egg == 1) {
                transaction += "eggs,";
            }
            if (coke == 1) {
                transaction += "coke,";
            }
            if (cereal == 1) {
                transaction += "cereal,";
            }

            // 移除最后一个逗号
            if (!transaction.isEmpty()) {
                transaction = transaction.substring(0, transaction.length() - 1);
                transactions.add(transaction);
            }

            System.out.println(transaction);
        }
        NAprioriService nAprioriService1 = new NAprioriService(transactions);
        logger.info("Received request support: {}, confidence : {}",support_processed, confidence);
        return nAprioriService1.performApriori(support, confidence);
    }
}
