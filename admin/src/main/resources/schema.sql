create type matag_user_status as enum ('ACTIVE', 'INACTIVE', 'VERIFYING');

create table matag_user (
  id serial primary key,
  username varchar(25) not null unique,
  password varchar(255) not null unique,
  email_address varchar(100) not null unique,
  status matag_user_status,
  created_at timestamp not null default current_timestamp
);

create table matag_session (
  id char(36) primary key,
  matag_user_id bigint not null,
  created_at timestamp not null default current_timestamp,
  valid_until timestamp not null,
  foreign key (matag_user_id) references matag_user(id) on delete cascade
);
