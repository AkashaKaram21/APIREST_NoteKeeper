drop table if exists users;

create table users (
    id bigint auto_increment primary key,
    name varchar(100) not null,
    email varchar(100) not null unique,
    password varchar(100) not null,
    ultim_acces timestamp,
    data_created timestamp,
    data_updated timestamp
);