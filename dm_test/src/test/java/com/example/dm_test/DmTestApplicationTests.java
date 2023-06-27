package com.example.dm_test;

import com.example.dm_test.entity.ClusterRes;
import com.example.dm_test.entity.Iris;
import com.example.dm_test.service.AprioriService;
import com.example.dm_test.service.ClassificationService;
import com.example.dm_test.service.ClusteringService;
import com.example.dm_test.service.IrisService;
import org.apache.catalina.Cluster;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
	public void testClassification()
	{
		String a = classificationService.performClassification(3.7f, 5.4f, 0.2f, 1.5f);
		System.out.println(a);
	}

	@Test
	public void testApriori()
	{
		aprioriService.performApriori();
	}

	@Test
	public void testClustering()
	{
		List<ClusterRes> a = clusteringService.performClustering(3);
		System.out.println(a);
	}

}
