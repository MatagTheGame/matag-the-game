create table matag_user
(
    id            bigserial primary key,
    username      varchar(25)  not null unique,
    password      varchar(255) not null unique,
    email_address varchar(100) not null unique,
    status        varchar(20)  not null,
    type          varchar(20)  not null,
    created_at    timestamp    not null default current_timestamp
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
    created_at timestamp   not null default current_timestamp,
    type       varchar(20) not null,
    status     varchar(20) not null,
    result     varchar(20)
);

create table game_session
(
    id             bigserial primary key,
    game_id        bigint   not null,
    session_id     char(36) unique,
    player_id      bigint   not null,
    player_options varchar(255),
    foreign key (game_id) references game (id) on delete set null,
    foreign key (session_id) references matag_session (id) on delete set null,
    foreign key (player_id) references matag_user (id) on delete set null
)