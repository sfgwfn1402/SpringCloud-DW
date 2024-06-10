package com.dwcloud.auth.common.base.dao;

import com.dwcloud.auth.common.base.model.AuthInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRepository extends JpaRepository<AuthInfo,Long> {
    AuthInfo findByUrlAndStatus(String url, Integer status);
}
