drop table if exists person_deffered_event;

create table person_deffered_event (
      id BIGSERIAL PRIMARY KEY,
      version BIGINT,
      deffered_event varchar,
      person_id bigint,
      user_id int,
      done boolean,
      execution_result varchar,
      email_id bigint,
      
      created_date timestamp,
      last_modified_date timestamp,
      constraint person_deffered_event_person__person_id_fk foreign key (person_id) references person(id),
      constraint person_deffered_event_user_id_fk foreign key (user_id) references user_spring(id),
      constraint person_deffered_event_email_id_fk foreign key (email_id) references email(id)
);

