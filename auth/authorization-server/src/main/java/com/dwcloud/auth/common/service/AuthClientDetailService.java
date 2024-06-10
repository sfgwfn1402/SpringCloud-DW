//package com.dwcloud.auth.common.service;
//
//import org.springframework.security.oauth2.provider.ClientDetails;
//import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
//
//import javax.persistence.Cacheable;
//import javax.sql.DataSource;
//
///**
// * 客户端信息服务接口
// * 这是OAUTH内置的客户端信息， 重新它是为了实现缓存， 减少数据库查询。
// * 对应的表为t_oauth_client_details。因为走的是redis缓存来处理鉴权，
// * 其他OAUTH的内置表可以不用加入。
// */
//public class AuthClientDetailService extends JdbcClientDetailsService {
//
//    public AuthClientDetailService(DataSource dataSource) {
//        super(dataSource);
//    }
//
//    /**
//     * 重写原生方法支持redis缓存
//     *
//     * @param clientId
//     * @return
//     */
//    @Override
//    @Cacheable(value = GlobalConstants.OAUTH_KEY_CLIENT_DETAILS, key = "#clientId", unless = "#result == null")
//    public ClientDetails loadClientByClientId(String clientId) {
//        return super.loadClientByClientId(clientId);
//    }
//
//}
//