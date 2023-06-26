package com.example.dm_test;

import com.example.dm_test.entity.Iris;
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
//	private ClusteringService clusteringService;
	private ClassificationService classificationService;
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
	public void testClustering()
	{
		classificationService.performClassification();
	}

}
