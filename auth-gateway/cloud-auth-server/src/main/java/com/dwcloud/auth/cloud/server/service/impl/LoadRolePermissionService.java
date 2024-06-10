package com.dwcloud.auth.cloud.server.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.dwcloud.auth.cloud.common.model.SysConstant;
import com.dwcloud.auth.cloud.server.model.po.SysRole;
import com.dwcloud.auth.cloud.server.model.vo.SysRolePermissionVO;
import com.dwcloud.auth.cloud.server.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 公众号：码猿技术专栏
 * 在项目启动时从数据库中将url->角色对应关系加载到Redis中
 */
@Service
public class LoadRolePermissionService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private PermissionService permissionService;

    @PostConstruct
    public void init() {
        //获取所有的权限
        List<SysRolePermissionVO> list = permissionService.listRolePermission();
        list.parallelStream().peek(k -> {
            List<String> roles = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(k.getRoles())) {
                for (SysRole role : k.getRoles()) {
                    roles.add(SysConstant.ROLE_PREFIX + role.getCode());
                }
            }
            //放入Redis中
            redisTemplate.opsForHash().put(SysConstant.OAUTH_URLS, k.getUrl(), roles);
        }).collect(Collectors.toList());
    }

}
