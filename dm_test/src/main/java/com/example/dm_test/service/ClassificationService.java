package com.example.dm_test.service;

import com.example.dm_test.entity.Iris;
import com.example.dm_test.mapper.IrisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.reflect.generics.tree.Tree;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.converters.ConverterUtils.DataSource;
import weka.gui.treevisualizer.PlaceNode2;
import weka.gui.treevisualizer.TreeVisualizer;

import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClassificationService {
    @Autowired
    private IrisMapper irisMapper;
    public String performClassification(float attr1, float attr2, float attr3, float attr4)
    {
        List<Iris> irisList = irisMapper.getAllIris();

        Instances instances = convertToInstances(irisList);
//        System.out.println(instances);

        // classification begin
        J48 classifier = new J48();
        try {
            classifier.buildClassifier(instances);
            // 创建一个新的Iris对象，用于分类
//            Iris newIris = new Iris(3.7f, 5.4f, 0.2f, 1.5f);
            Iris newIris = new Iris(attr1, attr2, attr3, attr4);
            // 将新的Iris对象转换为Instance
            double[] values = new double[instances.numAttributes()];
            values[0] = newIris.getSepL();
            values[1] = newIris.getSepW();
            values[2] = newIris.getPetL();
            values[3] = newIris.getPetW();
            Instance newInstance = new DenseInstance(1.0, values);
            newInstance.setDataset(instances);

            // 进行分类
            double classValue = classifier.classifyInstance(newInstance);

            // 将数值转换为类别标签
            String predictedClassLabel = instances.classAttribute().value((int) classValue);

            System.out.println("Predicted class label: " + predictedClassLabel);
            return predictedClassLabel;
        }catch (Exception e)
        {
            e.printStackTrace();
            String res_fail = "Fail";
            return res_fail;
        }

//        // visualization
//        try {
//            TreeVisualizer visualizer = new TreeVisualizer(null, classifier.graph(), new PlaceNode2());
//            visualizer.setSize(800, 600);
//
//            // 保存为图片
//            JFrame frame = new JFrame("Decision Tree");
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.setSize(800, 600);
//            frame.getContentPane().add(visualizer);
//            frame.setVisible(true);
//            visualizer.fitToScreen();
//
//            savePic(frame);
//        }catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//
    }

    public void savePic(JFrame jf)
    {
        //得到窗口内容面板
        Container content=jf.getContentPane();
        //创建缓冲图片对象
        BufferedImage img=new BufferedImage(
                jf.getWidth(),jf.getHeight(),BufferedImage.TYPE_INT_RGB);

        //得到图形对象
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.WHITE);
        //将窗口内容面板输出到图形对象中
        content.printAll(g2d);
        //保存为图片
        File f=new File("/home/benny/Desktop/saveScreen.jpg");
        try {
            ImageIO.write(img, "jpg", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //释放图形对象
        g2d.dispose();


    }

    private Instances convertToInstances(List<Iris> irisList) {
        // 创建属性列表
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("sepalLength"));
        attributes.add(new Attribute("sepalWidth"));
        attributes.add(new Attribute("petalLength"));
        attributes.add(new Attribute("petalWidth"));

        // 创建类别属性，并指定可能的取值
        ArrayList<String> classValues = new ArrayList<>();
        classValues.add("setosa");
        classValues.add("versicolor");
        classValues.add("virginica");
        Attribute classAttribute = new Attribute("class", classValues);
        attributes.add(classAttribute);

        // 创建Instances对象
        Instances instances = new Instances("iris", attributes, irisList.size());
        instances.setClassIndex(attributes.size() - 1);

        // 添加每个Iris对象作为Instance
        for (Iris iris : irisList) {
            double[] values = new double[attributes.size()];
            values[0] = iris.getSepL();
            values[1] = iris.getSepW();
            values[2] = iris.getPetL();
            values[3] = iris.getPetW();
            values[4] = classAttribute.indexOfValue(iris.getSpecies());

            Instance instance = new DenseInstance(1.0, values);
            instances.add(instance);
            System.out.println(instance);
        }

        return instances;
    }
}
