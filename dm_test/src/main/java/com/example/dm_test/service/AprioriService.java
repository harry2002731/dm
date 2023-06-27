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

import java.util.*;

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

    public List<AprioriData> getAllApriori()
    {
        return aprioriMapper.getAllApriori();
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
    //self-gen apriori
//private List<Set<String>> transactions;
//    private Map<Set<String>, Integer> itemsets;
//    private double minConfidence;
//    private double minSupport;
//
//    public List<String> runApriori(List<Set<String>> transactions, double minConfidence, double minSupport) {
//        this.transactions = transactions;
//        this.minConfidence = minConfidence;
//        this.minSupport = minSupport;
//        this.itemsets = new HashMap<>();
//
//        generateFrequentItemsets();
//        return generateAssociationRules();
//    }
//
//    private void generateFrequentItemsets() {
//        int transactionCount = transactions.size();
//        Map<Set<String>, Integer> candidateItemsets = new HashMap<>();
//
//        // Generate 1-itemsets
//        for (Set<String> transaction : transactions) {
//            for (String item : transaction) {
//                Set<String> itemset = new HashSet<>();
//                itemset.add(item);
//                candidateItemsets.put(itemset, candidateItemsets.getOrDefault(itemset, 0) + 1);
//            }
//        }
//
//        // Prune 1-itemsets based on minSupport
//        candidateItemsets.entrySet().removeIf(entry -> (entry.getValue() / (double) transactionCount) < minSupport);
//
//        // Generate k-itemsets (k > 1)
//        // Generate k-itemsets (k > 1)
//        boolean hasCandidateItemsets = true;
//        while (hasCandidateItemsets) {
//            itemsets.putAll(candidateItemsets);
//            candidateItemsets = generateCandidateItemsets(candidateItemsets);
//
//            // Prune k-itemsets based on minSupport
//            candidateItemsets.entrySet().removeIf(entry -> (entry.getValue() / (double) transactionCount) < minSupport);
//
//            // Check if there are any candidate itemsets
////            hasCandidateItemsets = !candidateItemsets.isEmpty();
//            if (candidateItemsets.isEmpty())
//            {
//                hasCandidateItemsets = false;
//            }
//        }
//
//
//
//        System.out.println("1");
//    }
//
//    private Map<Set<String>, Integer> generateCandidateItemsets(Map<Set<String>, Integer> prevItemsets) {
//        Map<Set<String>, Integer> candidateItemsets = new HashMap<>(prevItemsets);
//
//        List<Set<String>> prevItemsetList = new ArrayList<>(prevItemsets.keySet());
//        int k = prevItemsetList.get(0).size() + 1;
//
//        for (int i = 0; i < prevItemsetList.size() - 1; i++) {
//            for (int j = i + 1; j < prevItemsetList.size(); j++) {
//                Set<String> itemset1 = prevItemsetList.get(i);
//                Set<String> itemset2 = prevItemsetList.get(j);
//
//                Set<String> union = new HashSet<>(itemset1);
//                union.addAll(itemset2);
//
//                if (union.size() == k && !hasInfrequentSubset(union, prevItemsetList)) {
//                    candidateItemsets.put(union, 0);
//                }
//            }
//        }
//
//        for (Set<String> transaction : transactions) {
//            for (Set<String> itemset : candidateItemsets.keySet()) {
//                if (transaction.containsAll(itemset)) {
//                    candidateItemsets.put(itemset, candidateItemsets.get(itemset) + 1);
//                }
//            }
//        }
//
//        return candidateItemsets;
//    }
//
//    private boolean hasInfrequentSubset(Set<String> itemset, List<Set<String>> prevItemsetList) {
//        List<Set<String>> subsets = generateSubsets(itemset);
//
//        for (Set<String> subset : subsets) {
//            if (!prevItemsetList.contains(subset)) {
//                return true;
//            }
//        }
//
//        return false;
//    }
//
//    private List<Set<String>> generateSubsets(Set<String> itemset) {
//        List<Set<String>> subsets = new ArrayList<>();
//        String[] itemArray = itemset.toArray(new String[0]);
//        int n = itemArray.length;
//
//        for (int i = 0; i < (1 << n); i++) {
//            Set<String> subset = new HashSet<>();
//            for (int j = 0; j < n; j++) {
//                if ((i & (1 << j)) > 0) {
//                    subset.add(itemArray[j]);
//                }
//            }
//            subsets.add(subset);
//        }
//
//        return subsets;
//    }
//
//    private List<String> generateAssociationRules() {
//        System.out.println("2");
//        List<String> associationRules = new ArrayList<>();
//
//        for (Map.Entry<Set<String>, Integer> entry : itemsets.entrySet()) {
//            Set<String> itemset = entry.getKey();
//            if (itemset.size() > 1) {
//                List<Set<String>> subsets = generateSubsets(itemset);
//
//                for (Set<String> subset : subsets) {
//                    if (subset.size() < itemset.size()) {
//                        double confidence = entry.getValue() / (double) itemsets.get(subset);
//                        double support = entry.getValue() / (double) transactions.size();
//
//                        if (confidence >= minConfidence) {
//                            String rule = subset + " => " + itemset + " (Confidence: " + confidence + ", Support: " + support + ")";
//                            associationRules.add(rule);
//                        }
//                    }
//                }
//            }
//        }
//
//        return associationRules;
//    }
    }


