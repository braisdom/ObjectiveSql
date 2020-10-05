CREATE SEQUENCE members_seq START WITH 1;
CREATE TABLE objective_sql.members(
    id NUMBER NOT NULL PRIMARY KEY,
    no VARCHAR2(100),
    name VARCHAR2(100),
    gender SMALLINT,
    mobile VARCHAR2(11),
    extended_attributes VARCHAR2(512),
    registered_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE objective_sql.orders(
   id NUMBER NOT NULL PRIMARY KEY,
   no VARCHAR2(100),
   member_id INT,
   amount FLOAT,
   quantity FLOAT,
   sales_at TIMESTAMP
);