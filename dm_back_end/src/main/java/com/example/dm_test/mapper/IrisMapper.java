package com.example.dm_test.mapper;

import com.example.dm_test.entity.Iris;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface IrisMapper {
    @Select("SELECT * FROM iris")
    List<Iris> getAllIris();

    @Update("UPDATE iris SET SepL = #{iris.SepL}, SepW = #{iris.SepW}, PetL = #{iris.PetL}, PetW = #{iris.PetW} WHERE id = #{iris.id}")
    void updateIris(@Param("iris") Iris iris);

}
