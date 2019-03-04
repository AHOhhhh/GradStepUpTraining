drop table if exists tw_user;
create table tw_user(

   id bigint(20) primary key auto_increment,
   name varchar(20),
   password varchar(100),
   role varchar(20)
) default charset utf8;