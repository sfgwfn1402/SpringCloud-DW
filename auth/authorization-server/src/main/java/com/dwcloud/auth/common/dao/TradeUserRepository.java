//package com.dwcloud.auth.common.dao;
//
//import com.dwcloud.auth.entity.TradeUser;
//import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
//import org.springframework.data.repository.PagingAndSortingRepository;
//import org.springframework.stereotype.Repository;
//
///**
// * 用户信息数据层接口
// * 实现一个根据用户账号获取用户对象接口， 用于用户登陆处理。
// * 注意路径与实体TradeUser的路径要在上面讲的JPA扫描路径范围之内。
// */
//@Repository("tradeUserRepository")
//public interface TradeUserRepository extends PagingAndSortingRepository<TradeUser, String>, JpaSpecificationExecutor<TradeUser> {
//
//    /**
//     * 根据用户账号获取用户对象
//     *
//     * @param userNo
//     * @return
//     */
//    TradeUser findByUserNo(String userNo);
//}