drop table if exists  `tw_order`;
create table `tw_order`(
	id bigint(20) primary key auto_increment,
	order_time datetime
)  default charset utf8;


drop table if exists  order_item;
create table order_item(
	id bigint(20) primary key auto_increment,
	order_id bigint(20),
	product_id bigint(20),
	product_count int
) default charset utf8;
