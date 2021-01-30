DROP DATABASE IF EXISTS `Storage_Management`;
CREATE DATABASE IF NOT EXISTS `Storage_Management`;
USE `Storage_Management`;

CREATE TABLE `user`(
	`id` 			int(11) not null auto_increment,
    `username` 		varchar(50)	not null, 
    `password` 		varchar(50) not null,
    `name` 			varchar(100) not null,
    `email` 		varchar(100) null,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE STATUS = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
  
CREATE TABLE `user_role`(
	`id` 			int(11) not null auto_increment,
	`user_id`		int(11) not null,
	`role_id`		int(11) not null,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE active_flag = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
ALTER TABLE `user_role` ADD CONSTRAINT `userId_foreign_key` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) 
ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE `user_role` ADD CONSTRAINT `roleId_foreign_key` FOREIGN KEY (`role_id`) REFERENCES `role`(`id`)
ON DELETE RESTRICT ON UPDATE CASCADE;

  
CREATE TABLE `role`(
	`id` 			int(11) not null auto_increment,
	`role_name` 	varchar(50) not null,
    `description` 	varchar(200) null,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE active_flag = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
  
CREATE TABLE `auth`(
	`id` 			int(11) not null auto_increment,
	`role_id`		int(11) not null,
	`menu_id`		int(11) not null,
    `permission` 	int(1) not null default '1', /*Duoc phep truy cap = 1, khong duoc phep truy cap = 0*/
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE active_flag = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
ALTER TABLE `auth` ADD CONSTRAINT `roleId_auth_foreign_key` FOREIGN KEY (`role_id`) REFERENCES `role`(`id`)
ON DELETE RESTRICT ON UPDATE CASCADE;
ALTER TABLE `auth` ADD CONSTRAINT `menuId_auth_foreign_key` FOREIGN KEY (`menu_id`) REFERENCES `menu`(`id`) 
ON DELETE RESTRICT ON UPDATE CASCADE;
  
CREATE TABLE `menu`(
	`id` 			int(11) not null auto_increment,
	`parent_menu_id` int(11) not null,				/* == -1 thi khong show ra, > -1 thi show ra menu*/
	`url`			varchar(100) not null,
    `name` 			varchar(100) not null,
    `order_index` 	int(1) not null default '0',
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE active_flag = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
  
CREATE TABLE `category`(
	`id` 			int(11) not null auto_increment,
	`name` 			varchar(100) not null,
	`code`			varchar(50) not null,
    `description`	text,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE active_flag = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
  
CREATE TABLE `product_in_stock`(
	`id` 			int(11) not null auto_increment,
	`product_id` 	int(11) not null ,
	`quantity`		int(11) not null,
    `price` 		decimal(15, 2) not null,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE active_flag = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
ALTER TABLE `product_in_stock` ADD CONSTRAINT `productId_foreign_key` FOREIGN KEY (`product_id`) REFERENCES `product_info`(`id`) 
ON DELETE RESTRICT ON UPDATE CASCADE;
  
CREATE TABLE `product_info`(
	`id` 			int(11) not null auto_increment,
	`category_id` 	int(11) not null ,
    `name`			varchar(100) not null,
	`code`			varchar(50) not null,
    `description` 	text,
    `image_url` 	varchar(200) null,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE active_flag = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
ALTER TABLE `product_info` ADD CONSTRAINT `categoryId_foreign_key` FOREIGN KEY (`category_id`) REFERENCES `category`(`id`) 
ON DELETE RESTRICT ON UPDATE CASCADE;
  
CREATE TABLE `history`(
	`id` 			int(11) not null auto_increment,
	`action_name` 	varchar(100) not null ,
	`type`			int(1) not null,
    `product_id` 	int(11) not null,
    `quantity` 		int(11) not null,
    `price` 		decimal(15, 2) not null,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE active_flag = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
ALTER TABLE `history` ADD CONSTRAINT `productId_history_foreign_key` FOREIGN KEY (`product_id`) REFERENCES `product_info`(`id`) 
ON DELETE RESTRICT ON UPDATE CASCADE;
  
CREATE TABLE `invoice`(
	`id` 			int(11) not null auto_increment,
	`code` 			varchar(50) not null ,
	`type`			int(1) not null,
    `product_id` 	int(11) not null,
    `quantity` 		int(11) not null,
    `price` 		decimal(15, 2) not null,
    
    `active_flag` 	int(2) not null default '1',	/*KHI XOA USER THI TA KHONG XOA KHOI DB MA CHANGE active_flag = 0*/
    `created_date` 	timestamp not null default current_timestamp,
    `updated_date` 	timestamp not null default current_timestamp,
 
    primary key(id)
);
ALTER TABLE `invoice` ADD CONSTRAINT `productId_invoice_foreign_key` FOREIGN KEY (`product_id`) REFERENCES `product_info`(`id`) 
ON DELETE RESTRICT ON UPDATE CASCADE;

-- -------------------------------------------------------------
insert into `user` (id, username, password, name, email) values 
(1, "admin", "123", "Dau xanh", "admin@gmail.com");

-- -------------------------------------------------------------
INSERT INTO `role`(ROLE_NAME, DESCRIPTION) VALUES 
('admin', 'Admin of system'), 
('staff', 'Staff of system');

-- -------------------------------------------------------------
INSERT INTO `user_role`(user_id, role_id) VALUES 
(1, 1);

-- -------------------------------------------------------------
insert into `menu`(parent_menu_id, url, name, order_index) values  
  
(0,'/product','Sản phẩm',1),
(0,'/stock','Kho',2),
(0,'/management','Quản lý',3),

(1,'/product-info/list','Danh sách sản phẩm',2),
(1,'/category/list','Danh sách category',1),
(1,'/category/edit','Sửa',-1),
(1,'/category/view','Xem',-1),
(1,'/category/add','Thêm mới',-1),
(1,'/category/save','Lưu',-1),
(1,'/category/delete','Xoá',-1),

(1,'/product-info/edit','Sửa',-1),
(1,'/product-info/view','Xem',-1),
(1,'/product-info/add','Thêm mới',-1),
(1,'/product-info/save','Lưu',-1),
(1,'/product-info/delete','Xoá',-1),

(2,'/goods-receipt/list','Danh sách nhập kho',1),
(2,'/goods-receipt/view','Xem',-1),
(2,'/goods-receipt/edit','Sửa',-1),
(2,'/goods-receipt/add','Thêm mới',-1),
(2,'/goods-receipt/save','Lưu',-1),
(2,'/goods-receipt/export','Xuất báo cáo',-1),
(2,'/goods-receipt/delete','Xoá',-1),

(2,'/goods-issue/list','Danh sách xuất kho',2),
(2,'/product-in-stock/list','Sản phẩm trong kho',3),
(2,'/history/list','Lịch sử kho',4),

(3,'/user/list','Danh sách user',1),
(3,'/menu/list','Danh sách menu',1),
(3,'/role/list','Danh sách quyền',1),

(3, '/user/save', 'Save', -1),
(3, '/user/edit', 'Edit', -1),
(3, '/user/view', 'View', -1),
(3, '/user/add', 'Add', -1);

ALTER TABLE `auth` DROP FOREIGN KEY `menuId_auth_foreign_key`;
truncate menu;

-- -------------------------------------------------------------
INSERT INTO `auth`(ROLE_ID, MENU_ID, PERMISSION) VALUES 
-- admin co quyen truy cap vao toan bo endpoint trong web
(1,1,1),(1,2,1),(1,3,1),
(1,4,1),(1,5,1),(1,6,1),(1,7,1),(1,8,1),
(1,9,1),(1,10,1),(1,11,1),(1,12,1),(1,13,1),
(1,14,1),(1,15,1),(1,16,1),(1,17,1),
(1,18,1),(1,19,1),(1,20,1),(1,21,1),(1,22,1),
(1,23,1),(1,24,1),(1,25,1),(1,26,1), (1,27,1), (1,28,1),
(1,29,1), (1,30,1),(1,31,1),(1,32,1);

truncate `auth`;

-- ----------------------------------------------------------------------------
USE `Storage_Management`;
select * from user join user_role join role     
on user.id = user_role.user_id && role.id = user_role.role_id;

select * from category;
select * from product_info;
select * from product_in_stock;
select * from menu;productsproductsproducts
select * from invoice;
select * from auth;



select version()
