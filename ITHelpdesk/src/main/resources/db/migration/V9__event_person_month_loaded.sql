drop table if exists event_person_month_loaded;

create table event_person_month_loaded (
      id BIGSERIAL PRIMARY KEY,
      version BIGINT,
      event varchar(50),
      person_id bigint,
      month date,
      created_date timestamp,
      last_modified_date timestamp,
      unique(event, person_id, month),
      constraint event_person_month_loaded_person_id_fk foreign key (person_id) references person(id)
);

alter table time_register_event
add month date;

delete from time_register_event;

