CREATE SEQUENCE members_seq START WITH 1;
CREATE TABLE objective_sql.members(
    id NUMBER(10) NOT NULL ,
    no VARCHAR2(100),
    name VARCHAR2(100),
    gender INT,
    mobile VARCHAR2(11),
    extended_attributes VARCHAR2(512),
    registered_at TIMESTAMP,
    updated_at TIMESTAMP
);
