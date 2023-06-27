package com.example.dm_test.service;
import com.example.dm_test.entity.ClusterRes;
import com.example.dm_test.entity.Iris;
import com.example.dm_test.mapper.IrisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.DenseInstance;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.PrincipalComponents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ClusteringService {
    @Autowired
    private IrisMapper irisMapper;
    public List<ClusterRes> performClustering(int K_num)
    {
        List<Iris> irisList = irisMapper.getAllIris();
        List<ClusterRes> clusterRes = new ArrayList<>();

        Instances instances = convertToInstances(irisList);
        System.out.println(instances);
        SimpleKMeans kMeans = new SimpleKMeans();
        try {
            kMeans.setNumClusters(K_num);
            kMeans.buildClusterer(instances);

            // 输出聚类结果
            for (int i = 0; i < instances.numInstances(); i++) {
                int cluster = kMeans.clusterInstance(instances.instance(i));
                double[] data = instances.instance(i).toDoubleArray();
                double value1 = data[0];
                double value2 = data[1];
                ClusterRes res1 = new ClusterRes(value1, value2, cluster, "null");
                clusterRes.add(res1);
            }
            return clusterRes;
        }catch (Exception e)
        {
            e.printStackTrace();
            ClusterRes res1 = new ClusterRes(0, 0, 0, "null");
            clusterRes.add(res1);
            return clusterRes;
        }


    }

    private Instances convertToInstances(List<Iris> irisList) {
        // 创建属性列表
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("sepalLength"));
        attributes.add(new Attribute("sepalWidth"));
        attributes.add(new Attribute("petalLength"));
        attributes.add(new Attribute("petalWidth"));


        // 创建Instances对象
        Instances instances = new Instances("iris", attributes, irisList.size());
//        instances.setClassIndex(attributes.size() - 1);

        // 添加每个Iris对象作为Instance
        for (Iris iris : irisList) {
            double[] values = new double[attributes.size()];
            values[0] = iris.getSepL();
            values[1] = iris.getSepW();
            values[2] = iris.getPetL();
            values[3] = iris.getPetW();

            Instance instance = new DenseInstance(1.0, values);
            instances.add(instance);
            System.out.println(instance);
        }

        // 使用PCA降维为2维
        PrincipalComponents pca = new PrincipalComponents();
        pca.setMaximumAttributeNames(2);
        try
        {
            pca.setInputFormat(instances);
            instances = Filter.useFilter(instances, pca);
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        return instances;
    }
}
