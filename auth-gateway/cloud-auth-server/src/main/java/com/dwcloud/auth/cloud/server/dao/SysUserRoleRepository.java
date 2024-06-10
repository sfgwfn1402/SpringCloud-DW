package com.dwcloud.auth.cloud.server.dao;

import com.dwcloud.auth.cloud.server.model.po.SysUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SysUserRoleRepository extends JpaRepository<SysUserRole,Long> {
    List<SysUserRole> findByUserId(Long userId);
}
