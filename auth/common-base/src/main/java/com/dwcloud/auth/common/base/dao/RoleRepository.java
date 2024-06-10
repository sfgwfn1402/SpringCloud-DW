package com.dwcloud.auth.common.base.dao;

import com.dwcloud.auth.common.base.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
}
