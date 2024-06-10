//package com.dwcloud.auth.common.service;
//
//import com.dwcloud.auth.common.dao.TradeUserRepository;
//import com.dwcloud.auth.entity.OAuthTradeUser;
//import com.dwcloud.auth.entity.TradeUser;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.Cache;
//import org.springframework.cache.CacheManager;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
///**
// * 用户信息服务接口
// * <p>
// * 这是Spring Security 提供的用户信息接口， 采用OAUTH的密码模式
// * ，需要实现该接口的loadUserByUsername方法，为提升性能， 这里我们加入了Spring Cache缓存处理。
// */
//@Service("authStockUserDetailService")
//public class AuthStockUserDetailServiceImpl implements UserDetailsService {
//
//    @Autowired
//    private TradeUserRepository tradeUserRepository;
//
//    @Autowired
//    private CacheManager cacheManager;
//
//    @Override
//    public UserDetails loadUserByUsername(String userNo) throws UsernameNotFoundException {
//
//        // 查询缓存
////        Cache cache = cacheManager.getCache(GlobalConstants.OAUTH_KEY_STOCK_USER_DETAILS);
////        if (cache != null && cache.get(userNo) != null) {
////            return (UserDetails) cache.get(userNo).get();
////        }
//        // 缓存未找到， 查询数据库
//        TradeUser tradeUser = tradeUserRepository.findByUserNo(userNo);
//
//        if (null == tradeUser) {
//            throw new UsernameNotFoundException(userNo + " not valid !");
//        }
//        // 封装成OAUTH鉴权的用户对象
//        UserDetails userDetails = new OAuthTradeUser(tradeUser);
//        // 将用户信息放入缓存
////        cache.put(userNo, userDetails);
//
//        return userDetails;
//    }
//}
//
//