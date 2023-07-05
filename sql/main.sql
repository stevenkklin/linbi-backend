
-- 创建库
create database if not exists linbi;

-- 切换库
use linbi;

CREATE TABLE `user` (
                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                        `userAccount` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '账号',
                        `userPassword` varchar(512) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码',
                        `userName` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户昵称',
                        `userAvatar` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户头像',
                        `userRole` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'user' COMMENT '用户角色：user/admin/ban',
                        `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
                        PRIMARY KEY (`id`),
                        KEY `idx_userAccount` (`userAccount`)
) ENGINE=InnoDB AUTO_INCREMENT=1675848721191993347 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户';




CREATE TABLE `chart` (
                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
                         `goal` text COLLATE utf8mb4_unicode_ci COMMENT '分析目标',
                         `chartData` text COLLATE utf8mb4_unicode_ci COMMENT '图表数据',
                         `charType` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '图表类型',
                         `genChart` text COLLATE utf8mb4_unicode_ci COMMENT '生成的图表数据',
                         `genResult` text COLLATE utf8mb4_unicode_ci COMMENT '生成的分析结论',
                         `createTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `updateTime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `isDelete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
                         `userId` bigint(20) DEFAULT NULL COMMENT '创建用户id',
                         `name` varchar(128) null comment '图表名称',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1675732938877083650 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='图标信息表'

