drop table if exists email;

create table email (
      id BIGSERIAL PRIMARY KEY,
      version BIGINT,
      
      sender varchar(120),
      recipient varchar(120),
      subject varchar(120),
      text varchar,
      author_id int,
      date_to_senden timestamp,
      boundary_date timestamp,
      sent_date timestamp,
      sent boolean,
      
      created_date timestamp,
      last_modified_date timestamp,
      constraint email_user_spring_author_id_fk foreign key (author_id) references user_spring(id)
);

