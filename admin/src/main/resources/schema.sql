create type mtg_user_status as enum ('ACTIVE', 'INACTIVE', 'VERIFYING');

create table mtg_user (
  id serial primary key,
  username varchar(25) not null unique,
  password varchar(255) not null unique,
  email_address varchar(100) not null unique,
  status mtg_user_status,
  created_at timestamp not null default current_timestamp
);

create table mtg_session (
  id char(35) primary key,
  mtg_user_id bigint not null,
  created_at timestamp not null default current_timestamp,
  valid_until timestamp not null,
  foreign key (mtg_user_id) references mtg_user(id) on delete cascade
);
