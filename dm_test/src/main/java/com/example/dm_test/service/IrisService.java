package com.example.dm_test.service;

import com.example.dm_test.entity.Iris;
import com.example.dm_test.entity.StatsData;
import com.example.dm_test.mapper.IrisMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import weka.core.AttributeStats;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;

import java.util.ArrayList;
import java.util.List;
@Service
public class IrisService {
    @Autowired
    private IrisMapper irisMapper;

    @Autowired
    private ClassificationService classificationService;

    public List<Iris> getAllIris(){
        return irisMapper.getAllIris();
    }

    public PageInfo<Iris> findAll(int pageNum, int pageSize)
    {
        System.out.println(pageNum);
        System.out.println(pageSize);
        PageHelper.startPage(pageNum, pageSize);
        List<Iris> irisList = irisMapper.getAllIris();
        return new PageInfo<Iris>(irisList);
    }

    public void updateIris(Iris iris)
    {
        irisMapper.updateIris(iris);
    }



    public List<StatsData> getStats()
    {
        List<StatsData> statsDataList = new ArrayList<>();
        Instances instance = classificationService.convertToInstances(getAllIris());
        for (int i = 0; i < 4; i++)
        {
            AttributeStats stat = instance.attributeStats(i);
            double mean = stat.numericStats.mean;
            double min = stat.numericStats.min;
            double max = stat.numericStats.max;
            double median = Utils.kthSmallestValue(instance.attributeToDoubleArray(i), 50);
            double quartile25 = Utils.kthSmallestValue(instance.attributeToDoubleArray(i), 25);
            double quartile75 = Utils.kthSmallestValue(instance.attributeToDoubleArray(i), 75);
            StatsData data1 = new StatsData(min,max,mean,median,quartile25,quartile75);
            statsDataList.add(data1);
        }
        return statsDataList;
    }

}
