CREATE SCHEMA exchange;

create table users (
  id       numeric primary key not null,
  username varchar(50)         not null,
  password varchar(50)         not null,
  email    varchar(50)         not null
)