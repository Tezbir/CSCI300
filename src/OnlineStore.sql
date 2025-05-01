CREATE TABLE online_store.Employees(
	employee_id int AUTO_INCREMENT,
	employee_name varchar(255),
	employee_username varchar(255),
    employee_password varchar(255),
    employee_tempPassword varchar(255),
    PRIMARY KEY (employee_id)
    );
INSERT INTO online_store.Employees (employee_id, employee_name, employee_username,  employee_password, employee_tempPassword)
VALUES (1, 'John Smith' ,'JayS', 'Password', 'hey');

CREATE TABLE online_store.Items(
	item_id int AUTO_INCREMENT,
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
	code_id int AUTO_INCREMENT,
    discount_percent DECIMAL(5,2) CHECK (discount_percent >= 0 AND discount_percent <= 100),
	created_by int,
    PRIMARY KEY (code_id),
	FOREIGN KEY (created_by) REFERENCES Employees(employee_id)
);
CREATE TABLE online_store.Orders (
     order_id int AUTO_INCREMENT,
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

SELECT * FROM online_store.Employees;
SELECT * FROM online_store.Items;
SELECT * FROM online_store.Coupons;
SELECT * FROM online_store.Customers;


INSERT INTO online_store.Customers (customer_name, customer_username, customer_password, customer_address)
VALUES ('Melissa Smith', 'msmith', 'pass123', '123 Line St');


INSERT INTO online_store.Orders (order_id, customer_id, order_date_time, delivery_date, coupon_code)
VALUES (1, 1,'2024-04-30 12:00:00', '2024-05-14', 6545);

SELECT * FROM online_store.Orders;
SELECT * FROM online_store.OrderOfItems;

INSERT INTO online_store.OrderOfItems (order_id, item_id, quantity, item_status)
VALUES (1, 1, 2, 'pending');

INSERT INTO online_store.Customers (customer_name, customer_username, customer_password, customer_address)
VALUES ('Kyle Martin', 'kylem', 'martin345', '123 Line St');

INSERT INTO online_store.Orders (order_id, customer_id, order_date_time, delivery_date, coupon_code)
VALUES (2, 2,'2025-04-27 5:30:00', '2024-05-01', null);

INSERT INTO online_store.OrderOfItems (order_id, item_id, quantity, item_status)
VALUES (2, 3, 4, 'pending');

INSERT INTO online_store.Customers (customer_name, customer_username, customer_password, customer_address)
VALUES ('Nicole Martinez', 'niki', 'niki345', '123 Line St');

INSERT INTO online_store.Orders (order_id, customer_id, order_date_time, delivery_date, coupon_code)
VALUES (3, 3,'2025-04-28 7:45:00', '2024-05-10', 5643);

UPDATE online_store.Orders
SET delivery_date = '2025-04-30'
WHERE order_id = 3;

INSERT INTO online_store.OrderOfItems (order_id, item_id, quantity, item_status)
VALUES (3, 4, 1, 'completed');
Create Table online_store.admins(
admin_id int,
admin_name varchar(255),
admin_username varchar(255),
admin_password varchar(255),
admin_address varchar(255),
Primary Key(admin_id));
insert into
values(1,'Admin admin','admin','admin','1 Ronkonkoma Avenue, Lake Ronkonkoma, New York');
insert into
values(2,'Tezbir Singh','tezbir','Ribzet','10 Spring Yard, Lake Ronkonkoma, New York');

CREATE TABLE online_store.customers (
    customer_id int,
    customer_name varchar(255),
    customer_username varchar(255),
    customer_password varchar(255),
    customer_address varchar(255),
    PRIMARY KEY (customer_id));
ALTER TABLE customers AUTO_INCREMENT = 1;

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

CREATE TABLE online_store.shipments (
    shipment_id INT,
    order_id INT,
    customer_id INT,
    PRIMARY KEY (shipment_id),
    FOREIGN KEY (order_id) REFERENCES admin.orders(order_id),
    FOREIGN KEY (customer_id) REFERENCES admin.customers(customer_id));

 CREATE TABLE online_store.Items(
 	item_id int,
     item_name varchar(255),
     item_price DECIMAL(10,2),
     quantity_in_stock int,
     created_by int,
     PRIMARY KEY (item_id),
 	FOREIGN KEY (created_by) REFERENCES Employees(employee_id)
     );
INSERT INTO online_store.Items (item_id, item_name, item_price, quantity_in_stock,created_by)
VALUES (100, 'Skirt', 39.99, 20,'Mom and Pop Shop');

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

 CREATE TABLE online_store.OrderOfItems (
 	order_id int,
 	item_id int,
     quantity int,
     item_status varchar(255),
     PRIMARY KEY (order_id, item_id),
     FOREIGN KEY (order_id) REFERENCES Orders(order_id),
     FOREIGN KEY (item_id) REFERENCES Items(item_id)
 );

CREATE TABLE online_store.cart (
    customer_id INT AUTO_INCREMENT,
    item_name VARCHAR(255),
    item_price DECIMAL(10,2),
    item_quantity INT,
    PRIMARY KEY (customer_id, item_name),
    FOREIGN KEY (customer_id) REFERENCES online_store.Customers(customer_id)
);
DESCRIBE online_store.cart;
ALTER TABLE online_store.Orders ADD COLUMN total_price DECIMAL(10,2);
