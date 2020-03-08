create type mtg_user_status as enum ('ACTIVE', 'INACTIVE', 'VERIFYING');

create table mtg_user (
  id serial primary key,
  username varchar(25) not null unique,
  password varchar(100) not null unique,
  email_address varchar(100),
  status mtg_user_status
);

create table mtg_session (
  id char(35) primary key,
  mtg_user_id bigint not null,
  valid_until  timestamp not null default current_timestamp,
  foreign key (mtg_user_id) references mtg_user(id) on delete cascade
);
