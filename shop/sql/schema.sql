SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- 用户
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id`            BIGINT       NOT NULL AUTO_INCREMENT,
  `username`      VARCHAR(64)  NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `salt`          VARCHAR(64)  NOT NULL,
  `role`          VARCHAR(16)  NOT NULL DEFAULT 'customer',
  `phone`         VARCHAR(32)  DEFAULT NULL,
  `address`       VARCHAR(255) DEFAULT NULL,
  `created_at`    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 分类
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories` (
  `id`         BIGINT       NOT NULL AUTO_INCREMENT,
  `name`       VARCHAR(64)  NOT NULL,
  `icon`       VARCHAR(16)  DEFAULT NULL,
  `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 商品
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `id`          BIGINT        NOT NULL AUTO_INCREMENT,
  `name`        VARCHAR(128)  NOT NULL,
  `description` TEXT,
  `category_id` BIGINT        DEFAULT NULL,
  `image_url`   VARCHAR(255)  DEFAULT NULL,
  `status`      TINYINT       NOT NULL DEFAULT 1 COMMENT '1上架 0下架',
  `created_at`  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 商品规格(如"颜色")
-- ----------------------------
DROP TABLE IF EXISTS `product_specs`;
CREATE TABLE `product_specs` (
  `id`         BIGINT      NOT NULL AUTO_INCREMENT,
  `product_id` BIGINT      NOT NULL,
  `name`       VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 规格选项(如"黑色")
-- ----------------------------
DROP TABLE IF EXISTS `product_spec_options`;
CREATE TABLE `product_spec_options` (
  `id`         BIGINT      NOT NULL AUTO_INCREMENT,
  `spec_id`    BIGINT      NOT NULL,
  `value`      VARCHAR(64) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_spec` (`spec_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- SKU
-- ----------------------------
DROP TABLE IF EXISTS `skus`;
CREATE TABLE `skus` (
  `id`         BIGINT         NOT NULL AUTO_INCREMENT,
  `product_id` BIGINT         NOT NULL,
  `sku_code`   VARCHAR(64)    DEFAULT NULL,
  `price`      BIGINT         NOT NULL COMMENT '分',
  `stock`      INT            NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- SKU 对应的规格选项(每个规格选了哪个选项)
-- ----------------------------
DROP TABLE IF EXISTS `sku_values`;
CREATE TABLE `sku_values` (
  `id`        BIGINT NOT NULL AUTO_INCREMENT,
  `sku_id`    BIGINT NOT NULL,
  `spec_id`   BIGINT NOT NULL,
  `option_id` BIGINT NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_sku` (`sku_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 购物车(按 SKU)
-- ----------------------------
DROP TABLE IF EXISTS `cart_items`;
CREATE TABLE `cart_items` (
  `id`         BIGINT  NOT NULL AUTO_INCREMENT,
  `user_id`    BIGINT  NOT NULL,
  `sku_id`     BIGINT  NOT NULL,
  `quantity`   INT     NOT NULL DEFAULT 1,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 订单
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id`         BIGINT         NOT NULL AUTO_INCREMENT,
  `order_no`   VARCHAR(64)    NOT NULL,
  `user_id`    BIGINT         NOT NULL,
  `total`      BIGINT         NOT NULL COMMENT '分,已含折扣',
  `discount`   BIGINT         NOT NULL DEFAULT 0 COMMENT '分',
  `status`     VARCHAR(16)    NOT NULL DEFAULT 'pending' COMMENT 'pending/paid/shipped/done/cancelled',
  `receiver`   VARCHAR(64)    DEFAULT NULL,
  `phone`      VARCHAR(32)    DEFAULT NULL,
  `address`    VARCHAR(255)   DEFAULT NULL,
  `coupon_id`  BIGINT         DEFAULT NULL,
  `created_at` DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_orderno` (`order_no`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 订单明细
-- ----------------------------
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
  `id`           BIGINT      NOT NULL AUTO_INCREMENT,
  `order_id`     BIGINT      NOT NULL,
  `sku_id`       BIGINT      DEFAULT NULL,
  `product_name` VARCHAR(128) NOT NULL,
  `sku_specs`    VARCHAR(255) DEFAULT NULL,
  `price`        BIGINT      NOT NULL COMMENT '分',
  `quantity`     INT         NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 模拟支付记录
-- ----------------------------
DROP TABLE IF EXISTS `payments`;
CREATE TABLE `payments` (
  `id`       BIGINT      NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT      NOT NULL,
  `method`   VARCHAR(32) DEFAULT 'mock',
  `amount`   BIGINT      NOT NULL,
  `status`   VARCHAR(16) NOT NULL DEFAULT 'success',
  `paid_at`  DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_order` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 商品评价
-- ----------------------------
DROP TABLE IF EXISTS `reviews`;
CREATE TABLE `reviews` (
  `id`         BIGINT       NOT NULL AUTO_INCREMENT,
  `product_id` BIGINT       NOT NULL,
  `user_id`    BIGINT       NOT NULL,
  `rating`     TINYINT      NOT NULL,
  `content`    TEXT,
  `created_at` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_product` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 优惠券
-- ----------------------------
DROP TABLE IF EXISTS `coupons`;
CREATE TABLE `coupons` (
  `id`         BIGINT      NOT NULL AUTO_INCREMENT,
  `code`       VARCHAR(64) NOT NULL,
  `name`       VARCHAR(128) NOT NULL,
  `type`       VARCHAR(16) NOT NULL COMMENT 'fixed=满减 percent=折扣',
  `value`      INT         NOT NULL COMMENT '满减:分;折扣:百分数(如85表示8.5折)',
  `min_spend`  BIGINT      NOT NULL DEFAULT 0 COMMENT '分',
  `valid_from` DATETIME    DEFAULT NULL,
  `valid_to`   DATETIME    DEFAULT NULL,
  `total`      INT         NOT NULL DEFAULT 0,
  `claimed`    INT         NOT NULL DEFAULT 0,
  `status`     TINYINT     NOT NULL DEFAULT 1,
  `created_at` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- 用户领券
-- ----------------------------
DROP TABLE IF EXISTS `user_coupons`;
CREATE TABLE `user_coupons` (
  `id`         BIGINT   NOT NULL AUTO_INCREMENT,
  `user_id`    BIGINT   NOT NULL,
  `coupon_id`  BIGINT   NOT NULL,
  `status`     VARCHAR(16) NOT NULL DEFAULT 'unused' COMMENT 'unused/used',
  `claimed_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
-- ----------------------------
-- 会话
-- ----------------------------
DROP TABLE IF EXISTS `sessions`;
CREATE TABLE `sessions` (
  `token` VARCHAR(128) NOT NULL,
  `user_id` BIGINT NOT NULL,
  `role` VARCHAR(16) NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `expires_at` DATETIME NOT NULL,
  PRIMARY KEY (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;