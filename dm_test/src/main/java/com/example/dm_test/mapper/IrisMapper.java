package com.example.dm_test.mapper;

import com.example.dm_test.entity.Iris;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Mapper
public interface IrisMapper {
    @Select("SELECT * FROM iris")
    List<Iris> getAllIris();

}
