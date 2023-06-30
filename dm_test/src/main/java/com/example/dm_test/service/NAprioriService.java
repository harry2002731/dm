package com.example.dm_test.service;

import com.example.dm_test.entity.AprioriData;
import com.example.dm_test.mapper.AprioriMapper;
import com.example.dm_test.mapper.IrisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

@Service
public class NAprioriService {
    @Autowired
    private AprioriMapper aprioriMapper;

    public List<AprioriData> getAllApData()
    {
        return aprioriMapper.getAllApriori();
    }
    private List<String> transList;

    public NAprioriService(List<String> transList)
    {
        this.transList = transList;
    }

    private final static String ITEM_SPLIT=","; // 项之间的分隔符
    private final static String CON=" → "; // 项之间的分隔符

    /**
     *
     * @return  交易的所有频繁项集
     */
    public Map<List<String>, Integer> getFC(double SUPPORT, double Confidence) {
        Map<List<String>, Integer> frequentCollectionMap = new HashMap<>();// 所有的频繁集
        frequentCollectionMap.putAll(getItem1FC(Confidence));// 合并频繁1项集

        Map<List<String>, Integer> itemkFcMap = getItem1FC(Confidence), candidateCollection;
        while (itemkFcMap != null && itemkFcMap.size() != 0) {
            candidateCollection = getCandidateCollection(itemkFcMap);// 获得候选集

            // 对候选集项进行累加计数
            for (List<String> candidate : candidateCollection.keySet()) {
                if (candidate.size() == 2) {
                    for (String trans : transList) {
                        boolean flag = true;// 用来判断交易中是否出现该候选项，如果出现，计数加1
                        for (String candidateItem : candidate) {
                            if (trans.indexOf(candidateItem) == -1) {
                                flag = false;
                                break;
                            }
                        }
                        if (flag) {
                            candidateCollection.put(candidate, candidateCollection.get(candidate) + 1);
                        }
                    }
                }
            }

            itemkFcMap.clear();
            // 从候选集中找到符合支持度的频繁集项
            for (List<String> candidate : candidateCollection.keySet()) {
                Integer fc = candidateCollection.get(candidate);
                if (fc >= SUPPORT) {
                    itemkFcMap.put(candidate, fc);
                }
            }

            // 合并所有频繁集
            frequentCollectionMap.putAll(itemkFcMap);
        }
        return frequentCollectionMap;
    }

    /**
     * 关联规则
     * @param frequentCollectionMap 频繁集
     * @return 关联规则
     */
    public Map<String,Double> getRelationRules(Map<List<String>,Integer> frequentCollectionMap, double CONFIDENCE){
        Map<String,Double> relationRules=new HashMap<>();
        for (List<String> itmes : frequentCollectionMap.keySet()){
            if (itmes.size()>1){
                double countAll = frequentCollectionMap.get(itmes);
                List<List<String>> result = getSubsets(itmes);//获得itmes的所有非空子集

                for (List<String> itemList : result){
                    if (itemList.size() < itmes.size()){//只处理真子集

                        StringBuilder reasonStr = new StringBuilder();//前置
                        StringBuilder resultStr = new StringBuilder();//结果
                        for (String item : itemList) reasonStr.append(ITEM_SPLIT).append(item);
                        for (String item : itmes) if (!itemList.contains(item)) resultStr.append(ITEM_SPLIT).append(item);

                        double countReason = frequentCollectionMap.get(itemList);
                        double itemConfidence = countAll / countReason;//计算置信度

                        if (itemConfidence >= CONFIDENCE){
                            String rule = reasonStr.append(CON).append(resultStr.substring(1)).substring(1);
                            relationRules.put(rule,itemConfidence);
                        }
                    }
                }

            }
        }
        return relationRules;
    }


