create table tw_user (
  id bigint(20) primary key,
  username varchar(50) not null,
  password varchar(100) not null,
  fullname varchar(50),
  role varchar(50) not null
) default charset utf8;

insert into tw_user (id, username, password, fullname, role) values (
30001, "zhangsan", "$2a$10$qjpwHbXtOx1.Xx4M9STsou75C1X18sKWfc/1lLiBwsdRCN.FJvrIW", "张三", "ADMIN"
);