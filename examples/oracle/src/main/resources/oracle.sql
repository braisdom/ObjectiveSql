DROP SEQUENCE global_seq;
DROP TABLE members;
DROP TABLE orders;

CREATE SEQUENCE global_seq START WITH 1;
CREATE TABLE members(
    id INTEGER NOT NULL PRIMARY KEY,
    no VARCHAR2(100),
    name VARCHAR2(100),
    gender SMALLINT,
    mobile VARCHAR2(11),
    extended_attributes VARCHAR2(512),
    registered_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE orders(
   id INTEGER NOT NULL PRIMARY KEY,
   no VARCHAR2(100),
   member_id INTEGER,
   amount REAL,
   quantity REAL,
   sales_at TIMESTAMP
);