    /**
     *对于给定的频繁K项集，获得他的K+1项候选集
     * @param itemkFcMap 频繁K项集
     * @return  K+1项候选集
     */
    private Map<List<String>, Integer> getCandidateCollection(Map<List<String>, Integer> itemkFcMap) {
        Map<List<String>, Integer> candidateCollection = new HashMap<>();
        Set<List<String>> itemkSet1 = itemkFcMap.keySet();
        Set<List<String>> itemkSet2 = itemkFcMap.keySet();

        // 连接
        for (List<String> itemk1 : itemkSet1) {
            for (List<String> itemk2 : itemkSet2) {
                if (!itemk1.equals(itemk2) && itemk1.size() + itemk2.size() == 2) {
                    for (String item : itemk2) {
                        if (itemk1.contains(item)) continue;
                        List<String> temp = new ArrayList<>(itemk1);
                        temp.add(item);
                        temp.sort(Comparator.naturalOrder());
                        candidateCollection.put(temp, 0);
                    }
                }
            }
        }
        return candidateCollection;
    }
    /**
     * 获取频繁1项集
     * @return map<key,value> key-items value-frequency
     */
    private Map<List<String>,Integer> getItem1FC(double SUPPORT){
        Map<List<String>,Integer> sItem1FCMap = new HashMap<>();//statistics frequency of each item
        Map<List<String>,Integer> rItem1FCMap = new HashMap<>();//频繁1项集

        for (String trans : transList){
            String[] items = trans.split(ITEM_SPLIT);
            for (String item : items){
                List<String> itemList = new ArrayList<>();
                itemList.add(item);
                sItem1FCMap.put(itemList,sItem1FCMap.getOrDefault(itemList,0)+1);
            }
        }

        for (List itemList : sItem1FCMap.keySet()){
            Integer fc = sItem1FCMap.get(itemList);
            if (fc>=SUPPORT) rItem1FCMap.put(itemList,fc);
        }
        return rItem1FCMap;
    }

    /**
     * 构造子集
     * @param sourceSet
     * @return sourceSet的所有非空子集
     */
    private List<List<String>> getSubsets(List<String> sourceSet)
    {
        List<List<String>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        for(int i = 0;i<sourceSet.size();i++){
            int size = result.size();
            for (int j = 0;j<size;j++){
                List<String> temp = new ArrayList<>(result.get(j));
                temp.add(sourceSet.get(i));
                result.add(temp);
            }
        }
        return result.subList(1,result.size());//去掉空集
    }

    public static List<Set<String>> convertData(List<String> data) {
        List<Set<String>> transactions = new ArrayList<>();

        for (String transaction : data) {
            String[] items = transaction.split(",");
            Set<String> itemSet = new HashSet<>();

            for (int i = 0; i < items.length; i++) {
                if (items[i].equals("1")) {
                    itemSet.add("Item" + (i + 1));
                }
            }

            transactions.add(itemSet);
        }

        return transactions;
    }

