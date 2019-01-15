package com.hxlc.backstageapp.service.impl;

import com.hxlc.backstageapp.mapper.DistributorMapper;
import com.hxlc.backstageapp.pojo.DistributorInfo;
import com.hxlc.backstageapp.service.DistributorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistributorServiceImpl implements DistributorService{
    @Autowired
    private DistributorMapper distributorMapper;
    @Override
    public String findUrlById(Integer disId) {
        return distributorMapper.findUrlById(disId);
    }
}
