package com.example.dm_test.mapper;

import com.example.dm_test.entity.AprioriData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AprioriMapper {
    @Select("SELECT * FROM apriori")
    List<AprioriData> getAllApriori();
}
