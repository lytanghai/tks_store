Full Example
INSERT INTO tks.public.category (name, name_kh, description, status)
VALUES (1,'Clothing', 'សម្លៀកបំពាក់', 'Apparel including shirts, pants, and more', 'ACTIVE');

INSERT INTO tks.public.product (name_en, name_kh, code, category_id, sale_price, currency, description, status)
VALUES (1,'Men T-Shirt', 'អាវយឺតបុរស', 'TSHIRT001', 1, 15.99, 'USD', 'Comfortable cotton T-shirt for men', 'ACTIVE');

INSERT INTO tks.public.attributes (name, name_kh, status)
VALUES
('Size', 'ទំហំ', 'ACTIVE'),
('Color', 'ពណ៌', 'ACTIVE'),
('Material', 'សម្ភារៈ', 'ACTIVE');

INSERT INTO tks.public.variants (product_id, sku, base_price, currency, stock_quantity, status)
VALUES
(1, 'TSHIRT001-RED-M', 15.99, 'USD', 50, 'ACTIVE'),
(1, 'TSHIRT001-BLUE-L', 15.99, 'USD', 30, 'ACTIVE');

INSERT INTO tks.public.variant_images (variant_id, image,image_type)
VALUES
(1, 'https://example.com/images/tshirt_red_m.jpg', 'Front'),
(2, 'https://example.com/images/tshirt_blue_l.jpg', 'Back');

INSERT INTO tks.public.variant_attributes (variant_id, attribute_id, value, status)
VALUES
(1, 1, 'M', 'ACTIVE'),  -- Medium size
(1, 2, 'Red', 'ACTIVE'),
(1, 3, 'Cotton', 'ACTIVE'),
(2, 1, 'L', 'ACTIVE'),  -- Large size
(2, 2, 'Blue', 'ACTIVE'),
(2, 3, 'Cotton', 'ACTIVE');


-- 1. Category Table (No Changes)
CREATE TABLE tks.public.category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    name_kh VARCHAR(255),
    description TEXT,
    status VARCHAR(10),
    created_at TIMESTAMP DEFAULT NOW(),
    last_updated_at TIMESTAMP DEFAULT NOW()
);

-- 2. Product Table (No Changes)
CREATE TABLE tks.public.product (
    id SERIAL PRIMARY KEY,
    name_en VARCHAR(255),
    name_kh VARCHAR(255),
    code VARCHAR(255),
    category_id INT REFERENCES tks.public.category(id) ON DELETE SET NULL,
    sale_price DECIMAL(10,2),
    currency VARCHAR(5),
    description TEXT,
    status VARCHAR(10),
    created_at TIMESTAMP DEFAULT NOW(),
    last_updated_at TIMESTAMP DEFAULT NOW()
);

-- 3. Attributes Table (No Changes)
CREATE TABLE tks.public.attributes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    name_kh VARCHAR(100),
    status VARCHAR(10)
);

-- 4. Product Variants Table (Now images will be linked to variants) use it as a tab(s) when create product
CREATE TABLE tks.public.variants (
    id SERIAL PRIMARY KEY,
    product_id INT REFERENCES tks.public.product(id) ON DELETE CASCADE,
    sku VARCHAR(255) UNIQUE,
    base_price DECIMAL(10,2),
    currency VARCHAR(5),
    stock_quantity INT DEFAULT 0,
    status VARCHAR(10) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW(),
    last_updated_at TIMESTAMP DEFAULT NOW()
);

-- 5. Product Variant Images Table (NEW: Links images to product variants) store in tab(s)
CREATE TABLE tks.public.variant_images (
    id SERIAL PRIMARY KEY,
    variant_id INT REFERENCES tks.public.variants(id) ON DELETE CASCADE,
    image TEXT,
    image_type VARCHAR(50),
    created_at TIMESTAMP DEFAULT NOW(),
    last_updated_at TIMESTAMP DEFAULT NOW()
);


-- 6. Variant Attributes Table (Links attributes to variants)
CREATE TABLE tks.public.variant_attributes (
    id SERIAL PRIMARY KEY,
    variant_id INT REFERENCES tks.public.variants(id) ON DELETE CASCADE,
    attribute_id INT REFERENCES tks.public.attributes(id) ON DELETE CASCADE,
    value VARCHAR(255) NOT NULL,  -- Example: "Red", "M", "Cotton, Adidas"
    status VARCHAR(10)
);
