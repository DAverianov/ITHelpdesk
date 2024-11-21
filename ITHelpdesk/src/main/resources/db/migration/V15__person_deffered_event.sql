drop table if exists person_deffered_event;

create table person_deffered_event (
      id BIGSERIAL PRIMARY KEY,
      version BIGINT,
      deffered_event varchar(120),
      person_id bigint,
      user_id int,
      done boolean,
      execution_result varchar,
      
      created_date timestamp,
      last_modified_date timestamp,
      unique(person_id, user_id, deffered_event),
      constraint person_deffered_event_person__person_id_fk foreign key (person_id) references person(id),
      constraint person_deffered_event_user_id_fk foreign key (user_id) references user_spring(id)
);

