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

import java.math.BigDecimal;
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
            BigDecimal mean_fil = new BigDecimal(mean);
            BigDecimal min_fil = new BigDecimal(min);
            BigDecimal max_fil = new BigDecimal(max);
            BigDecimal median_fil = new BigDecimal(median);
            BigDecimal qu25 = new BigDecimal(quartile25);
            BigDecimal qu75 = new BigDecimal(quartile75);
            double mean_final = mean_fil.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
            double min_final = min_fil.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
            double max_final = max_fil.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
            double median_final = median_fil.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
            double qu25_final = qu25.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
            double qu75_final = qu75.setScale(3,BigDecimal.ROUND_HALF_UP).doubleValue();
            StatsData data1 = new StatsData(min_final,max_final,mean_final,median_final,qu25_final,qu75_final);
            statsDataList.add(data1);
        }
        return statsDataList;
    }

}
