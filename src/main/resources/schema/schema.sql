---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--1
CREATE TABLE public.category (
	id serial4 NOT NULL,
	"name" varchar(255) NOT NULL,
	name_kh varchar(255) NULL,
	description text NULL,
	status varchar(10) NULL,
	created_at timestamp NULL,
	last_updated_at timestamp NULL,
	CONSTRAINT category_name_key UNIQUE (name),
	CONSTRAINT category_pkey PRIMARY KEY (id)
);
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--2
CREATE TABLE public."attributes" (
	id serial4 NOT NULL,
	"name" varchar(255) NULL,
	name_kh varchar(100) NULL,
	status varchar(10) NULL,
	CONSTRAINT attributes_name_key UNIQUE (name),
	CONSTRAINT attributes_pkey PRIMARY KEY (id)
);
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--3
CREATE TABLE public.product (
	id serial4 NOT NULL,
	name_en varchar(255) NOT NULL,
	name_kh varchar(255) NOT NULL,
	code varchar(100) NULL,
	category_id int4 NULL,
	sale_price numeric(10, 2) NULL,
	currency varchar(5) NULL,
	description text NULL,
	status varchar(10) NULL,
	created_at timestamp NULL,
	last_updated_at timestamp NULL,
	CONSTRAINT product_pkey PRIMARY KEY (id)
);
-- public.product foreign keys
ALTER TABLE public.product ADD CONSTRAINT product_category_id_fkey FOREIGN KEY (category_id) REFERENCES public.category(id) ON DELETE SET NULL;
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--4
CREATE TABLE public.variants (
	id serial4 NOT NULL,
	product_id int4 NULL,
	sku varchar(255) NULL,
	base_price numeric(10, 2) NULL,
	currency varchar(5) NULL,
	stock_quantity int4 NULL DEFAULT 0,
	status varchar(10) NOT NULL,
	created_at timestamp NULL DEFAULT now(),
	last_updated_at timestamp NULL DEFAULT now(),
	CONSTRAINT variants_pkey PRIMARY KEY (id)
);
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--5
CREATE TABLE public.variant_attributes (
	id serial4 NOT NULL,
	variant_id int4 NULL,
	attribute_id int4 NULL,
	value varchar(255) NOT NULL,
	status varchar(10) NULL,
	CONSTRAINT variant_attributes_pkey PRIMARY KEY (id)
);
-- public.variant_attributes foreign keys
ALTER TABLE public.variant_attributes ADD CONSTRAINT variant_attributes_attribute_id_fkey FOREIGN KEY (attribute_id) REFERENCES public."attributes"(id) ON DELETE CASCADE;
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--6
CREATE TABLE public.images (
	id serial4 NOT NULL,
	variant_id int4 NULL,
	status bool NULL DEFAULT true,
	file_name varchar(255) NULL,
	file_type varchar(100) NULL,
	"size" int8 NULL,
	"uuid" varchar(36) NULL,
	system_name varchar(255) NULL,
	"data" bytea NULL,
	created_by varchar(255) NULL,
	created_date timestamp NULL DEFAULT CURRENT_TIMESTAMP,
	updated_by varchar(255) NULL,
	updated_date timestamp NULL DEFAULT CURRENT_TIMESTAMP,
	CONSTRAINT images_pkey PRIMARY KEY (id)
);
---------------------------------------------------------------------------------------------------------------------------------------------------------------------------
--Category
INSERT INTO public.category("name", name_kh, description, status, created_at, last_updated_at) VALUES('Other', 'ផ្សេងៗ', NULL, 'ACTIVE', now(), NULL);
INSERT INTO public.category("name", name_kh, description, status, created_at, last_updated_at) VALUES('Belt', 'ខ្សែរក្រវ៉ាត់', NULL, 'ACTIVE', now(), NULL);
INSERT INTO public.category("name", name_kh, description, status, created_at, last_updated_at) VALUES('Male-Pant', 'ខោ(ប្រុស)', NULL, 'ACTIVE', now(), NULL);
INSERT INTO public.category("name", name_kh, description, status, created_at, last_updated_at) VALUES('Female-Pant', 'ខោ(ស្រី)', NULL, 'ACTIVE', now(), NULL);
INSERT INTO public.category("name", name_kh, description, status, created_at, last_updated_at) VALUES('Male-Shirt', 'អាវ(ប្រុស)', NULL, 'ACTIVE', now(), NULL);
INSERT INTO public.category("name", name_kh, description, status, created_at, last_updated_at) VALUES('Female-Shirt', 'អាវ(ស្រី)', NULL, 'ACTIVE', now(), NULL);
INSERT INTO public.category("name", name_kh, description, status, created_at, last_updated_at) VALUES('Bag', 'កាបូប', NULL, 'ACTIVE', now(), NULL);
INSERT INTO public.category("name", name_kh, description, status, created_at, last_updated_at) VALUES('Male-Shoes', 'ស្បែកជើង(ប្រុស)', NULL, 'ACTIVE', now(), NULL);
INSERT INTO public.category("name", name_kh, description, status, created_at, last_updated_at) VALUES('Female-Shoes', 'ស្បែកជើង(ស្រី)', NULL, 'ACTIVE', now(), NULL);
INSERT INTO public.category("name", name_kh, description, status, created_at, last_updated_at) VALUES('Boy-Shoes', 'ស្បែកជើងក្មេង(ប្រុស)', NULL, 'ACTIVE', now(), NULL);
INSERT INTO public.category("name", name_kh, description, status, created_at, last_updated_at) VALUES('Girl-Shoes', 'ស្បែកជើងក្មេង(ស្រី)', NULL, 'ACTIVE', now(), NULL);
INSERT INTO public.category("name", name_kh, description, status, created_at, last_updated_at) VALUES('Socks', 'ស្រោមជើង', NULL, 'ACTIVE', now(), NULL);
INSERT INTO public.category("name", name_kh, description, status, created_at, last_updated_at) VALUES('Glove', 'ស្រោមដៃ', NULL, 'ACTIVE', now(), NULL);
INSERT INTO public.category("name", name_kh, description, status, created_at, last_updated_at) VALUES('Sport Equipment', 'សម្ពារះកីឡា', NULL, 'ACTIVE', now(), NULL);

-- Attributes
INSERT INTO public."attributes" ("name", name_kh, status) VALUES('Color', 'ពណ៍', 'ACTIVE');
INSERT INTO public."attributes" ("name", name_kh, status) VALUES('Size', 'ទំហំ', 'ACTIVE');
INSERT INTO public."attributes" ("name", name_kh, status) VALUES('Number', 'លេខ', 'ACTIVE');
INSERT INTO public."attributes" ("name", name_kh, status) VALUES('Length', 'ប្រវែង', 'ACTIVE');
INSERT INTO public."attributes" ("name", name_kh, status) VALUES('Material', 'រូបធាតុ/ស្បែក', 'ACTIVE');
INSERT INTO public."attributes" ("name", name_kh, status) VALUES('Weight', 'ទំងន់', 'ACTIVE');
INSERT INTO public."attributes" ("name", name_kh, status) VALUES('Brand', 'ផ្លាកហាង', 'ACTIVE');
INSERT INTO public."attributes" ("name", name_kh, status) VALUES('Country', 'ប្រទេស', 'ACTIVE');
INSERT INTO public."attributes" ("name", name_kh, status) VALUES('Smell', 'ក្លិន', 'ACTIVE');
INSERT INTO public."attributes" ("name", name_kh, status) VALUES('Quality', 'លេខគុណភាព', 'ACTIVE');