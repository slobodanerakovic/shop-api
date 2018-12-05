-- Sql to be executed from favorite db client (pgadmin for example)
-- Schema creation
CREATE SCHEMA IF NOT EXISTS shop AUTHORIZATION test;
GRANT ALL ON SCHEMA shop TO test;

--reset everything
--recreate dbs
DROP TABLE IF EXISTS shop.product CASCADE;
DROP TABLE IF EXISTS shop.order CASCADE;
DROP TABLE IF EXISTS shop.order_product CASCADE;

--recreate sequences
DROP SEQUENCE IF EXISTS shop.seq_product;
DROP SEQUENCE IF EXISTS shop.seq_order;
DROP SEQUENCE IF EXISTS shop.seq_order_product;


-- Create table order
CREATE TABLE shop.order (
id bigint NOT NULL,
version integer NOT NULL DEFAULT 0,
creation_date timestamp without time zone NOT NULL,
buyer_email character varying(100) NOT NULL,
order_amount double precision NOT NULL,
CONSTRAINT order_pkey PRIMARY KEY (id)
)
WITH (
OIDS=FALSE
);
ALTER TABLE shop.order OWNER TO test;
GRANT ALL ON TABLE shop.order TO test;

CREATE SEQUENCE shop.seq_order START 1000;
ALTER TABLE shop.seq_order OWNER TO test;

--Create table product
CREATE TABLE shop.product (
id bigint NOT NULL,
version integer NOT NULL DEFAULT 0,
creation_date timestamp without time zone NOT NULL,
modification_date timestamp without time zone,
name character varying(50) NOT NULL,
price double precision NOT NULL,
active boolean NOT NULL DEFAULT true,
order_id bigint,
CONSTRAINT product_pkey PRIMARY KEY (id),
CONSTRAINT product_order_fkey FOREIGN KEY (order_id)
REFERENCES shop.order (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS=FALSE
);
ALTER TABLE shop.product OWNER TO test;
GRANT ALL ON TABLE shop.product TO test;

CREATE SEQUENCE shop.seq_product START 1000;
ALTER TABLE shop.seq_product OWNER TO test;


-- Create table order_product
CREATE TABLE shop.order_product (
version integer NOT NULL DEFAULT 0,
creation_date timestamp without time zone NOT NULL DEFAULT now(),
order_id bigint NOT NULL,
product_id bigint NOT NULL,
CONSTRAINT order_product_pkey PRIMARY KEY (order_id, product_id),
CONSTRAINT order_id_fkey FOREIGN KEY (order_id)
REFERENCES shop.order (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION,
CONSTRAINT product_id_fkey FOREIGN KEY (product_id)
REFERENCES shop.product (id) MATCH SIMPLE
ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
OIDS=FALSE
);
ALTER TABLE shop.order_product OWNER TO test;
GRANT ALL ON TABLE shop.order_product TO test;

CREATE SEQUENCE shop.seq_order_product START 1000;
ALTER TABLE shop.seq_order_product OWNER TO test;
