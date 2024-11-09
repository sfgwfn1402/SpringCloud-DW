package org.gateway.service;

import org.gateway.dao.UserInfoMapper;
import org.gateway.domain.UserInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserInfoService {
 
    @Resource
    private UserInfoMapper userInfoMapper ;
 
    public void saveData(UserInfo userInfo) {
        userInfoMapper.saveData(userInfo);
    }
 
    public UserInfo selectById(Integer id) {
        return userInfoMapper.selectById(id);
    }
 
    public List<UserInfo> selectList() {
        return userInfoMapper.selectList();
    }
 
}

