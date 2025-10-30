Create database test;
use test;
drop database test;
-- Bảng Người Dùng (User)
CREATE TABLE User (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100),
    phone_number VARCHAR(20) UNIQUE,
    registration_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    role ENUM('CUSTOMER', 'SELLER', 'ADMIN') DEFAULT 'CUSTOMER' NOT NULL -- Thêm trường phân quyền
);

-- Bảng Địa Chỉ (Address)
CREATE TABLE Address (
    address_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    street VARCHAR(255) NOT NULL,
    district VARCHAR(100),
    city VARCHAR(100) NOT NULL,
    is_default BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- Bảng Cửa Hàng (Shop)
CREATE TABLE Shop (
    shop_id INT PRIMARY KEY AUTO_INCREMENT,
    owner_user_id INT UNIQUE NOT NULL, 
    shop_name VARCHAR(255) UNIQUE NOT NULL,
    rating DECIMAL(2,1) DEFAULT 0.0,
    is_verified BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (owner_user_id) REFERENCES User(user_id)
);

-- Bảng Danh Mục (Category) - Hỗ trợ đa cấp
CREATE TABLE Category (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(100) UNIQUE NOT NULL,
    parent_id INT,
    FOREIGN KEY (parent_id) REFERENCES Category(category_id) 
);

-- Bảng Sản Phẩm (Product)
CREATE TABLE Product (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    shop_id INT NOT NULL,
    category_id INT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    description TEXT,
    base_price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (shop_id) REFERENCES Shop(shop_id),
    FOREIGN KEY (category_id) REFERENCES Category(category_id)
);

-- Bảng 6: Định nghĩa Trục Biến Thể (Variants) - Ví dụ: 'Màu sắc', 'Kích cỡ'
CREATE TABLE variants (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) UNIQUE NOT NULL 
);

-- Bảng 7: Định nghĩa Giá Trị Biến Thể (Variant Values) - Ví dụ: 'Đỏ', 'S', 'M'
CREATE TABLE variant_values (
    id INT PRIMARY KEY AUTO_INCREMENT,
    variant_id INT NOT NULL, 
    value VARCHAR(100) NOT NULL, 
    image VARCHAR(255), 
    FOREIGN KEY (variant_id) REFERENCES variants(id)
);

-- Bảng 8: Tồn Kho, Giá & SKU (Product Variant Values) - LƯU Ý: Chứa SKU, Stock, Price
CREATE TABLE product_variant_values (
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    
    sku VARCHAR(50) UNIQUE NOT NULL, -- Mã SKU duy nhất cho biến thể này
    stock INT NOT NULL DEFAULT 0, -- Số lượng tồn kho
    price DECIMAL(10,2) NOT NULL, -- Giá bán hiện tại
    old_price DECIMAL(10,2), -- Giá cũ (để hiển thị khuyến mãi)
    
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

-- Bảng 10: Đơn Hàng (Order)
CREATE TABLE `Order` (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    shipping_address_id INT NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'SHIPPING', 'DELIVERED', 'CANCELLED') NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (shipping_address_id) REFERENCES Address(address_id)
);

-- Bảng 11: Chi Tiết Đơn Hàng (OrderDetail)
CREATE TABLE OrderDetail (
    order_detail_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    -- Tham chiếu đến ID của dòng tồn kho/giá mới
    product_variant_value_id INT NOT NULL, 
    quantity INT NOT NULL,
    price_per_unit DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES `Order`(order_id),
    -- Khóa ngoại đã được cập nhật để tham chiếu bảng tồn kho/giá mới
    FOREIGN KEY (product_variant_value_id) REFERENCES product_variant_values(id), 
    UNIQUE KEY (order_id, product_variant_value_id)
);

-- Bảng 12: Thanh Toán (Payment)
CREATE TABLE Payment (
    payment_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT UNIQUE NOT NULL, 
    payment_method ENUM('COD', 'CARD', 'E-WALLET', 'BANK_TRANSFER') NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    transaction_status ENUM('SUCCESS', 'FAILED', 'PENDING') NOT NULL,
    FOREIGN KEY (order_id) REFERENCES `Order`(order_id)
);
-- Bảng 13: Đánh Giá (Review)
CREATE TABLE Review (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    order_detail_id INT UNIQUE NOT NULL,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    comment TEXT,
    FOREIGN KEY (order_detail_id) REFERENCES OrderDetail(order_detail_id),
    FOREIGN KEY (user_id) REFERENCES User(user_id),
    FOREIGN KEY (product_id) REFERENCES Product(product_id)
);

-- Bảng 14: Voucher
CREATE TABLE Voucher (
    voucher_id INT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) UNIQUE NOT NULL,
    discount_type ENUM('PERCENTAGE', 'FIXED_AMOUNT', 'FREE_SHIPPING') NOT NULL,
    discount_value DECIMAL(10,2) NOT NULL,
    min_spend DECIMAL(10,2) DEFAULT 0.00
);

-- Bảng 15: Giỏ Hàng (Cart)
CREATE TABLE Cart (
    cart_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT UNIQUE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES User(user_id)
);

-- Bảng 16: Chi Tiết Giỏ Hàng (CartItem)
CREATE TABLE CartItem (
    cart_item_id INT PRIMARY KEY AUTO_INCREMENT,
    cart_id INT NOT NULL,
    -- Tham chiếu đến ID của dòng tồn kho/giá mới
    product_variant_value_id INT NOT NULL, 
    quantity INT NOT NULL CHECK (quantity > 0),
    FOREIGN KEY (cart_id) REFERENCES Cart(cart_id),
    -- Khóa ngoại đã được cập nhật để tham chiếu bảng tồn kho/giá mới
    FOREIGN KEY (product_variant_value_id) REFERENCES product_variant_values(id), 
    UNIQUE KEY (cart_id, product_variant_value_id)
);

-- Bảng 17: EAV - Định nghĩa Tên Thuộc tính (AttributeKey)
CREATE TABLE AttributeKey (
    attribute_key_id INT PRIMARY KEY AUTO_INCREMENT,
    key_name VARCHAR(100) UNIQUE NOT NULL 
);

-- Bảng 18: EAV - Liên kết Giá trị thuộc tính với Sản phẩm (ProductAttributeValue)
CREATE TABLE ProductAttributeValue (
    pav_id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT NOT NULL,
    attribute_key_id INT NOT NULL,
    value_text VARCHAR(255), 
    
    FOREIGN KEY (product_id) REFERENCES Product(product_id),
    FOREIGN KEY (attribute_key_id) REFERENCES AttributeKey(attribute_key_id),
    UNIQUE KEY (product_id, attribute_key_id)
);
