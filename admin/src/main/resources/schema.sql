create type mtg_user_status as enum ('ACTIVE', 'INACTIVE', 'VERIFYING');

create table mtg_user (
  id serial primary key,
  username varchar(25),
  password varchar(100),
  email_address varchar(100),
  status mtg_user_status
);

create unique index mtg_user_username_unique on mtg_user(username);
create unique index mtg_user_email_address_unique on mtg_user(email_address);
