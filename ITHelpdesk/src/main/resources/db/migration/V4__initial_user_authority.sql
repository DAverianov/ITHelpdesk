drop table if exists user_spring;
drop table if exists user_spring_seq;

create table user_spring (
      id SERIAL PRIMARY KEY,
      version int,
      username varchar(120),
      password varchar(200),
      account_non_expired boolean,
      account_non_locked boolean,
      credentials_non_expired boolean,
      enabled boolean
);

drop table if exists authority;
drop table if exists authorityr_seq;

create table authority (
      id SERIAL PRIMARY KEY,
      version int,
      role varchar(120)
);

drop table if exists user_spring_authority;
drop table if exists user_spring_authority_seq;

create table user_spring_authority (
      user_spring_id INTEGER,
      authority_id INTEGER,
      primary key(user_spring_id, authority_id),
      constraint pc_user_spring_id_fk foreign key (user_spring_id) references user_spring(id),
      constraint pc_authority_id_fk foreign key (authority_id) references authority(id)
);
