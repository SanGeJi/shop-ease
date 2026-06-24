# ShopEase 商城

Java Spring Boot + Vue 3 全栈电商平台。

## 技术栈

- **后端**: Spring Boot 3.3.5 + MyBatis + MySQL 8
- **前端**: Vue 3 + Chart.js
- **构建**: Maven

## 功能

### 前台（顾客）
- 注册/登录
- 商品浏览（分类筛选、搜索、分页）
- 商品详情（SKU 规格选择）
- 购物车
- 下单结算（选地址、选优惠券）
- 模拟支付
- 我的订单
- 商品评价
- 优惠券领取
- 个人中心（地址管理）

### 后台（管理员）
- 数据看板（销售额/订单数/商品数/用户数）
- 商品 CRUD + 图片上传
- SKU 规格管理
- 分类管理
- 订单管理（改状态）
- 优惠券管理
- 用户列表

## 快速启动

前置条件: JDK 17+、MySQL 8

`ash
# 1. 创建数据库
mysql -u root -p < sql/schema.sql
mysql -u root -p shop < sql/seed.sql

# 2. 修改 application.yml 中的数据库密码

# 3. 启动
mvn spring-boot:run
# 或双击 run-server.bat
`

访问 http://localhost:8080

- 管理员: dmin / dmin123
- 测试用户: 注册新账号

## 项目结构

`
shop/
  src/main/java/com/shop/
    common/     # 通用（认证、拦截器、返回值）
    config/     # Spring 配置
    controller/ # API 控制器
    entity/     # 数据实体
    mapper/     # MyBatis 数据访问
  src/main/resources/
    static/     # 前端页面（Vue SPA）
    application.yml  # 应用配置
  sql/          # 数据库建表 + 种子数据
  pom.xml       # Maven 配置
`