-- 测试数据库表结构初始化脚本
-- 用于 supermarket_test 隔离测试环境

-- 会员表
CREATE TABLE IF NOT EXISTS member (
    memberid VARCHAR(64) PRIMARY KEY COMMENT '会员ID',
    password VARCHAR(128) NOT NULL COMMENT '密码',
    name VARCHAR(50) COMMENT '姓名',
    sex VARCHAR(10) COMMENT '性别',
    birthday DATE COMMENT '生日'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员表';

-- 会员卡表
CREATE TABLE IF NOT EXISTS card (
    cardid VARCHAR(64) PRIMARY KEY COMMENT '卡号',
    memberid VARCHAR(64) NOT NULL COMMENT '会员ID',
    balance INT DEFAULT 0 COMMENT '余额(分)',
    integral INT DEFAULT 0 COMMENT '积分',
    lose INT DEFAULT 0 COMMENT '挂失状态：0正常，1挂失',
    FOREIGN KEY (memberid) REFERENCES member(memberid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会员卡表';

-- 商品表
CREATE TABLE IF NOT EXISTS product (
    productid INT AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    price INT NOT NULL COMMENT '价格(分)',
    stock INT DEFAULT 0 COMMENT '库存',
    integral INT DEFAULT 0 COMMENT '购买获得积分',
    category VARCHAR(50) COMMENT '分类',
    description TEXT COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    orderid VARCHAR(64) PRIMARY KEY COMMENT '订单ID',
    cardid VARCHAR(64) NOT NULL COMMENT '会员卡ID',
    order_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
    total_amount INT COMMENT '总金额(分)',
    total_integral INT COMMENT '总积分',
    payment_method INT DEFAULT 0 COMMENT '支付方式：0余额，1积分',
    status INT DEFAULT 1 COMMENT '状态：1已完成，2已取消',
    FOREIGN KEY (cardid) REFERENCES card(cardid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单项表
CREATE TABLE IF NOT EXISTS order_item (
    id INT AUTO_INCREMENT PRIMARY KEY COMMENT '订单项ID',
    orderid VARCHAR(64) NOT NULL COMMENT '订单ID',
    productid INT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL COMMENT '数量',
    price INT NOT NULL COMMENT '单价(分)',
    subtotal INT COMMENT '小计(分)',
    FOREIGN KEY (orderid) REFERENCES orders(orderid) ON DELETE CASCADE,
    FOREIGN KEY (productid) REFERENCES product(productid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单项表';

-- 交易记录表
CREATE TABLE IF NOT EXISTS record (
    recordid INT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    cardid VARCHAR(64) NOT NULL COMMENT '卡号',
    value INT NOT NULL COMMENT '金额/积分变化',
    spend_type INT COMMENT '类型：0金额，1积分',
    time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '交易时间',
    orderid VARCHAR(64) COMMENT '关联订单ID',
    description VARCHAR(255) COMMENT '描述',
    FOREIGN KEY (cardid) REFERENCES card(cardid)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交易记录表';

-- 管理员表
CREATE TABLE IF NOT EXISTS manager (
    managerid VARCHAR(64) PRIMARY KEY COMMENT '管理员ID',
    password VARCHAR(128) NOT NULL COMMENT '密码',
    name VARCHAR(50) COMMENT '姓名',
    role VARCHAR(20) DEFAULT 'ADMIN' COMMENT '角色',
    CONSTRAINT chk_manager_role CHECK (role IN ('ADMIN', 'OPERATOR'))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理员表';

-- 供应商表
CREATE TABLE IF NOT EXISTS supplier (
    supplier_id VARCHAR(64) PRIMARY KEY COMMENT '供应商ID',
    name VARCHAR(100) NOT NULL COMMENT '供应商名称',
    contact VARCHAR(50) COMMENT '联系人',
    phone VARCHAR(20) COMMENT '联系电话',
    address VARCHAR(255) COMMENT '地址',
    status INT DEFAULT 1 COMMENT '状态：1正常，0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商表';

-- 采购订单表
CREATE TABLE IF NOT EXISTS purchase_order (
    purchase_id VARCHAR(64) PRIMARY KEY COMMENT '采购单ID',
    supplier_id VARCHAR(64) COMMENT '供应商ID',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '下单日期',
    expected_date DATETIME COMMENT '预计到货日期',
    actual_date DATETIME COMMENT '实际到货日期',
    total_amount DECIMAL(12,2) COMMENT '总金额',
    status INT DEFAULT 1 COMMENT '状态：1待处理，2已发货，3已完成，4已取消',
    operator VARCHAR(64) COMMENT '操作员',
    remark TEXT COMMENT '备注',
    FOREIGN KEY (supplier_id) REFERENCES supplier(supplier_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单表';

-- 促销活动表
CREATE TABLE IF NOT EXISTS promotion (
    promotion_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '促销ID',
    name VARCHAR(100) NOT NULL COMMENT '活动名称',
    description TEXT COMMENT '活动描述',
    discount_rate DECIMAL(5,2) COMMENT '折扣率',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    status INT DEFAULT 1 COMMENT '状态：1启用，0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='促销活动表';

-- 优惠券表
CREATE TABLE IF NOT EXISTS coupon (
    coupon_id INT AUTO_INCREMENT PRIMARY KEY COMMENT '优惠券ID',
    name VARCHAR(100) NOT NULL COMMENT '优惠券名称',
    description TEXT COMMENT '描述',
    discount_type INT COMMENT '类型：1满减，2折扣',
    discount_value DECIMAL(10,2) COMMENT '优惠值',
    min_amount DECIMAL(10,2) COMMENT '最低消费',
    start_time DATETIME COMMENT '开始时间',
    end_time DATETIME COMMENT '结束时间',
    status INT DEFAULT 1 COMMENT '状态：1启用，0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';
