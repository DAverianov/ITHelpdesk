drop table if exists time_register_event;
drop table if exists time_register_event_seq;

create table time_register_event (
      id SERIAL PRIMARY KEY,
      version int,
      
      person_id SERIAL,
      event_date timestamp,
      start_date timestamp,
      end_date timestamp,
      
      created_date timestamp,
      last_modified_date timestamp
);