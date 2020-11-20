drop table if exists dbo.members;
drop table if exists dbo.orders;
drop table if exists dbo.order_lines;
drop table if exists dbo.products;

CREATE TABLE dbo.members(
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    no VARCHAR(100),
    name VARCHAR(100),
    gender INT,
    mobile VARCHAR(11),
    extended_attributes VARCHAR(512),
    registered_at DATETIME,
    updated_at DATETIME
);

CREATE TABLE dbo.orders(
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    no VARCHAR(100),
    member_id BIGINT,
    amount REAL,
    quantity REAL,
    sales_at DATETIME
);

CREATE TABLE dbo.order_lines(
    id BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id INT,
    order_no VARCHAR(100),
    barcode VARCHAR(100),
    product_id INT,
    member_id BIGINT,
    sales_price REAL,
    amount REAL,
    quantity REAL
);

CREATE TABLE dbo.products(
   id BIGINT IDENTITY(1,1) PRIMARY KEY,
   barcode VARCHAR(100),
   name VARCHAR(100),
   category_id INT,
   sales_price REAL,
   cost REAL
);
