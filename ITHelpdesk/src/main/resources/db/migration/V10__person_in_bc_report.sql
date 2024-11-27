drop table if exists person_in_bc_report_month;

create table person_in_bc_report_month (
      id BIGSERIAL PRIMARY KEY,
      version BIGINT,
      person_id bigint,
      month date,
      attribute varchar,
      date_table varchar,
      saldo varchar,
      created_date timestamp,
      last_modified_date timestamp,
      unique(person_id, month),
      constraint event_person_month_loaded_person_id_fk foreign key (person_id) references person(id)
);

CREATE INDEX person_in_bc_report_month_index_personid
    ON public.person_in_bc_report_month USING btree
    (person_id ASC NULLS LAST);

