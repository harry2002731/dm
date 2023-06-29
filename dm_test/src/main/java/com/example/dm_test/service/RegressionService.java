package com.example.dm_test.service;

import com.example.dm_test.entity.RegressionData;
import com.example.dm_test.mapper.RegressionMapper;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.fitting.PolynomialCurveFitter;
import org.apache.commons.math3.fitting.WeightedObservedPoints;

@Service
public class RegressionService {
    @Autowired
    private RegressionMapper regressionMapper;

    public List<RegressionData> getRegressionData()
    {
        return regressionMapper.getAllregression_noise();
    }

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

    private double[][] simpleRegressionData(Instances data)
    {
        int length = data.numInstances();

        // 创建WeightedObservedPoints对象用于保存数据点
        WeightedObservedPoints obs = new WeightedObservedPoints();

        double[][] dataArray = new double[length][2];
        int i = 0;
        // 将Instances对象中的数据点添加到WeightedObservedPoints对象中
        for (Instance instance : data) {
//            double x = instance.value(0);  // 假设输入数据只有一个属性，即x值
//            double y = instance.value(1);  // 假设输出数据只有一个属性，即y值
//            obs.add(x, y);
            dataArray[i][0] = instance.value(0);
            dataArray[i][1] = instance.value(1);
            i = i + 1;
        }

        return dataArray;


    }

    public double[] performSimpleRegression()
    {
        List<RegressionData> regressionData = regressionMapper.getAllregression_noise();
        Instances instances = convertToInstances(regressionData);
        double[][] data = simpleRegressionData(instances);
        SimpleRegression regression = new SimpleRegression();
        regression.addData(data);
        double[] linear_res = new double[2];
        linear_res[1] = regression.getSlope();
        linear_res[0] = regression.getIntercept();
        return linear_res;

    }



    public double[] performPolyRegression(int degree)
    {
        List<RegressionData> regressionData = regressionMapper.getAllregression_noise();
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


    // 使用RANSAC算法拟合直线模型
    private static LineModel ransac(Instances data, int numIterations, double inlierThreshold, double confidence) {
        int numPoints = data.numInstances();
        int numInliers = (int) (numPoints * confidence);

        LineModel bestModel = null;
        int bestNumInliers = 0;

        for (int i = 0; i < numIterations; i++) {
            // 随机选择两个数据点作为模型的参数
            Instance instance1 = data.instance(new Random().nextInt(numPoints));
            Instance instance2 = data.instance(new Random().nextInt(numPoints));

            // 构建模型
            LineModel model = new LineModel(instance1, instance2);

            // 计算当前模型的内点数量
            int numCurrentInliers = 0;
            for (int j = 0; j < numPoints; j++) {
                Instance instance = data.instance(j);
                if (model.isInlier(instance, inlierThreshold)) {
                    numCurrentInliers++;
                }
            }

            // 更新最优模型
            if (numCurrentInliers > bestNumInliers) {
                bestModel = model;
                bestNumInliers = numCurrentInliers;
            }

            // 判断是否已找到足够数量的内点
            if (bestNumInliers >= numInliers) {
                break;
            }
        }

        return bestModel;
    }

    // 直线模型
    private static class LineModel {
        private final double slope;
        private final double intercept;

        public LineModel(Instance instance1, Instance instance2) {
            double x1 = instance1.value(0);
            double y1 = instance1.value(1);
            double x2 = instance2.value(0);
            double y2 = instance2.value(1);

            this.slope = (y2 - y1) / (x2 - x1);
            this.intercept = y1 - slope * x1;
        }

        public double getSlope() {
            return slope;
        }

        public double getIntercept() {
            return intercept;
        }

        public boolean isInlier(Instance instance, double threshold) {
            double x = instance.value(0);
            double y = instance.value(1);

            double residual = Math.abs(y - (slope * x + intercept));
            return residual <= threshold;
        }
    }

    public double[] performRANSAC()
    {
        double[] res = new double[2];
        List<RegressionData> regressionData = regressionMapper.getAllregression_noise();
        Instances instances = convertToInstances(regressionData);

        LineModel lineModel = ransac(instances, 100000, 0.61, 0.99);

        // 输出拟合结果
        System.out.println("拟合结果：");
        System.out.println("斜率：" + lineModel.getSlope());
        System.out.println("截距：" + lineModel.getIntercept());
        res[1] = lineModel.getSlope();
        res[0] = lineModel.getIntercept();
        return res;
    }

}
