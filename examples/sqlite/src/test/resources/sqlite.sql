drop table if exists members;
drop table if exists orders;
drop table if exists order_lines;

create table members (id INTEGER PRIMARY KEY AUTOINCREMENT,
    no TEXT, name TEXT,
    gender INTEGER,
    mobile TEXT,
    extended_attributes TEXT,
    registered_at TEXT,
    updated_at TEXT);

create table orders (order_id INTEGER PRIMARY KEY AUTOINCREMENT,
    no TEXT, member_id INTEGER,
    amount REAL,
    quantity REAL,
    sales_at TEXT);

create table order_lines (id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_no TEXT,
    amount REAL,
    quantity REAL);