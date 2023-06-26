package com.example.dm_test.service;

import com.example.dm_test.entity.Iris;
import com.example.dm_test.mapper.IrisMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
@Service
public class IrisService {
    @Autowired
    private IrisMapper irisMapper;

    public List<Iris> getAllIris(){
        return irisMapper.getAllIris();
    }

    public PageInfo<Iris> findAll(int pageNum, int pageSize)
    {
        PageHelper.startPage(pageNum, pageSize);
        List<Iris> irisList = irisMapper.getAllIris();
        return new PageInfo<>(irisList);
    }
}
