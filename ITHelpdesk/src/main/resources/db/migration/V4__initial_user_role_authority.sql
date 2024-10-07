drop table if exists user_spring_authority;
drop table if exists user_spring_role;
drop table if exists role_authority;
drop table if exists user_spring;
drop table if exists role;
drop table if exists authority;

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

create table role (
      id SERIAL PRIMARY KEY,
      version int,
      name varchar(120)
);

create table authority (
      id SERIAL PRIMARY KEY,
      version int,
      permission varchar(120)
);

create table user_spring_role (
      user_spring_id INTEGER,
      role_id INTEGER,
      primary key(user_spring_id, role_id),
      constraint pc_user_spring_id_fk foreign key (user_spring_id) references user_spring(id),
      constraint pc_role_id_fk foreign key (role_id) references role(id)
);

create table role_authority (
      role_id INTEGER,
      authority_id INTEGER,
      primary key(role_id, authority_id),
      constraint pc_role_id_fk foreign key (role_id) references role(id),
      constraint pc_authority_id_fk foreign key (authority_id) references authority(id)
);
