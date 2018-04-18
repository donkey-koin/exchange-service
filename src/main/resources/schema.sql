-- CREATE SCHEMA exchange;

create table users (
  id       serial primary key not null,
  username varchar(50)        not null,
  password varchar(60)        not null,
  email    varchar(50)        not null
);

create table transactions (
  id      SERIAL primary key not null,
  user_id integer            not null REFERENCES users (id)
)