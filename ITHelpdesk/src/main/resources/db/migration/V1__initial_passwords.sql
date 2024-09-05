drop table if exists passwords;
drop table if exists passwords_seq;

create table passwords (
      id SERIAL PRIMARY KEY,
      version int,
      name varchar(120),
      password varchar(200),
      created_date timestamp,
      last_modified_date timestamp
);
