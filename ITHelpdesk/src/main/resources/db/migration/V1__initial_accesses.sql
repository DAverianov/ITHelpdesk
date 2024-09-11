drop table if exists accesses;
drop table if exists accesses_seq;

create table accesses (
      id SERIAL PRIMARY KEY,
      version int,
      name varchar(120),
      domain varchar(60),
      user_ varchar(120),
      password varchar(200),
      description varchar(200),
      created_date timestamp,
      last_modified_date timestamp
);
