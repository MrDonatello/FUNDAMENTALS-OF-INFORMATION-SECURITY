DROP DATABASE IF EXISTS signature;
CREATE DATABASE `signature`; 
USE `signature`;

CREATE TABLE user (
 id  int (11) not null auto_increment,
 login varchar(50) not null,
 password varchar (50) not null collate utf8_bin,
 firstName varchar(50) not null,
 lastName varchar (50) not null,
 patronymic varchar (50),
 role ENUM ('ADMIN', 'CLIENT'),
 primary key (id),
 unique key user (login)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;
 

 CREATE TABLE admin_access (
 id int (11) not null auto_increment,
 admin_code int (11) not null default 777,
 primary key (id)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE admin (
 userid int (11) not null,
 position varchar (50) not null,
 primary key (userid),
 foreign key(userid) references user(id) on delete cascade
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;
 
 CREATE TABLE client (
 userid int (11) not null,
 email varchar(50) not null,
 address varchar (50) not null,
 phone varchar (50) not null,
 public_key varchar (50) not null,
 foreign key(userid) references user(id) on delete cascade,
 primary key (userid),
 key (userid)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;

 CREATE TABLE session (
 id  int (11) not null auto_increment,
 userid int (11) not null,
 javasessionid varchar (50),
  primary key (id)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;