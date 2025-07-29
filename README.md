
# 模块说明
这个项目是由前端发送到网关，由网关在寻找需要的功能模块。以下模块使用到的配置类都在自己模块里，这里我没有统一的给他们归为一个模块

# 1. vue-drawprize (前端)
使用 Vue 3 + Vite + Pinia + Element Plus，向用户展示抽奖界面、中奖记录展示、用户登录注册、奖品展示(需要登录成功后才能显示)等模块

# 2. lottery-service (抽奖服务)
实现了抽奖逻辑处理、中奖记录管理。使用openfeign分别调用peize-service和user-service来实现抽奖功能。抽奖成功后，并通过rabbitMQ给用户发送短信(这里由于没有特别的场景应用，没有接收消息的消费者，只有发送者。消费者可以在后端模拟一个或者在前端通过监听来实现)。

# 3. prize-service (奖品服务)
实现了奖品管理(查询所有奖品、分页查询奖品、根据id查询奖品、根据名字查询奖品、添加奖品、修改奖品、删除奖品)、库存控制(主要是扣减库存)。sql文件在代码里"drawprize.sql"文件，并且在最后我也给出SQL文件。

# 4. user-service (用户服务)
用户认证模块，用户的登录和注册，使用jwt实现

# 5. sss-gateway (API网关)
网关模块，统一效验权限(使用token进行身份验证获取用户id，使用用户id获取用户信息)。并且在网管模块还需要配置nacos、mq、jwt等的配置类

