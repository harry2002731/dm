package com.example.dm_test;

import com.example.dm_test.entity.AprioriData;
import com.example.dm_test.entity.ClusterRes;
import com.example.dm_test.entity.Iris;
import com.example.dm_test.service.*;
import org.apache.catalina.Cluster;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.*;

@SpringBootTest
class DmTestApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private IrisService irisService;

	@Autowired
	private ClusteringService clusteringService;
	@Autowired
	private ClassificationService classificationService;

	@Autowired
	private AprioriService aprioriService;

	@Autowired
	private NAprioriService nAprioriService;

	@Autowired
	private RegressionService regressionService;
	@Test
	public void testGetAllIris(){
		List<Iris> irisList = irisService.getAllIris();
		System.out.println(irisList);
		if (irisList != null) {
			for (Iris iris : irisList){
				System.out.println(iris.getSpecies());
			}
		} else {
			System.out.println("No iris data found.");
		}
	}

	@Test
	public void testClassification() throws IOException {
		String a = classificationService.performClassification(3.7f, 5.4f, 0.2f, 1.5f);
		System.out.println(a);
		String b = classificationService.getPath();
		System.out.println(b);
	}

//	@Test
//	public void testApriori()
//	{
//		Set<String> transaction1 = new HashSet<>(Arrays.asList("bread", "coke", "cereal"));
//		Set<String> transaction2 = new HashSet<>(Arrays.asList("milk", "coke"));
//		Set<String> transaction3 = new HashSet<>(Arrays.asList("milk", "bread", "cereal"));
//		Set<String> transaction4 = new HashSet<>(Arrays.asList("milk", "coke"));
//		Set<String> transaction5 = new HashSet<>(Arrays.asList("bread", "egg", "cereal"));
//		Set<String> transaction6 = new HashSet<>(Arrays.asList("milk", "bread", "coke"));
//		Set<String> transaction7 = new HashSet<>(Arrays.asList("milk", "bread", "egg", "cereal"));
//		Set<String> transaction8 = new HashSet<>(Arrays.asList("milk", "bread", "coke"));
//		Set<String> transaction9 = new HashSet<>(Arrays.asList("bread", "coke"));
//
//		List<Set<String>> transactions = new ArrayList<>();
//		transactions.add(transaction1);
//		transactions.add(transaction2);
//		transactions.add(transaction3);
//		transactions.add(transaction4);
//		transactions.add(transaction5);
//		transactions.add(transaction6);
//		transactions.add(transaction7);
//		transactions.add(transaction8);
//		transactions.add(transaction9);
//
//		double minConfidence = 0.5;
//		double minSupport = 0.4;
//		List<String> associationRules = aprioriService.runApriori(transactions, minConfidence, minSupport);
//
//		for (String rule : associationRules) {
//			System.out.println(rule);
//		}
//
//
//	}

	@Test
	public void testClustering()
	{
		List<ClusterRes> a = clusteringService.performClustering(3);
		System.out.println(a);
	}

	@Test
	public void testRegression()
	{
		regressionService.performRegression(2);
	}

	@Test
	public void testNApriori()
	{
//		apriori  test
		List<String> transactions = new ArrayList<>();
		List<double[]> out_data = new ArrayList<>();
		List<AprioriData> aprioriDataList = nAprioriService.getAllApData();

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

		double confidence = 0.5;
		double support = 0.4;

		NAprioriService nAprioriService1 = new NAprioriService(transactions);
		Map<List<String>,Integer> frequentCollectionMap = nAprioriService1.getFC(support,confidence);
		System.out.println("-------------频繁集" + "----------------");
		for (List<String> list : frequentCollectionMap.keySet()) {
			System.out.println(list + "\t" + frequentCollectionMap.get(list));

		}

		Map<String, Double> relationRulesMap = nAprioriService1.getRelationRules(frequentCollectionMap, confidence);
		System.out.println("-----------关联规则"+"----------------");
		double[] array = new double[3];
		for (String s : relationRulesMap.keySet()) {
			System.out.println(s + "\t" + relationRulesMap.get(s));
			String[] parts = s.split("→");
			String firstPart = parts[0];
			String secPart = parts[1];
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
					array[0] = 4;
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
			array[2] = relationRulesMap.get(s);
			out_data.add(array);
		}
		System.out.println(out_data);
//
		//data transfer test
//		System.out.println(nAprioriService.getAllApData());

//		List<String> data = Arrays.asList("1,1,1,0,0", "0,1,0,0,1", "0,1,1,0,1", "0,1,0,0,1", "1,0,1,0,1", "0,1,1,0,1", "1,1,1,1,1", "1,1,0,0,1");



//		List<Set<String>> transactions = nAprioriService.convertData(data);
//
//		for (Set<String> transaction : transactions) {
//			System.out.println(transaction);
//		}
	}
}
