-- CREATE SCHEMA exchange;

CREATE TABLE USERS (
  ID       SERIAL primary key not null,
  USERNAME VARCHAR(50)        not null,
  PASSWORD VARCHAR(60)        not null,
  EMAIL    VARCHAR(50)        not null
);

CREATE UNIQUE INDEX USERS_USERNAME_IDX
  ON USERS (USERNAME);

CREATE TABLE TRANSACTIONS (
  ID                    SERIAL primary key not null,
  USER_ID               integer            not null REFERENCES USERS (ID),
  TRANSACTION_TIMESTAMP date               not null
)