package com.dwcloud.auth.cloud.server.dao;

import com.dwcloud.auth.cloud.server.model.po.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysUserRepository extends JpaRepository<SysUser,Long> {
    SysUser findByUsernameAndStatus(String username,Integer status);

    SysUser findByMobileAndStatus(String mobile,Integer status);

}
