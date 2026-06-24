SET NAMES utf8mb4;

-- admin 账号 (admin / admin123), PBKDF2WithHmacSHA256, 100000 轮, 512 位
INSERT INTO `users` (`username`,`password_hash`,`salt`,`role`) VALUES
('admin','50fadf43c41d4ae87c57a05f0b946d54c79fafebe041b925af178a1d6ce623780b566bea477cc2a5e37098c4afa44b899b943b0fb435d5b0a9a287a9f082a060','shopsalt2026fixedxx','admin');

-- 分类
INSERT INTO `categories` (`name`,`icon`) VALUES
('手机数码','📱'),('电脑办公','💻'),('家用电器','🔌'),('服饰鞋包','👕'),('食品生鲜','🍱');

-- 商品 + 规格 + SKU 示例(两个商品带多规格,其余单规格)
-- 商品1: 手机(颜色+存储 双规格)
INSERT INTO `products` (`name`,`description`,`category_id`,`image_url`,`status`) VALUES
('星河手机 Pro','6.7寸 OLED,5000万像素,旗舰芯片。',1,'emoji:📱',1);
SET @p1 = LAST_INSERT_ID();
INSERT INTO `product_specs` (`product_id`,`name`) VALUES (@p1,'颜色'),(@p1,'存储');
SET @s1c = (SELECT id FROM product_specs WHERE product_id=@p1 AND name='颜色');
SET @s1m = (SELECT id FROM product_specs WHERE product_id=@p1 AND name='存储');
INSERT INTO `product_spec_options` (`spec_id`,`value`) VALUES
(@s1c,'黑色'),(@s1c,'白色'),(@s1m,'128G'),(@s1m,'256G');
SET @o1b = (SELECT id FROM product_spec_options WHERE spec_id=@s1c AND value='黑色');
SET @o1w = (SELECT id FROM product_spec_options WHERE spec_id=@s1c AND value='白色');
SET @o1_128 = (SELECT id FROM product_spec_options WHERE spec_id=@s1m AND value='128G');
SET @o1_256 = (SELECT id FROM product_spec_options WHERE spec_id=@s1m AND value='256G');
-- 4 个 SKU
INSERT INTO `skus` (`product_id`,`sku_code`,`price`,`stock`) VALUES
(@p1,'PH-BK-128',499900,50),(@p1,'PH-BK-256',549900,30),(@p1,'PH-WH-128',499900,40),(@p1,'PH-WH-256',549900,20);
SET @k1=(SELECT id FROM skus WHERE product_id=@p1 AND sku_code='PH-BK-128');
SET @k2=(SELECT id FROM skus WHERE product_id=@p1 AND sku_code='PH-BK-256');
SET @k3=(SELECT id FROM skus WHERE product_id=@p1 AND sku_code='PH-WH-128');
SET @k4=(SELECT id FROM skus WHERE product_id=@p1 AND sku_code='PH-WH-256');
INSERT INTO `sku_values` (`sku_id`,`spec_id`,`option_id`) VALUES
(@k1,@s1c,@o1b),(@k1,@s1m,@o1_128),(@k2,@s1c,@o1b),(@k2,@s1m,@o1_256),
(@k3,@s1c,@o1w),(@k3,@s1m,@o1_128),(@k4,@s1c,@o1w),(@k4,@s1m,@o1_256);

-- 商品2: 笔记本(颜色单规格)
INSERT INTO `products` (`name`,`description`,`category_id`,`image_url`,`status`) VALUES
('轻薄笔记本 Air','14寸,1.2kg,16G内存,长续航。',2,'emoji:💻',1);
SET @p2 = LAST_INSERT_ID();
INSERT INTO `product_specs` (`product_id`,`name`) VALUES (@p2,'颜色');
SET @s2c = (SELECT id FROM product_specs WHERE product_id=@p2 AND name='颜色');
INSERT INTO `product_spec_options` (`spec_id`,`value`) VALUES (@s2c,'银色'),(@s2c,'深空灰');
SET @o2s=(SELECT id FROM product_spec_options WHERE spec_id=@s2c AND value='银色');
SET @o2g=(SELECT id FROM product_spec_options WHERE spec_id=@s2c AND value='深空灰');
INSERT INTO `skus` (`product_id`,`sku_code`,`price`,`stock`) VALUES (@p2,'NB-SIL',699900,25),(@p2,'NB-GRAY',699900,15);
SET @k5=(SELECT id FROM skus WHERE product_id=@p2 AND sku_code='NB-SIL');
SET @k6=(SELECT id FROM skus WHERE product_id=@p2 AND sku_code='NB-GRAY');
INSERT INTO `sku_values` (`sku_id`,`spec_id`,`option_id`) VALUES (@k5,@s2c,@o2s),(@k6,@s2c,@o2g);

-- 商品3-6: 单规格(单 SKU,无规格选项)
INSERT INTO `products` (`name`,`description`,`category_id`,`image_url`,`status`) VALUES
('蓝牙耳机','主动降噪,30小时续航。',1,'emoji:🎧',1),
('机械键盘','热插拔,RGB背光。',2,'emoji:⌨️',1),
('电饭煲','4L,智能预约。',3,'emoji:🍚',1),
('纯棉T恤','100%棉,舒适透气。',4,'emoji:👕',1);
INSERT INTO `skus` (`product_id`,`sku_code`,`price`,`stock`) VALUES
(7,'SKU7',19900,100),(8,'SKU8',29900,80),(9,'SKU9',15900,60),(10,'SKU10',4900,200);

-- 优惠券: 满500减50 + 满1000打9折
INSERT INTO `coupons` (`code`,`name`,`type`,`value`,`min_spend`,`total`,`claimed`,`status`) VALUES
('SAVE50','满500减50','fixed',5000,50000,1000,0,1),
('OFF10','满1000打9折','percent',90,100000,1000,0,1);