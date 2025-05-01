CREATE TABLE online_store.Employees(
	employee_id int,
	employee_name varchar(255),
	employee_username varchar(255),
    employee_password varchar(255),
    employee_tempPassword varchar(255),
    PRIMARY KEY (employee_id)
    );
CREATE TABLE online_store.Items(
	item_id int,
    item_name varchar(255),
    item_price DECIMAL(10,2),
    quantity_in_stock int,
    created_by int,
    PRIMARY KEY (item_id),
	FOREIGN KEY (created_by) REFERENCES Employees(employee_id)
    );
CREATE TABLE online_store.Customers (
    customer_id int AUTO_INCREMENT,
    customer_name varchar(255),
    customer_username varchar(255),
    customer_password varchar(255),
    customer_address varchar(255),
    PRIMARY KEY (customer_id)
    );

CREATE TABLE online_store.Coupons(
	code_id int,
    discount_percent DECIMAL(5,2) CHECK (discount_percent >= 0 AND discount_percent <= 100),
	created_by int,
    PRIMARY KEY (code_id),
	FOREIGN KEY (created_by) REFERENCES Employees(employee_id)
);
CREATE TABLE online_store.Orders (
     order_id int,
	 customer_id int,
     order_date_time DATETIME,
     delivery_date DATE,
     coupon_code int,
     PRIMARY KEY (order_id),  
	 FOREIGN KEY (customer_id) REFERENCES Customers(customer_id),
	 FOREIGN KEY (coupon_code) REFERENCES Coupons(code_id)
);
CREATE TABLE online_store.OrderOfItems (
	order_id int,
	item_id int,
    quantity int,
    item_status varchar(255),
    PRIMARY KEY (order_id, item_id),
    FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (item_id) REFERENCES Items(item_id)
);
