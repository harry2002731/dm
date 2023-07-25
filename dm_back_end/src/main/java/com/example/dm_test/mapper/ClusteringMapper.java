package com.example.dm_test.mapper;

import com.example.dm_test.entity.RegressionData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ClusteringMapper {
    @Select("SELECT * FROM X_2moon")
    List<RegressionData> get2moonData();

    @Select("SELECT * FROM X_3clusters")
    List<RegressionData> get3clusterData();

}
