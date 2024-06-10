package com.dwcloud.auth.common.base.service;


import com.dwcloud.auth.common.base.model.SecurityUser;

public interface LoginService {
    /**
     * 根据用户名查找
     */
    SecurityUser loadByUsername(String username);
}
