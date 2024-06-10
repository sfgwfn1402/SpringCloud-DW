package com.dwcloud.auth.cloud.server.dao;

import com.dwcloud.auth.cloud.server.model.po.SysRolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SysRolePermissionRepository  extends JpaRepository<SysRolePermission,Long> {
    List<SysRolePermission> findByPermissionId(Long permissionId);
}
