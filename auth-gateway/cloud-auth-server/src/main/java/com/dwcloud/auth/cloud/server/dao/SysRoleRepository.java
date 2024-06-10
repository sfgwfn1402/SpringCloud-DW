package com.dwcloud.auth.cloud.server.dao;

import com.dwcloud.auth.cloud.server.model.po.SysRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SysRoleRepository  extends JpaRepository<SysRole,Long> {
}
