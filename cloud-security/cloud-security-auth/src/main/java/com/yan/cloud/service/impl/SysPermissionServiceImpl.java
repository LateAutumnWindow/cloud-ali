package com.yan.cloud.service.impl;

import com.yan.cloud.mapper.SysPermissionMapper;
import com.yan.cloud.pojo.SysPermission;
import com.yan.cloud.service.SysPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysPermissionServiceImpl implements SysPermissionService {
    @Autowired
    private SysPermissionMapper sysPermissionMapper;

    @Override
    public List<SysPermission> selectListByUser(Integer userId) {
        return sysPermissionMapper.selectPermissionByUser(userId);
    }
}
