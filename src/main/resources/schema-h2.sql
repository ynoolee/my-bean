drop table if exists order_items;
drop table if exists products;
drop table if exists orders;

CREATE TABLE products
(
    product_id   BINARY(16) PRIMARY KEY,
    product_name VARCHAR(20) NOT NULL,
    price        BIGINT      NOT NULL,
    stock        int         NOT NULL DEFAULT 0,
    description  VARCHAR(500) DEFAULT NULL,
    created_at   datetime(6) NOT NULL,
    updated_at   datetime(6) DEFAULT NULL
);

CREATE TABLE orders
(
    order_id     BINARY(16) PRIMARY KEY,
    email        VARCHAR(50)  NOT NULL,
    address      VARCHAR(200) NOT NULL,
    postcode     VARCHAR(200) NOT NULL,
    order_status VARCHAR(50)  NOT NULL,
    created_at    datetime(6) NOT NULL,
    updated_at    datetime(6) DEFAULT NULL
);

CREATE TABLE order_items
(
    seq        bigint      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_id   BINARY(16) NOT NULL,
    product_id BINARY(16) NOT NULL,
    quantity   int         NOT NULL,
    created_at datetime(6) NOT NULL,
    updated_at datetime(6) DEFAULT NULL,
    UNIQUE(order_id),
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
)