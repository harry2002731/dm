package com.example.dm_test.service;
import com.example.dm_test.entity.ClusterRes;
import com.example.dm_test.entity.Iris;
import com.example.dm_test.entity.RegressionData;
import com.example.dm_test.mapper.ClusteringMapper;
import com.example.dm_test.mapper.IrisMapper;
import elki.clustering.dbscan.DBSCAN;
import elki.data.Cluster;
import elki.data.Clustering;
import elki.data.DoubleVector;
import elki.data.NumberVector;
import elki.data.type.TypeUtil;
import elki.database.StaticArrayDatabase;
import elki.database.ids.DBID;
import elki.database.ids.DBIDIter;
import elki.database.ids.DBIDs;
import elki.database.relation.Relation;
import elki.datasource.ArrayAdapterDatabaseConnection;
import elki.distance.minkowski.EuclideanDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.DenseInstance;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.PrincipalComponents;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClusteringService {
    @Autowired
    private IrisMapper irisMapper;

    @Autowired
    private ClusteringMapper clusteringMapper;

    private Instances getData(int dataset)
    {
        // params dataset 1 for iris, 2 for 2 centers moon dataset, 3 for 3 centers dataset
        if (dataset == 1)
        {
            List<Iris> irisList = irisMapper.getAllIris();
            return convertToInstances(irisList);
        } else if (dataset == 2) {
            List<RegressionData> regressionDataList = clusteringMapper.get2moonData();
            return convertToX_YInstances(regressionDataList);
        } else if (dataset == 3)
        {
            List<RegressionData> regressionDataList = clusteringMapper.get3clusterData();
            return convertToX_YInstances(regressionDataList);
        }
        return null;
    }

    public List<ClusterRes> performKmeansClustering(int K_num, int dataset)
    {
        List<ClusterRes> clusterRes = new ArrayList<>();
        Instances instances = getData(dataset);
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
//            System.out.println(instance);
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

    // for self-generated dataset

    private Instances convertToX_YInstances(List<RegressionData> regressionDataList) {
        // 创建属性列表
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("x"));
        attributes.add(new Attribute("y"));

        // 创建Instances对象
        Instances instances = new Instances("regression", attributes, regressionDataList.size());
//        instances.setClassIndex(attributes.size() - 1);

        // 添加每个Iris对象作为Instance
        for (RegressionData regressionData : regressionDataList) {
            double[] values = new double[attributes.size()];
            values[0] = regressionData.getX();
            values[1] = regressionData.getY();


            Instance instance = new DenseInstance(1.0, values);
            instances.add(instance);
//            System.out.println(instance);
        }

        return instances;
    }

    private StaticArrayDatabase instanceToELKI(Instances instances)
    {
        int numInstances = instances.numInstances();
        int numAttributes = instances.numAttributes();
        double[][] data = new double[numInstances][numAttributes];
        for (int i = 0; i < numInstances; i++) {
            Instance instance = instances.instance(i);
            for (int j = 0; j < numAttributes; j++) {
                data[i][j] = instance.value(j);
            }
        }

        // Create database connection
        ArrayAdapterDatabaseConnection conn = new ArrayAdapterDatabaseConnection(data);

        // Create static database
        StaticArrayDatabase db = new StaticArrayDatabase(conn, null);
        return db;
    }

    public List<ClusterRes> performDBSCAN(int dataset, double epsilon, int minPts)
    {
        List<ClusterRes> clusterRes = new ArrayList<>();
        Instances instances = getData(dataset);
        StaticArrayDatabase db = instanceToELKI(instances);
        db.initialize();
        DBSCAN<DoubleVector> dbscan = new DBSCAN<>(new EuclideanDistance(),epsilon, minPts);
        Relation<DoubleVector> relation = db.getRelation(TypeUtil.NUMBER_VECTOR_FIELD);
        Clustering<?> clustering = dbscan.run(relation);
        int cluster_num = 1;
        // 输出聚类结果
        for (Cluster<?> cluster : clustering.getAllClusters())
        {

            System.out.println("Cluster ID: " + cluster.getIDs()+ ", Size: " + cluster.size());

            DBIDs dbiDs = cluster.getIDs();
            for (DBIDIter iter = dbiDs.iter(); iter.valid(); iter.advance())
            {
                double[] org_data = relation.get(iter).toArray();
                double value1 = org_data[0];
                double value2 = org_data[1];
                ClusterRes res1 = new ClusterRes(value1,value2,cluster_num,"null");
                clusterRes.add(res1);
            }
            cluster_num = cluster_num + 1;

        }
        return clusterRes;

    }
}
