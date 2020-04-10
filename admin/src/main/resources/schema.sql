create type matag_user_status as enum ('ACTIVE', 'INACTIVE', 'VERIFYING');
create type matag_user_type as enum ('ADMIN', 'USER', 'GUEST');
create type game_type as enum ('UNLIMITED', 'PRIVATE');
create type game_status_type as enum ('STARTING', 'IN_PROGRESS', 'CANCELLED', 'FINISHED');
create type game_result_type as enum ('R1', 'RX', 'R2');

create table matag_user
(
    id            bigserial primary key,
    username      varchar(25)       not null unique,
    password      varchar(255)      not null unique,
    email_address varchar(100)      not null unique,
    status        matag_user_status not null,
    type          matag_user_type   not null,
    created_at    timestamp         not null default current_timestamp
);

create table matag_session
(
    id            char(36) primary key,
    matag_user_id bigint    not null,
    created_at    timestamp not null default current_timestamp,
    valid_until   timestamp not null,
    foreign key (matag_user_id) references matag_user (id) on delete cascade
);

create table game
(
    id         bigserial primary key,
    created_at timestamp        not null default current_timestamp,
    type       game_type        not null,
    status     game_status_type not null,
    result     game_result_type
);

create table game_session
(
    id              bigserial primary key,
    game            bigint   not null,
    session_num     smallint not null,
    session         char(36) unique,
    session_options varchar(255),
    player          bigint   not null,
    foreign key (session) references matag_session (id) on delete set null,
    foreign key (player) references matag_user (id) on delete set null
)
