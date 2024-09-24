drop table if exists person;
drop table if exists person_seq;

create table person (
      id SERIAL PRIMARY KEY,
      version int,
      name varchar(120),
      name_for_search varchar(120),
      bc_code varchar(4),
      created_date timestamp,
      last_modified_date timestamp
);
