-- valid for postgreSQL version > 10

DROP DATABASE IF EXISTS usersdb;
CREATE DATABASE usersdb;

DROP TABLE IF EXISTS users;

CREATE TABLE users(
    user_id int PRIMARY KEY,
    first_name text,
    last_name text,
    email text
);

insert into users(user_id, first_name, last_name, email) values(1, 'sai', 'padala', 'abc@y.com');
insert into users(user_id, first_name, last_name, email) values(2, 'john', 'doe', 'doe@xy.com');
insert into users(user_id, first_name, last_name, email) values(3, 'casper', 'singh', 'casper@y.com');
