-- CategoryTable
CREATE table tks.public.category (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    name_kh VARCHAR(255),
    description TEXT,
    status varchar(10),
    created_at TIMESTAMP,
    last_updated_at TIMESTAMP
);

-- Products Table
sd

-- Product Images (Stored as Base64)
CREATE table tks.public.product_images (
    id SERIAL PRIMARY KEY,
    product_id INT REFERENCES products(id) ON DELETE CASCADE,
    image TEXT,
    created_at TIMESTAMP
);


-- Product Variants Table (E.g., Red Shirt, Blue Shirt)
CREATE TABLE tks.public.product_variants (
    id SERIAL PRIMARY KEY,
    product_id INT REFERENCES products(id) ON DELETE CASCADE,
    sku VARCHAR(255) UNIQUE NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    currency VARCHAR(5),
    stock_quantity INT,
    created_at TIMESTAMP,
    last_updated_at TIMESTAMP
);

Brand: Nike
Model: Air Zoom Pegasus
Gender: Men
Shoe Type: Running Shoes
Upper Material: Mesh
Sole Material: Rubber
Closure Type: Lace-up
Color Options: Black, Blue, Red
Sizes Available: 7, 8, 9, 10, 11
-- Attributes Table (E.g., Color, Size, Brand)
CREATE TABLE tks.public.attributes (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(10)
);


Variant 1:
Color:
Size:
Price:
SKU:
-- Mapping Variants to Attributes (E.g., Color: Red)
CREATE TABLE tks.public.variant_attributes (
    id SERIAL PRIMARY KEY,
    variant_id INT REFERENCES product_variants(id) ON DELETE CASCADE,
    attribute_id INT REFERENCES attributes(id) ON DELETE CASCADE,
    value VARCHAR(255) NOT NULL,  -- Example: "Red", "M", "Cotton, Adidas"
    status VARCHAR(10)
);
