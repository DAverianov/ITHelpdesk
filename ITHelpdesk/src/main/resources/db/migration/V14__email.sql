drop table if exists email_account;

create table email_account (
      id BIGSERIAL PRIMARY KEY,
      version BIGINT,
      email varchar,
      host varchar,
      port int,
      username varchar,
      access_id bigint,
      outgoing_protocol varchar,
      smtp_auth varchar,
      smtp_starttls_enable varchar,
      description varchar,
      
      created_date timestamp,
      last_modified_date timestamp,
      constraint email_account_access_access_id_fk foreign key (access_id) references accesses(id)

);

drop table if exists email;

create table email (
      id BIGSERIAL PRIMARY KEY,
      version BIGINT,
      
      sender_id bigint,
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
      constraint email_email_account_sender_id_fk foreign key (sender_id) references email_account(id),
      constraint email_user_spring_author_id_fk foreign key (author_id) references user_spring(id)
);

