package com.dwcloud.auth.common.base.dao;

import com.dwcloud.auth.common.base.model.RoleAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleAuthRepository extends JpaRepository<RoleAuth,Long> {

    List<RoleAuth> findByRoleIdAndStatus(Long roleId,Integer status);

    //多对多
    List<RoleAuth> findByAuthIdAndStatus(Long authId,Integer status);
}
