package com.yan.cloud.service.impl;

import com.yan.cloud.dao.TDictDAO;
import com.yan.cloud.pojo.TDict;
import com.yan.cloud.service.TDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TDictServiceImpl implements TDictService {

    @Autowired
    private TDictDAO dictDAO;

    @Override
    public Integer insertDict(TDict dict) {
        return dictDAO.insertSelective(dict);
    }
}
