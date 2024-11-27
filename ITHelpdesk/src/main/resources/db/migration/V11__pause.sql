drop table if exists pause;

create table pause (
      id BIGSERIAL PRIMARY KEY,
      version BIGINT,
      name varchar(50) UNIQUE,
      minuten int,
      created_date timestamp,
      last_modified_date timestamp);


