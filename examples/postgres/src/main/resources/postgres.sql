drop table if exists members;
drop table if exists orders;
drop table if exists order_lines;
drop table if exists products;

CREATE TABLE members (
	id bigserial NOT NULL,
	"no" varchar(100) NULL,
	name varchar(100) NULL,
	gender int4 NULL,
	mobile varchar(11) NULL,
	extended_attributes varchar(512) NULL,
	registered_at timestamp NULL,
	updated_at timestamp NULL,
	CONSTRAINT members_pkey PRIMARY KEY (id)
);

CREATE TABLE orders (
	id bigserial NOT NULL,
	"no" varchar(100) NULL,
	member_id int8 NULL,
	amount float4 NULL,
	quantity float4 NULL,
	sales_at timestamp NULL,
	CONSTRAINT orders_pkey PRIMARY KEY (id)
);

CREATE TABLE order_lines (
	id bigserial NOT NULL,
	order_id int8 NULL,
	order_no varchar(100) NULL,
	barcode varchar(100) NULL,
	product_id int8 NULL,
	member_id int8 NULL,
	sales_price float4 NULL,
	amount float4 NULL,
	quantity float4 NULL,
	CONSTRAINT order_lines_pkey PRIMARY KEY (id)
);

CREATE TABLE products(
   id bigserial NOT NULL,
   barcode VARCHAR(100),
   name VARCHAR(100),
   category_id int8,
   sales_price float4,
   cost float4,
   CONSTRAINT products_pkey PRIMARY KEY (id)
);
