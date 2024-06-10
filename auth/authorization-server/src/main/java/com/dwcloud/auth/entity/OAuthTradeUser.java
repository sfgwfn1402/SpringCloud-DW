//package com.dwcloud.auth.entity;
//
//import org.springframework.security.core.userdetails.User;
//
//import java.util.Collections;
//
///**
// * 用户封装信息
// */
//public class OAuthTradeUser extends User {
//
//    private static final long serialVersionUUID = -1L;
//
//    /**
//     * 业务用户信息
//     */
//    private TradeUser tradeUser;
//
//    public OAuthTradeUser(TradeUser tradeUser) {
//        // OAUTH2认证用户信息构造处理
//        super(tradeUser.getUserNo(), tradeUser.getUserPwd(), (tradeUser.getStatus() == 0 ? true : false)
//                , true, true, (tradeUser.getStatus() == 0 ? true : false), Collections.emptyList());
//        this.tradeUser = tradeUser;
//    }
//
//}
//