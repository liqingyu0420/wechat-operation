
SET NAMES utf8mb4;
-- ----------------------------
-- Table structure for 用户认证
-- ----------------------------
DROP TABLE IF EXISTS `t_auth_user`;
CREATE TABLE `t_auth_user`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `user_code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户登录账号',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '密码',
  `open_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'openid',
  `nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '昵称',
  `avatar_url` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '头像',
  `parent_user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '父账号',
  `state` tinyint(1) NULL DEFAULT NULL COMMENT '0 无效，1有效',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '登录用户' ROW_FORMAT = Dynamic;
 -- 初始账号密码 admin/admin123!@#
INSERT INTO `t_auth_user`(`id`, `user_code`, `password`, `open_id`, `nick_name`, `avatar_url`, `parent_user_id`, `state`, `create_time`) VALUES ('1305345329560653826', 'admin', '9b045c31ea6aff22f11de768a37fa0e5', NULL, '管理员', 'http://pic.51yuansu.com/pic3/cover/01/69/80/595f67c2aff1e_610.jpg', NULL, 1, '2020-09-14 11:19:13');

-- ----------------------------
-- Table structure for 公众号列表
-- ----------------------------
DROP TABLE IF EXISTS `t_account`;
CREATE TABLE `t_account`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `authorizer_app_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '授权方appid',
  `authorizer_access_token` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '授权方accessToken',
  `authorizer_refresh_token` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '授权方刷新token',
  `expires_in` int(0) NULL DEFAULT NULL COMMENT 'token过期时间',
  `nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '昵称',
  `head_image` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '头像',
  `service_type_info` json NULL COMMENT '公众号类型',
  `verify_type_info` json NULL COMMENT '公众号认证类型',
  `user_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '原始 ID',
  `principal_name` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '主体名称',
  `alias` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号所设置的微信号，可能为空',
  `business_info` json NULL COMMENT '用以了解功能的开通状况（0代表未开通，1代表已开通）',
  `qrcode_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '二维码图片的 URL',
  `state` tinyint(1) NULL DEFAULT NULL COMMENT '状态',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `create_user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_account_userid`(`create_user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '公众号' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for 公众号粉丝
-- ----------------------------
DROP TABLE IF EXISTS `t_account_fans`;
CREATE TABLE `t_account_fans`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `account_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号id',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'openid',
  `nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'nickname',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '用户的性别，值为1时是男性，值为2时是女性，值为0时是未知',
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '城市',
  `province` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '省份',
  `country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '国家',
  `head_img_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'headimgurl',
  `union_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `subscribe` tinyint(1) NULL DEFAULT NULL COMMENT '1是关注',
  `subscribe_time` bigint(0) NULL DEFAULT NULL COMMENT '关注时间',
  `subscribe_scene` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '关注的渠道来源',
  `subscribe_scene_label` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '关注的渠道来源zh',
  `tag_id_list` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标签id集合tagid_list',
  `last_inactive_time` bigint(0) NULL DEFAULT NULL COMMENT '最后互动时间',
  `group_id` int(0) NULL DEFAULT NULL COMMENT '分组',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `state` tinyint(1) NULL DEFAULT NULL COMMENT '状态码',
  `create_time` bigint(0) NULL DEFAULT NULL COMMENT '创建时间 秒数时间戳',
  `tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '添加这个字段是为了方便查询',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '公众号粉丝' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for 公众号标签
-- ----------------------------
DROP TABLE IF EXISTS `t_account_tag`;
CREATE TABLE `t_account_tag`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `wx_id` int(0) NULL DEFAULT NULL COMMENT '微信返回的id',
  `account_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号id',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标签名称',
  `fans_count` int(0) NULL DEFAULT 0 COMMENT '粉丝数量 微信字段count',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '公众号标签' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for 公众号粉丝统计
-- ----------------------------
DROP TABLE IF EXISTS `t_account_stat`;
CREATE TABLE `t_account_stat`  (
  `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `account_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号id',
  `new_num` int(0) NULL DEFAULT 0 COMMENT '新增数量',
  `cancel_num` int(0) NULL DEFAULT 0 COMMENT '取关数量',
  `inactive_num` int(0) NULL DEFAULT 0 COMMENT '活跃数量',
  `total_fans_num` int(0) NULL DEFAULT 0 COMMENT '总粉丝数',
  `add_num` int(0) NULL DEFAULT 0 COMMENT '净增数量',
  `page_read_num` int(0) NULL DEFAULT 0 COMMENT '阅读数量',
  `new_rate` decimal(5, 2) NULL DEFAULT NULL COMMENT '新增比例',
  `cancel_rate` decimal(5, 2) NULL DEFAULT NULL COMMENT '取关比例',
  `inactive_rate` decimal(5, 2) NULL DEFAULT NULL COMMENT '活跃数量',
  `total_fans_rate` decimal(5, 2) NULL DEFAULT NULL COMMENT '总粉丝比例',
  `add_rate` decimal(5, 2) NULL DEFAULT NULL COMMENT '净增比例',
  `page_read_rate` decimal(5, 2) NULL DEFAULT NULL COMMENT '阅读比例',
  `seven_num` int(0) NULL DEFAULT NULL COMMENT '七天',
  `fifteen_num` int(0) NULL DEFAULT NULL COMMENT '15天',
  `stat_date` date NULL DEFAULT NULL COMMENT '统计日期',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '公众号粉丝统计' ROW_FORMAT = Dynamic;
-- ----------------------------
-- Table structure for 粉丝动作统计
-- ----------------------------
DROP TABLE IF EXISTS `t_fans_action_stat`;
CREATE TABLE `t_fans_action_stat`  (
  `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `account_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号id',
  `open_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'openid',
  `action` tinyint(1) NULL DEFAULT NULL COMMENT '粉丝动作 0-粉丝消息,1-新关注,2-关注,3-取关,4-扫描二维码,5-菜单点击',
  `create_time` bigint(0) NULL DEFAULT NULL COMMENT '创建时间 秒级',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '粉丝动作统计' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_subscribe_scene
-- ----------------------------
DROP TABLE IF EXISTS `t_subscribe_scene`;
CREATE TABLE `t_subscribe_scene`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'key',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'value',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '粉丝关注类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_subscribe_scene
-- ----------------------------
INSERT INTO `t_subscribe_scene` VALUES (1, 'ADD_SCENE_SEARCH', '公众号搜索');
INSERT INTO `t_subscribe_scene` VALUES (2, 'ADD_SCENE_ACCOUNT_MIGRATION', '公众号迁移');
INSERT INTO `t_subscribe_scene` VALUES (3, 'ADD_SCENE_PROFILE_CARD', '名片分享');
INSERT INTO `t_subscribe_scene` VALUES (4, 'ADD_SCENE_QR_CODE', '扫描二维码');
INSERT INTO `t_subscribe_scene` VALUES (5, 'ADD_SCENE_PROFILE_LINK', '图文页内名称点击');
INSERT INTO `t_subscribe_scene` VALUES (6, 'ADD_SCENE_PROFILE_ITEM', '图文页右上角菜单');
INSERT INTO `t_subscribe_scene` VALUES (7, 'ADD_SCENE_PAID', '支付后关注');
INSERT INTO `t_subscribe_scene` VALUES (8, 'ADD_SCENE_WECHAT_ADVERTISEMENT', '微信广告');
INSERT INTO `t_subscribe_scene` VALUES (9, 'ADD_SCENE_OTHERS', '其他');



-- ----------------------------
-- Table structure for t_article_stat 图文阅读统计
-- ----------------------------
DROP TABLE IF EXISTS `t_article_stat`;
CREATE TABLE `t_article_stat`  (
  `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `account_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号id',
  `stat_date` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '数据日期',
  `msg_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '微信返回msg_id',
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
  `target_user` int(0) NULL DEFAULT NULL COMMENT '送达人数',
  `int_page_read_user` int(0) NULL DEFAULT NULL COMMENT '图文页（点击群发图文卡片进入的页面）的阅读人数',
  `int_page_read_count` int(0) NULL DEFAULT NULL COMMENT '图文页的阅读次数',
  `ori_page_read_user` int(0) NULL DEFAULT NULL COMMENT '原文页（点击图文页“阅读原文”进入的页面）的阅读人数，无原文页时此处数据为0',
  `ori_page_read_count` int(0) NULL DEFAULT NULL COMMENT '原文页的阅读次数',
  `share_user` int(0) NULL DEFAULT NULL COMMENT '分享的人数',
  `share_count` int(0) NULL DEFAULT NULL COMMENT '分享的次数',
  `add_to_fav_user` int(0) NULL DEFAULT NULL COMMENT '收藏的人数',
  `add_to_fav_count` int(0) NULL DEFAULT NULL COMMENT '收藏的次数',
  `create_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '图文阅读统计' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_fans_msg 粉丝消息 暂时统计文本和图片
-- ----------------------------
DROP TABLE IF EXISTS `t_fans_msg`;
CREATE TABLE `t_fans_msg`  (
   `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
   `account_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号id',
   `to_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '开发者微信号',
   `from_user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发送方帐号opendid',
   `create_time` bigint(20) NULL DEFAULT NULL COMMENT '发送时间',
   `msg_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '消息类型 ',
   `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '内容，为文本消息时候填充',
   `media_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '媒体id,非文本时候填充',
   `pic_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图片地址',
   `msg_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '消息id',
   `read` tinyint(1) NULL DEFAULT NULL COMMENT '是否已读 0未读',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;



DROP TABLE IF EXISTS `t_account_push`;
CREATE TABLE `t_account_push`  (
  `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `account_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号id',
  `push_type` tinyint(1) NULL DEFAULT 0 COMMENT '推送方式:0-全部推送,1-按顺序推送,2-随机推送一条',
  `push_timer` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '推送限制时间 ',
  `quiet` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '安静时间',
  `push_trigger` int(0) NULL DEFAULT 111 COMMENT '第一位关注公众号，第二位 发送消息到公众号 第三位 点击菜单',
  `push_limit` int(0) NULL DEFAULT NULL COMMENT '推送限制',
  `enable` tinyint(1) NULL DEFAULT 0 COMMENT '开关',
  `nick_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名称冗余字段',
  `head_image` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'headImage 冗余字段',
  `content` json NULL COMMENT '推送内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '推送内容' ROW_FORMAT = Dynamic;



-- ----------------------------
-- Table structure for t_account_material 素材
-- ----------------------------
DROP TABLE IF EXISTS `t_account_material`;
CREATE TABLE `t_account_material`  (
   `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
   `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '描述',
   `type` tinyint(4) NULL DEFAULT NULL COMMENT '类型',
   `media_id` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '微信media id',
   `account_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号id',
   `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名称',
   `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '微信url',
   `author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
   `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL COMMENT '图文内容',
   `create_time` datetime(0) NULL DEFAULT NULL,
   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '素材 并不包含图文' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_account_articles 图文
-- ----------------------------
DROP TABLE IF EXISTS `t_account_articles`;
CREATE TABLE `t_account_articles`  (
   `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
   `account_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号id',
   `thumb_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
   `thumb_media_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '缩略图media id',
   `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'url',
   `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标题',
   `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '内容',
   `content_source_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '链接地址',
   `show_cover_pic` tinyint(1) NULL DEFAULT 1 COMMENT '是否显示封面',
   `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
   `release` tinyint(1) NULL DEFAULT NULL COMMENT '是否发布',
   `media_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '微信media id',
                                       PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '公众号图文' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_account_follow_reply 被关注回复
-- ----------------------------
DROP TABLE IF EXISTS `t_account_follow_reply`;
CREATE TABLE `t_account_follow_reply`  (
  `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `account_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号id',
  `nike_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号名称',
  `head_image` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '头像',
  `push_type` tinyint(1) NULL DEFAULT NULL COMMENT '推送方式:0-全部推送,1-按顺序推送,2-随机推送一条',
  `enable` tinyint(1) NULL DEFAULT 0 COMMENT '开关',
  `content` json NULL COMMENT '回复内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '被关注回复' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_qr_code 二维码
-- ----------------------------
DROP TABLE IF EXISTS `t_qr_code`;
CREATE TABLE `t_qr_code`  (
  `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `account_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号id',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号标题',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '类型 0 临时 1永久',
  `total_num` int(0) NULL DEFAULT NULL COMMENT '总扫码次数',
  `new_num` int(0) NULL DEFAULT NULL COMMENT '新扫码且关注',
  `follow_num` int(0) NULL DEFAULT NULL COMMENT '已关注扫码',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '二维码地址',
  `ticket` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'ticket',
  `push_type` tinyint(1) NULL DEFAULT NULL COMMENT '推送方式',
  `nike_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号名称 冗余',
  `head_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号头像 冗余',
  `expire_time` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `content` json NULL COMMENT '扫描二维码推送内容',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '二维码' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_account_menu 公众号菜单
-- ----------------------------
DROP TABLE IF EXISTS `t_account_menu`;
CREATE TABLE `t_account_menu`  (
  `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `account_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号id',
  `type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '类型',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名称',
  `key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'key',
  `parent_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '上级',
  `param1` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `param2` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;



-- ----------------------------
-- Table structure for t_menu_type 公众号菜单类型
-- ----------------------------
DROP TABLE IF EXISTS `t_menu_type`;
CREATE TABLE `t_menu_type`  (
  `id` int(0) NOT NULL AUTO_INCREMENT,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '类型',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '公众号菜单类型' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_menu_type 公众号菜单类型 内容
-- ----------------------------
INSERT INTO `t_menu_type` VALUES (1, 'click', '点击推事件用户点击click类型按钮后，微信服务器会通过消息接口推送消息类型为event的结构给开发者（参考消息接口指南），并且带上按钮中开发者填写的key值，开发者可以通过自定义的key值与用户进行交互');
INSERT INTO `t_menu_type` VALUES (2, 'view', '跳转URL用户点击view类型按钮后，微信客户端将会打开开发者在按钮中填写的网页URL，可与网页授权获取用户基本信息接口结合，获得用户基本信息');
INSERT INTO `t_menu_type` VALUES (3, 'scancode_push', '扫码推事件用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后显示扫描结果（如果是URL，将进入URL），且会将扫码的结果传给开发者，开发者可以下发消息');
INSERT INTO `t_menu_type` VALUES (4, 'scancode_waitmsg', '扫码推事件且弹出“消息接收中”提示框用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后，将扫码的结果传给开发者，同时收起扫一扫工具，然后弹出“消息接收中”提示框，随后可能会收到开发者下发的消息');
INSERT INTO `t_menu_type` VALUES (5, 'pic_sysphoto', '弹出系统拍照发图用户点击按钮后，微信客户端将调起系统相机，完成拍照操作后，会将拍摄的相片发送给开发者，并推送事件给开发者，同时收起系统相机，随后可能会收到开发者下发的消息');
INSERT INTO `t_menu_type` VALUES (6, 'pic_photo_or_album', '弹出拍照或者相册发图用户点击按钮后，微信客户端将弹出选择器供用户选择“拍照”或者“从手机相册选择”。用户选择后即走其他两种流程');
INSERT INTO `t_menu_type` VALUES (7, 'pic_weixin', '弹出微信相册发图器用户点击按钮后，微信客户端将调起微信相册，完成选择操作后，将选择的相片发送给开发者的服务器，并推送事件给开发者，同时收起相册，随后可能会收到开发者下发的消息。');
INSERT INTO `t_menu_type` VALUES (8, 'location_select', '弹出地理位置选择器用户点击按钮后，微信客户端将调起地理位置选择工具，完成选择操作后，将选择的地理位置发送给开发者的服务器，同时收起位置选择工具，随后可能会收到开发者下发的消息');
INSERT INTO `t_menu_type` VALUES (9, 'media_id', '下发消息（除文本消息）用户点击media_id类型按钮后，微信服务器会将开发者填写的永久素材id对应的素材下发给用户，永久素材类型可以是图片、音频、视频、图文消息');
INSERT INTO `t_menu_type` VALUES (10, 'view_limited', '跳转图文消息URL用户点击view_limited类型按钮后，微信客户端将打开开发者在按钮中填写的永久素材id对应的图文消息URL，永久素材类型只支持图文消息');



-- ----------------------------
-- Table structure for t_customer_msg 客服消息
-- ----------------------------
DROP TABLE IF EXISTS `t_customer_msg`;
CREATE TABLE `t_customer_msg`  (
   `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
   `account_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号id',
   `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '消息名称',
   `content` json NULL COMMENT '消息内容',
   `send_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发送时间',
   `send_type` tinyint(1) NULL DEFAULT NULL,
   `type` tinyint(1) NULL DEFAULT NULL COMMENT '类型 0 全部粉丝 1 筛选粉丝',
   `select_sex` int(11) NULL DEFAULT NULL COMMENT '筛选性别',
   `select_subscribe_time` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '筛选关注时间',
   `select_province` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '省份',
   `select_city` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '城市',
   `select_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标签',
   `success_num` int(11) NULL DEFAULT NULL COMMENT '发送成功',
   `pre_success_num` int(11) NULL DEFAULT NULL COMMENT '预计成功',
   `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态',
   `auth_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '认证权限id parentUserId',
   `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '客服消息' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_group_msg 群发消息
-- ----------------------------
DROP TABLE IF EXISTS `t_group_msg`;
CREATE TABLE `t_group_msg`  (
  `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `account_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号id',
  `content` json NULL COMMENT '发送内容',
  `send_time` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发送时间',
  `send_num` int(0) NULL DEFAULT NULL COMMENT '发送数量',
  `msg_type` tinyint(1) NULL DEFAULT NULL COMMENT ' 0 图文 1 图片 2文字 3 音频 4 视频',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT ' 0 条件筛选 1 全部',
  `repeat_send` tinyint(1) NULL DEFAULT NULL COMMENT '转发是否继续发送',
  `select_sex` tinyint(1) NULL DEFAULT NULL COMMENT '选择性别',
  `select_subscribe_time` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '订阅时间',
  `select_province` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '省',
  `select_city` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '城市',
  `select_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标签',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '电话',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `head_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '群发消息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_template_msg 模板消息
-- ----------------------------
DROP TABLE IF EXISTS `t_template_msg`;
CREATE TABLE `t_template_msg`  (
   `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
   `account_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '公众号id',
   `label` varchar(60) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '消息名称',
   `template_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '模板id',
   `template_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '模板名称',
   `template_data` json NULL COMMENT '模板数据',
   `link_type` tinyint(1) NULL DEFAULT NULL COMMENT '0 链接，1小程序',
   `link_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '链接地址或者小程序页面',
   `link_app_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '小程序appid',
   `link_app_page` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '小程序page',
   `send_time` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '发送时间',
   `send_num` int(11) NULL DEFAULT NULL COMMENT '发送数目',
   `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态码',
   `nick_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
   `head_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
   `create_time` datetime(0) NULL DEFAULT NULL,
   `type` tinyint(1) NULL DEFAULT NULL COMMENT '发送类型  0 全部粉丝 1 筛选粉丝',
   `select_sex` tinyint(1) NULL DEFAULT NULL COMMENT '选择性别',
   `select_tag` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '标签',
   `select_province` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
   `select_city` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
   `select_subscribe_time` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
   PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '模板消息' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Table structure for t_job_task 定时任务
-- ----------------------------
DROP TABLE IF EXISTS `t_job_task`;
CREATE TABLE `t_job_task`  (
  `id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'id',
  `task_label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务描述',
  `task_timer` bigint(0) NULL DEFAULT NULL COMMENT '出发时间戳',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态',
  `type` tinyint(1) NULL DEFAULT NULL COMMENT '类型 0群发1客服消息3模板消息',
  `task_key` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '任务信息id',
  `create_time` datetime(0) NULL DEFAULT NULL,
  `update_time` datetime(0) NULL DEFAULT NULL,
  `account_id` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `nike_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  `head_image` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '定时任务记录表' ROW_FORMAT = Dynamic;

