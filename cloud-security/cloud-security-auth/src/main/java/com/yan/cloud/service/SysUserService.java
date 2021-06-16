package com.yan.cloud.service;

import com.yan.cloud.pojo.SysUser;

public interface SysUserService {
    /**
     * 根据用户名查询用户
     *
     * @param userName
     * @return
     */
    SysUser selectByName(String userName);
}
