package com.example.dm_test.service;

import com.example.dm_test.entity.AprioriData;
import com.example.dm_test.mapper.AprioriMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.AssociationRules;
import weka.core.*;
import weka.filters.Filter;
import weka.filters.supervised.attribute.NominalToBinary;
import weka.filters.unsupervised.attribute.NumericToBinary;
import weka.filters.unsupervised.attribute.NumericToNominal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AprioriService {
    @Autowired
    private AprioriMapper aprioriMapper;

    private Instances convertToInstances(List<AprioriData> aprioriDataList)
    {
        // 创建属性列表
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("milk"));
        attributes.add(new Attribute("bread"));
        attributes.add(new Attribute("eggs"));
        attributes.add(new Attribute("coke"));
        attributes.add(new Attribute("cereal"));

        // 创建Instances对象
        Instances instances = new Instances("iris", attributes, aprioriDataList.size());
        instances.setClassIndex(attributes.size() - 1);

        for (AprioriData aprioriData : aprioriDataList) {
            double[] values = new double[attributes.size()];
            values[0] = aprioriData.getMilk();
            values[1] = aprioriData.getBread();
            values[2] = aprioriData.getEggs();
            values[3] = aprioriData.getCoke();
            values[4] = aprioriData.getCereal();

            Instance instance = new DenseInstance(1.0, values);
            instances.add(instance);
        }

        NumericToNominal numericToNominal = new NumericToNominal();
        try
        {
            numericToNominal.setInputFormat(instances);
            instances = Filter.useFilter(instances, numericToNominal);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return instances;
    }

    public void performApriori()
    {
        List<AprioriData> aprioriDataList = aprioriMapper.getAllApriori();

        Instances instances = convertToInstances(aprioriDataList);

        System.out.println(instances);
        Apriori apriori = new Apriori();
        apriori.setCar(false);
//        apriori.setNumRules(10);
//        apriori.setMinMetric(0.5);
        SelectedTag metricType = new SelectedTag(0,Apriori.TAGS_SELECTION);
        apriori.setMetricType(metricType);
        apriori.setLowerBoundMinSupport(0.4);
        apriori.setUpperBoundMinSupport(0.5);
        apriori.setMinMetric(0.5);

        try{
            apriori.buildAssociations(instances);
            System.out.println(apriori);
            apriori.setLowerBoundMinSupport(0.4);
            AssociationRules rules = apriori.getAssociationRules();
            // 格式化输出二阶关联规则
            for (AssociationRule rule : rules.getRules()) {
                if (rule.getPremise().size() == 1 && rule.getConsequence().size() == 1) {
                    String item1 = rule.getPremise().toString();  // 项目1
                    String item2 = rule.getConsequence().toString();  // 项目2
                    double support = rule.getTotalSupport();  // 支持度

                    String formattedRule = "{" + item1 + ", " + item2 + ", " + support + "}";
                    System.out.println(formattedRule);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
