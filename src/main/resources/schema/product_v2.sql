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

-- 3. Product Variants Table (Now images will be linked to variants)
CREATE TABLE tks.public.product_variants (
    id SERIAL PRIMARY KEY,
    product_id INT REFERENCES tks.public.product(id) ON DELETE CASCADE,
    sku VARCHAR(255) UNIQUE,
    base_price DECIMAL(10,2),
    currency VARCHAR(5),
    stock_quantity INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT NOW(),
    last_updated_at TIMESTAMP DEFAULT NOW()
);

-- 4. Product Variant Images Table (NEW: Links images to product variants)
CREATE TABLE tks.public.product_variant_images (
    id SERIAL PRIMARY KEY,
    variant_id INT REFERENCES tks.public.product_variants(id) ON DELETE CASCADE,
    image TEXT,
    created_at TIMESTAMP DEFAULT NOW()
);

-- 5. Attributes Table (No Changes)
CREATE TABLE tks.public.attributes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE,
    name_kh VARCHAR(100),
    status VARCHAR(10)
);

-- 6. Variant Attributes Table (Links attributes to variants)
CREATE TABLE tks.public.variant_attributes (
    id SERIAL PRIMARY KEY,
    variant_id INT REFERENCES tks.public.product_variants(id) ON DELETE CASCADE,
    attribute_id INT REFERENCES tks.public.attributes(id) ON DELETE CASCADE,
    value VARCHAR(255) NOT NULL,  -- Example: "Red", "M", "Cotton, Adidas"
    status VARCHAR(10)
);
