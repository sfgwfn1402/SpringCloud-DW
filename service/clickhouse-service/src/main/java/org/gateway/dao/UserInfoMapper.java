package org.gateway.dao;

import org.apache.ibatis.annotations.Param;
import org.gateway.domain.UserInfo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserInfoMapper {
    void saveData(UserInfo userInfo);

    UserInfo selectById(@Param("id") Integer id);

    List<UserInfo> selectList();
}
