CREATE DATABASE IF NOT EXISTS cases default charset utf8mb4 COLLATE utf8mb4_general_ci;
USE cases;

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for execute_case
-- ----------------------------
DROP TABLE IF EXISTS `execute_case`;
CREATE TABLE `execute_case` (
  `id` int(11) NOT NULL,
  `brief` varchar(100) DEFAULT NULL COMMENT '简介',
  `file_name` varchar(20) DEFAULT NULL COMMENT '文件名',
  `file_path` varchar(20) DEFAULT NULL COMMENT '文件路径',
  `title` varchar(100) DEFAULT NULL COMMENT '文章标题',
  `detail` text COMMENT '详情',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for guide_case
-- ----------------------------
DROP TABLE IF EXISTS `guide_case`;
CREATE TABLE `guide_case` (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '标题',
  `court` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发布法院',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新日期',
  `detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '详情',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- ----------------------------
-- Table structure for reference_case
-- ----------------------------
DROP TABLE IF EXISTS `reference_case`;
CREATE TABLE `reference_case` (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `title` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '标题',
  `court` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '发布法院',
  `update_time` varchar(20) DEFAULT NULL COMMENT '更新日期',
  `detail` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '详情',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;