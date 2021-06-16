package com.yan.cloud.service;

import com.yan.cloud.pojo.SysPermission;

import java.util.List;

public interface SysPermissionService {
    /**
     * 查询用户的权限列表
     *
     * @param userId
     * @return
     */
    List<SysPermission> selectListByUser(Integer userId);
}
