
CREATE TABLE objective_sql.dbo.members(
    id bigint IDENTITY(1,1) PRIMARY KEY,
    no VARCHAR(100),
    name VARCHAR(100),
    gender INT,
    mobile VARCHAR(11),
    extended_attributes VARCHAR(512),
    registered_at DATETIME,
    updated_at DATETIME
);

CREATE TABLE objective_sql.dbo.orders(
    id INT IDENTITY(1,1) PRIMARY KEY,
    no VARCHAR(100),
    member_id VARCHAR(100),
    amount FLOAT,
    quantity FLOAT,
    sales_at DATETIME
);

CREATE TABLE objective_sql.dbo.order_lines(
    id INT IDENTITY(1,1) PRIMARY KEY,
    order_id INT,
    order_no VARCHAR(100),
    barcode VARCHAR(100),
    product_id INT,
    member_id VARCHAR(100),
    sales_price FLOAT,
    amount FLOAT,
    quantity FLOAT
);

CREATE TABLE objective_sql.dbo.products(
    id INT IDENTITY(1,1) PRIMARY KEY,
    barcode VARCHAR(100),
    name VARCHAR(100),
    category_id INT,
    sales_price FLOAT,
    cost FLOAT
);