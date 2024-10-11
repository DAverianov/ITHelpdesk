drop table if exists log;
drop table if exists log_seq;

create table log (
      id SERIAL PRIMARY KEY,
      version int,
      
      user_id SERIAL,
      event varchar(50),
      description varchar(300),
      
      created_date timestamp,
      last_modified_date timestamp
);