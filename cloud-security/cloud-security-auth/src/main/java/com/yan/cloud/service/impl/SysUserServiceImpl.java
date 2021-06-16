package com.yan.cloud.service.impl;

import com.yan.cloud.mapper.SysUserMapper;
import com.yan.cloud.pojo.SysUser;
import com.yan.cloud.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Override
    public SysUser selectByName(String userName) {
        return sysUserMapper.selectByName(userName);
    }
}
