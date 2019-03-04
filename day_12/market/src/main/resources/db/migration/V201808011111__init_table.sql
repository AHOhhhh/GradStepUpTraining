create table todo_demo.products (
  id bigint(20) primary key AUTO_INCREMENT,
  name varchar(100) not null,
  price float default 0,
  category varchar(100),
  brand varchar(100),
  description varchar(200),
  time date,  
  place varchar(200)
) charset utf8;