# ShopEase 商城 — 全栈购物平台 需求文档

## 1. 项目概述

从零开发的全栈购物平台(Java 实现),包含顾客前台与管理员后台。功能覆盖:多规格商品(SKU)、购物车、模拟支付、订单全流程、商品评价、优惠券促销、商品图片上传。单 Spring Boot 进程同时提供 REST API 和前端页面。

独立开发,业务代码全部自写。

## 2. 技术栈

| 层 | 选型 |
| --- | --- |
| 后端 | Java 17+ / Spring Boot / MyBatis |
| 数据库 | MySQL 8 |
| 认证 | 会话 token + 角色(customer / admin) |
| 前端 | Vue 3 + Chart.js(本地 vendor,不联网加载)+ 原生 CSS |
| 构建 | Maven |
| 图片上传 | Spring Boot MultipartFile,存本地目录,静态对外提供 |

## 3. 角色

- customer:浏览、购物车、下单、模拟支付、评价、领券
- admin:管理商品/分类/SKU/订单/优惠券/用户,数据看板

## 4. 功能需求

### 4.1 顾客前台
- 注册 / 登录 / 登出
- 商品列表(分类筛选、关键字搜索、分页)
- 商品详情:图片、描述、**多规格选择**(选规格 → 对应 SKU 的价格/库存)、加入购物车、商品评价列表
- 购物车:按 SKU 增删改数量、合计
- 结算:选收货信息、**选优惠券**(自动算折扣)、生成订单(扣 SKU 库存)、订单状态=待付款
- **模拟支付**:订单页点"去支付"→ 模拟支付成功(无真实网关)→ 状态变已付款
- 我的订单:列表、状态、明细、**已完成的订单可写评价**
- 优惠券:可领取的券列表、我的券

### 4.2 管理员后台
- 管理员登录(预置 admin / admin123)
- 数据看板:总销售额、订单数、商品数、用户数 + 销售趋势折线图 + 分类销售占比环形图
- 商品管理:增删改查,含**图片上传**
- **SKU 管理**:为商品定义规格(如颜色/尺寸)+ 规格选项,生成 SKU(每个 SKU 独立价格/库存)
- 分类管理:增删改查
- 订单管理:列表、改状态(待付款→已付款→已发货→已完成)、查看明细
- 优惠券管理:创建(满减/折扣、门槛、有效期、发放量)、查看领取情况
- 用户管理:列表

## 5. 数据模型

```
users(id, username, password_hash, salt, role, phone, address, created_at)
categories(id, name, icon, created_at)
products(id, name, description, category_id, image_url, status, created_at)
product_specs(id, product_id, name)              -- 规格名,如"颜色"
product_spec_options(id, spec_id, value)         -- 选项,如"黑色"
skus(id, product_id, sku_code, price, stock)     -- 具体规格组合
sku_values(id, sku_id, spec_id, option_id)       -- 该 SKU 对应每个规格选的选项
cart_items(id, user_id, sku_id, quantity)
orders(id, order_no, user_id, total, discount, status, receiver, phone, address, coupon_id, created_at)
order_items(id, order_id, sku_id, product_name, sku_specs, price, quantity)
payments(id, order_id, method, amount, status, paid_at)   -- 模拟支付记录
reviews(id, product_id, user_id, rating, content, created_at)
coupons(id, code, name, type, value, min_spend, valid_from, valid_to, total, claimed, status)
user_coupons(id, user_id, coupon_id, status, claimed_at)
```

## 6. API 设计(`/api/*`,JSON,token 鉴权,admin 接口校验角色 403)

```
POST /api/auth/register  POST /api/auth/login  POST /api/auth/logout  GET /api/auth/me
GET  /api/products?category=&q=&page=   GET /api/products/:id   GET /api/categories
GET/POST/PUT/DELETE /api/cart            (按 SKU)
POST /api/orders       GET /api/orders   POST /api/orders/:id/pay  (模拟支付)
POST /api/reviews      GET /api/products/:id/reviews
GET  /api/coupons      POST /api/coupons/:id/claim   GET /api/my/coupons

# admin
GET /api/admin/dashboard
GET/POST/PUT/DELETE /api/admin/products   POST /api/admin/products/:id/image (上传)
GET/POST/PUT/DELETE /api/admin/categories
GET/POST/PUT/DELETE /api/admin/products/:id/skus  (SKU 与规格)
GET /api/admin/orders   PATCH /api/admin/orders/:id
GET/POST/PUT/DELETE /api/admin/coupons
GET /api/admin/users
```

## 7. 前端(单 SPA,hash 路由)
前台:`#/` `#/product/:id` `#/cart` `#/checkout` `#/orders` `#/orders/:id` `#/coupons` `#/login`
后台:`#/admin` `#/admin/products` `#/admin/categories` `#/admin/orders` `#/admin/coupons` `#/admin/users`

## 8. 项目结构
```
shop/
  pom.xml
  src/main/java/com/shop/{ShopApplication, config, controller, service, mapper, entity, common}
  src/main/resources/{application.yml, mapper/*.xml, static/(index.html,app.js,style.css,vendor/), uploads/}
  sql/schema.sql + seed.sql
  start.bat  (启动 MySQL + java -jar)
  README.md  REQUIREMENTS.md  .gitignore
```

## 9. 非功能
- 安全:MyBatis 参数化、密码哈希、admin 角色校验、下单扣 SKU 库存用事务防超卖
- 鲁棒:输入校验、错误提示
- 可运行:start.bat → MySQL + Spring Boot → localhost:8080
- 预置:1 admin + 分类 + 示例商品(带 SKU)+ 一张优惠券

## 10. 验收
1. 顾客:注册→浏览→选规格→加购物车→用券结算→模拟支付→评价
2. admin:登录→看板→管理商品/SKU/分类/订单/券→上传商品图
3. SKU 库存下单正确扣减、防超卖
4. 优惠券满减/折扣计算正确
5. 顾客访问 admin 接口返 403