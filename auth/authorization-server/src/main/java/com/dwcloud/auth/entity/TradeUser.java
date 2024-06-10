//package com.dwcloud.auth.entity;
//
//import lombok.Data;
//
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.Table;
//import java.util.Date;
//
//@Data
//@Entity
//@Table(name = "t_trade_user")
//public class TradeUser {
//
//    @Id
//    private Long id;
//
//    /**
//     * 用户编号
//     */
//    private String userNo;
//
//    /**
//     * 用户名称
//     */
//    private String name;
//
//    /**
//     * 用户密码
//     */
//    private String userPwd;
//
//    /**
//     * 电话号码
//     */
//    private String phone;
//
//    /**
//     * 公司ID
//     */
//    private Long companyId;
//
//    /**
//     * 邮箱
//     */
//    private String email;
//
//    /**
//     * 地址
//     */
//    private String address;
//
//    /**
//     * 最近一次用户登陆IP
//     */
//    private String lastLoginIp;
//
//    /**
//     * 最近一次登陆时间
//     */
//    private Date lastLoginTime;
//
//    /**
//     * 状态(0:有效， 1：锁定， 2：禁用）
//     */
//    private int status;
//
//    /**
//     * 创建时间
//     */
//    private Date craeteTime;
//
//
//}