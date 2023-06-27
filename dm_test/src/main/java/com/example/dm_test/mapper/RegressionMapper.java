package com.example.dm_test.mapper;

import com.example.dm_test.entity.RegressionData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RegressionMapper {
    @Select("SELECT * FROM regression")
    List<RegressionData> getAllregression();
}
