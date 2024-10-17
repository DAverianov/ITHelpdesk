drop table if exists instruction;
drop table if exists instruction_line;

create table instruction (
      id BIGINT PRIMARY KEY,
      version BIGINT,
      name varchar(120) UNIQUE,
      author varchar(60),
      description varchar(3000),
      created_date timestamp,
      last_modified_date timestamp
);
create table instruction_line (
      id BIGINT PRIMARY KEY,
      version BIGINT,
      instruction_id BIGINT,
      string_nummer int,
      description varchar(3000),
      created_date timestamp,
      last_modified_date timestamp,
      constraint instruction_line_instruction_id_fk foreign key (instruction_id) references instruction(id)
);
