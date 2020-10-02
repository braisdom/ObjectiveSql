DROP IF NOT TABLE objective_sql.members;
DROP IF NOT TABLE objective_sql.orders;
DROP IF NOT TABLE objective_sql.order_lines;

CREATE TABLE IF NOT EXISTS objective_sql.members(
    id SERIAL PRIMARY KEY,
    no VARCHAR(100),
    name VARCHAR(100),
    gender INT,
    mobile VARCHAR(11),
    other_info VARCHAR(512),
    registered_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS objective_sql.orders(
    id SERIAL PRIMARY KEY,
    no VARCHAR(100),
    member_id VARCHAR(100),
    amount FLOAT,
    quantity FLOAT,
    sales_at TIMESTAMP
);

CREATE TABLE IF NOT EXISTS objective_sql.order_lines(
    id SERIAL PRIMARY KEY,
    order_id INT,
    order_no VARCHAR(100),
    barcode VARCHAR(100),
    product_id INT,
    member_id VARCHAR(100),
    sales_price FLOAT,
    amount FLOAT,
    quantity FLOAT
);