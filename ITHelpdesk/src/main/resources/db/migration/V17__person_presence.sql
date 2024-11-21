drop table if exists person_presence;

create table person_presence (
      id BIGSERIAL PRIMARY KEY,
      version BIGINT,
      person_id bigint unique,
      presence boolean,
      arrival_time varchar(12),
      
      created_date timestamp,
      last_modified_date timestamp,
      constraint person_presence_person__person_id_fk foreign key (person_id) references person(id)
);