# sql文件
/*
 Navicat Premium Dump SQL

 Source Server         : drawprize
 Source Server Type    : MySQL
 Source Server Version : 90300 (9.3.0)
 Source Host           : 192.168.118.129:3306
 Source Schema         : drawprize

 Target Server Type    : MySQL
 Target Server Version : 90300 (9.3.0)
 File Encoding         : 65001

 Date: 29/07/2025 11:16:56
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for prize
-- ----------------------------
DROP TABLE IF EXISTS `prize`;
CREATE TABLE `prize`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '奖品名称',
  `total` int NOT NULL COMMENT '奖品总数',
  `remaining` int NOT NULL COMMENT '剩余数量',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_enabled`(`enabled` ASC) USING BTREE,
  INDEX `idx_remaining`(`remaining` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of prize
-- ----------------------------
INSERT INTO `prize` VALUES (1, '一等奖：iPhone 16 Pro', 5, 5, 1, '2025-05-26 10:02:45', '2025-05-30 09:39:35');
INSERT INTO `prize` VALUES (2, '二等奖：AirPods Pro', 10, 10, 1, '2025-05-26 10:02:45', '2025-05-26 10:02:45');
INSERT INTO `prize` VALUES (3, '三等奖：100元京东卡', 50, 50, 1, '2025-05-26 10:02:45', '2025-06-03 14:36:02');
INSERT INTO `prize` VALUES (4, '四等奖：10元优惠券', 200, 186, 1, '2025-05-26 10:02:45', '2025-07-29 09:56:05');
INSERT INTO `prize` VALUES (5, '五等奖：谢谢参与', 1000, 956, 1, '2025-05-26 10:02:45', '2025-07-29 09:13:32');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '密码，加密存储',
  `phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '注册手机号',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL,
  `status` int NULL DEFAULT 1 COMMENT '使用状态（1正常 2冻结）',
  `balance` int NULL DEFAULT NULL COMMENT '账户余额',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '用户表' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'Jack', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '13900112224', '2017-08-19 20:50:21', '2017-08-19 20:50:21', 1, 948800);
INSERT INTO `user` VALUES (2, 'Rose', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '13900112223', '2017-08-19 21:00:23', '2017-08-19 21:00:23', 1, 1000000);
INSERT INTO `user` VALUES (3, 'Hope', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '13900112222', '2017-08-19 22:37:44', '2017-08-19 22:37:44', 1, 1000000);
INSERT INTO `user` VALUES (4, 'Thomas', '$2a$10$6ptTq3V9XfaJmFYwYT2W9ud377BUkEWk.whf.iQ.0sX5F.L497rAC', '17701265258', '2017-08-19 23:44:45', '2017-08-19 23:44:45', 1, 1000000);
INSERT INTO `user` VALUES (10, 'p', '$2a$10$eLIwBPJXrfs5sRJCP1x8N.LZh3K92wNiR53JfN4hbs6p7vRCoHWoS', NULL, NULL, NULL, 1, NULL);
INSERT INTO `user` VALUES (11, 'bob', '$2a$10$tQ162YU/t7eYXDbkFfivguAFgQ99ZnP8Du2.kMdv.QEhCcbqb3hpy', NULL, NULL, NULL, 1, NULL);
INSERT INTO `user` VALUES (12, 'songsong', '$2a$10$kiIcIPn1Fuk09yNeTqCKEO.CNTOspiwDMhYg0DgLFwfuyr.gu8stO', NULL, NULL, NULL, 1, NULL);
INSERT INTO `user` VALUES (13, 'taotao', '$2a$10$KKX2OlWULEr4c6DDQnbf7.k1t4xh3ru5JfEmONJ6E4np1WLk5JCi6', NULL, NULL, NULL, 1, NULL);
INSERT INTO `user` VALUES (14, 'qwer', '$2a$10$bJBRy6k9ewl2U8iDyN.2o.r.v1Wcb.LZjzPeDzvuXaE1xoQIo9bDS', NULL, NULL, NULL, 1, NULL);
INSERT INTO `user` VALUES (15, '吕迪', '$2a$10$bNs06fvUcHFrDu3s8WR/I.SJI78EfZm.nLmzc3Kb0.ipZFf0LkGt2', NULL, NULL, NULL, 1, NULL);
INSERT INTO `user` VALUES (16, 'ttt', '$2a$10$kmFB4Z2KxR6X4tYiK2.y/.rmHuxaW.wOj.QN8ObuCSuoCx1UMWxiu', NULL, NULL, NULL, 1, NULL);
INSERT INTO `user` VALUES (17, 'ooo', '$2a$10$IFCFhBnfsdUGOE9tElhGK.vq7sOJE2PvdxC.8MdR5pVAyXcNdbigS', NULL, NULL, NULL, 1, NULL);
INSERT INTO `user` VALUES (18, 'oopp', '$2a$10$fgBJkt3oOIRDhN2.VuLTPe4fAnfJC2DYDJmOAbaXE3cFYRYix7W/G', NULL, NULL, NULL, 1, NULL);
INSERT INTO `user` VALUES (19, 'uuuu', '$2a$10$9NGnXi4KXo1HDBwuTZiYjOzedRDtRx5YjmCMshDb66TGUM9b/Caly', NULL, NULL, NULL, 1, NULL);
INSERT INTO `user` VALUES (20, 'aaaa', '$2a$10$zBPvcCLygdsSsKh.xRCekuxsG7XVjILb.aRv7m5pkaPGxBp9AeCsi', NULL, NULL, NULL, 1, NULL);
INSERT INTO `user` VALUES (21, 'oppo', '$2a$10$I8BXdQHGBAKv72AmA13HWumB7FyQbopl01xER2iM9C2RrkiPYM1Ii', NULL, NULL, NULL, 1, NULL);
INSERT INTO `user` VALUES (22, '松松', '$2a$10$3ZAIwpZvGJDk3MfT/kQr2udZNUrZDB7UfPOt5Q1xhDmxAUCnKDwuW', NULL, NULL, NULL, 1, NULL);
INSERT INTO `user` VALUES (23, 'tt', '$2a$10$a1.n2NnmovTpzsbNPQ2kn.Az/6j.yIUHvRrSxbJGW1EiOTtTvy27.', NULL, NULL, NULL, 1, NULL);
INSERT INTO `user` VALUES (24, '松松松松', '$2a$10$TxkPDpDV5evZtawc5Gkeneauz9ev5zqTzMZl3tmHDcdVvD/WUZu1i', NULL, NULL, NULL, 1, NULL);
INSERT INTO `user` VALUES (25, '涛涛涛涛', '$2a$10$WOEHzvB12Tlw7PhFHB90VuRR0T2N9IjapPa7VZjdkdB9EianWmd8C', NULL, NULL, NULL, 1, NULL);

-- ----------------------------
-- Table structure for user_lottery_record
-- ----------------------------
DROP TABLE IF EXISTS `user_lottery_record`;
CREATE TABLE `user_lottery_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '用户ID',
  `prize_id` bigint NULL DEFAULT NULL COMMENT '奖品ID',
  `result` tinyint(1) NOT NULL COMMENT '是否中奖',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '抽奖时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `prize_id`(`prize_id` ASC) USING BTREE,
  CONSTRAINT `user_lottery_record_ibfk_1` FOREIGN KEY (`prize_id`) REFERENCES `prize` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 81 CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_lottery_record
-- ----------------------------
INSERT INTO `user_lottery_record` VALUES (22, '1', 5, 1, '2025-05-28 15:32:27');
INSERT INTO `user_lottery_record` VALUES (23, '10', 5, 1, '2025-05-28 15:46:54');
INSERT INTO `user_lottery_record` VALUES (24, '11', 5, 1, '2025-05-28 15:47:29');
INSERT INTO `user_lottery_record` VALUES (75, '15', 5, 1, '2025-06-03 15:10:36');
INSERT INTO `user_lottery_record` VALUES (76, '20', 5, 1, '2025-06-28 22:07:21');
INSERT INTO `user_lottery_record` VALUES (77, '22', 5, 1, '2025-06-28 22:11:22');
INSERT INTO `user_lottery_record` VALUES (78, '23', 5, 1, '2025-07-15 10:48:08');
INSERT INTO `user_lottery_record` VALUES (79, '24', 5, 1, '2025-07-29 09:13:33');
INSERT INTO `user_lottery_record` VALUES (80, '25', 4, 1, '2025-07-29 09:56:06');

SET FOREIGN_KEY_CHECKS = 1;

