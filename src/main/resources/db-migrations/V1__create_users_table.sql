CREATE SEQUENCE user_id_seq START 1;

CREATE TABLE usr (
    id bigserial primary key,
    email varchar(100) NOT NULL,
    first_name varchar(100) NOT NULL,
    last_name varchar(100) NOT NULL,
    birthday_date date,
    bio varchar(1000)
);