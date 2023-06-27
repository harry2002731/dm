package com.example.dm_test.service;

import com.example.dm_test.entity.RegressionData;
import com.example.dm_test.mapper.RegressionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

@Service
public class RegressionService {
    @Autowired
    private RegressionMapper regressionMapper;

    private Instances convertToInstances(List<RegressionData> regressionDataList) {
        // 创建属性列表
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("x"));
        attributes.add(new Attribute("y"));

        // 创建Instances对象
        Instances instances = new Instances("regression", attributes, regressionDataList.size());
        instances.setClassIndex(attributes.size() - 1);

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

    public static double[] polynomialRegression(Instances data, int degree) {
        // 创建WeightedObservedPoints对象用于保存数据点
        WeightedObservedPoints obs = new WeightedObservedPoints();

        // 将Instances对象中的数据点添加到WeightedObservedPoints对象中
        for (Instance instance : data) {
            double x = instance.value(0);  // 假设输入数据只有一个属性，即x值
            double y = instance.value(1);  // 假设输出数据只有一个属性，即y值
            obs.add(x, y);
        }

        // 使用PolynomialCurveFitter进行多项式拟合
        PolynomialCurveFitter fitter = PolynomialCurveFitter.create(degree);
        double[] coefficients = fitter.fit(obs.toList());

        return coefficients;
    }

    public double[] performRegression(int degree)
    {
        List<RegressionData> regressionData = regressionMapper.getAllregression();
        Instances instances = convertToInstances(regressionData);

        try
        {
            double[] coefficients = polynomialRegression(instances, degree);

            // 输出拟合的多项式系数
            System.out.println("拟合的多项式系数：");
            for (int i = 0; i <= degree; i++) {
                System.out.println("x^" + i + ": " + coefficients[i]);
            }
            return coefficients;


        }catch (Exception e)
        {
            e.printStackTrace();
            double[] a = new double[0];
            return a;
        }





    }
}
