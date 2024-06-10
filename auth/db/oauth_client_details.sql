
-- ----------------------------
-- Table structure for t_oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `t_oauth_client_details`;
CREATE TABLE `t_oauth_client_details`  (
                                           `client_id` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                           `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                           `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                           `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                           `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                           `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                           `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                           `access_token_validity` int(11) NULL DEFAULT NULL,
                                           `refresh_token_validity` int(11) NULL DEFAULT NULL,
                                           `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                           `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                           PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

INSERT INTO `trade_stock`.`t_oauth_client_details`(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES ('admin', NULL, 'ISMvKXpXpadDiUoOSoAfww==', 'read_writer', 'password_refresh_token', NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `trade_stock`.`t_oauth_client_details`(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES ('app', NULL, '0qV9wdiD/SH7mVFpnfccxw==', 'server', 'password,refresh_token', NULL, NULL, NULL, NULL, NULL, 'true');
-- 新增t_oauth_client_details表， 初始化两条client信息， 名称app， 密码app； 名称admin， 密码admin。

-- 初始化t_trade_user表数据
INSERT INTO `trade_stock`.`t_trade_user`(`id`, `userNo`, `name`, `userPwd`, `phone`, `companyId`, `email`, `address`, `lastLoginIp`, `lastLoginTime`, `status`, `craeteTime`) VALUES (1, 'admin', 'admin', 'ISMvKXpXpadDiUoOSoAfww==', '123', 1, NULL, NULL, NULL, NULL, 0, NULL);
-- 在用户信息表t_trade_user里面初始化一条数据， 用户名为admin， 密码为admin。