    public void printSecondOrderRules(Map<List<String>, Integer> frequentCollectionMap, double CONFIDENCE) {
        Map<String, Double> relationRules = new HashMap<>();
        for (List<String> items : frequentCollectionMap.keySet()) {
            if (items.size() > 1) {
                double countAll = frequentCollectionMap.get(items);
                List<List<String>> result = getSubsets(items);//获得items的所有非空子集

                for (List<String> itemList : result) {
                    if (itemList.size() == 2) {//只处理二阶关联规则

                        StringBuilder reasonStr = new StringBuilder();//前置
                        StringBuilder resultStr = new StringBuilder();//结果
                        for (String item : itemList) reasonStr.append(ITEM_SPLIT).append(item);
                        for (String item : items) if (!itemList.contains(item)) resultStr.append(ITEM_SPLIT).append(item);

                        double countReason = frequentCollectionMap.get(itemList);
                        double itemConfidence = countAll / countReason;//计算置信度

                        if (itemConfidence >= CONFIDENCE) {
                            String rule = reasonStr.append(CON).append(resultStr).substring(1);
                            relationRules.put(rule, itemConfidence);
                        }
                    }
                }

            }
        }

        // 格式化输出
//        for (Map.Entry<String, Double> entry : relationRules.entrySet()) {
//            String[] ruleParts = entry.getKey().split(CON);
//            String[] reasonItems = ruleParts[0].split(ITEM_SPLIT);
//            String[] resultItems = new String[0];
//            if (ruleParts.length > 1) {
//                resultItems = ruleParts[1].split(ITEM_SPLIT);
//            }
//            double support = frequentCollectionMap.get(Arrays.asList(reasonItems)) / (double) frequentCollectionMap.size();
//            double confidence = entry.getValue();
//
//            System.out.println(Arrays.toString(reasonItems) + ", " + Arrays.toString(resultItems) + ", " + support + ", " + confidence);
//        }
        for (Map.Entry<String, Double> entry : relationRules.entrySet()) {
            String[] ruleParts = entry.getKey().split(CON);
            String[] reasonItems = ruleParts[0].split(ITEM_SPLIT);
            String[] resultItems = new String[0];

            // 判断 ruleParts 的长度是否大于1
            if (ruleParts.length > 1) {
                resultItems = ruleParts[1].split(ITEM_SPLIT);
            }

            // 只输出二阶关联规则
            if (reasonItems.length == 2 && resultItems.length == 1) {
                double support = frequentCollectionMap.get(Arrays.asList(reasonItems)) / (double) frequentCollectionMap.size();
                double confidence = entry.getValue();

                String reasonStr = Arrays.toString(reasonItems).replaceAll("[\\[\\]]", "");
                String resultStr = Arrays.toString(resultItems).replaceAll("[\\[\\]]", "");

                System.out.printf("[%s], [%s], %.2f, %.2f%n", reasonStr, resultStr, support, confidence);
            }
        }




    }

    public List<double[]> performApriori(double supp, double confid)
    {
        System.out.println("enfcwicfnirwhfciuhruic");
        System.out.println(supp);
        List<String> transactions = new ArrayList<>();
        List<double[]> out_data = new ArrayList<>();

//        List<AprioriData> aprioriDataList = getAllApData();

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

        Map<List<String>,Integer> frequentCollectionMap = getFC(supp,confid);
        System.out.println("-------------频繁集" + "----------------");
        for (List<String> list : frequentCollectionMap.keySet()) {
            System.out.println(list + "\t" + frequentCollectionMap.get(list));

        }

        Map<String, Double> relationRulesMap = getRelationRules(frequentCollectionMap, confid);
        System.out.println("-----------关联规则"+"----------------");

        for (String s : relationRulesMap.keySet()) {
            double[] array = new double[3];
            System.out.println("hedecdewifchiwrhfihriewhfi");
            System.out.println(s + "\t" + relationRulesMap.get(s));
            String[] parts = s.split("→");
            String firstPart = parts[0].trim();
            String secPart = parts[1].trim();
            System.out.println("First part: " + firstPart);
            System.out.println("Second part: " + secPart);
            switch (firstPart) {
                case "milk":
                    array[0] = 0;
                    break;
                case "bread":
                    array[0] = 1;
                    break;
                case "eggs":
                    array[0] = 2;
                    break;
                case "coke":
                    array[0] = 3;
                    break;
                case "cereal":
                    array[0] = 4.0;
                    break;
            }
            switch (secPart) {
                case "milk":
                    array[1] = 0;
                    break;
                case "bread":
                    array[1] = 1;
                    break;
                case "eggs":
                    array[1] = 2;
                    break;
                case "coke":
                    array[1] = 3;
                    break;
                case "cereal":
                    array[1] = 4;
                    break;
            }
            double confi = relationRulesMap.get(s);
            BigDecimal two = new BigDecimal(confi);
            double flitered = two.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
            array[2] = flitered;
            out_data.add(array);
        }
        return out_data;
    }

}
