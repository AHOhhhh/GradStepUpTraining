
drop table if exists product;
create table product(
 id bigint(20) primary key auto_increment,
 name varchar(100) not null,
 price double(16,2),
 unit varchar(20),
 img varchar(100)
)  default charset utf8;