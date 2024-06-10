//package com.dwcloud.auth.common.web;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/trade")
//public class TradeUserController {
//
//    @Autowired
//    private UserDetailsService authStockUserDetailService;
//
//    /**
//     * 获取用户信息
//     *
//     * @param username
//     * @return
//     */
//    @RequestMapping("/user")
//    @ResponseBody
//    public UserDetails getUser(@RequestParam("username") String username) {
//
//        UserDetails userDetails = authStockUserDetailService.loadUserByUsername(username);
//        return userDetails;
//    }
//}
//