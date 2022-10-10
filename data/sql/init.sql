create database if not exists recraft character set utf8 collate utf8_general_ci;

use recraft

create table if not exists  staff_rank (
     rank_id smallint NOT NULL AUTO_INCREMENT,
     name CHAR(30) NOT NULL,
     PRIMARY KEY (rank_id)
);

insert into staff_rank (name) values
    ('None'),
    ('Owner'),
    ('Mapper'),
    ('Developer'),
    ('Manager'),
    ('YouTuber');

create table if not exists player_data (
    player_uuid  uuid not null,
    current_name varchar(16) not null,
    first_join   timestamp not null default current_timestamp,
    last_join    timestamp not null default current_timestamp,
    last_logout  timestamp not null default current_timestamp,
    rank_id      smallint  not null default 1,
    primary key (player_uuid),
    foreign key (rank_id) references staff_rank(rank_id)
);

create table punish_reason (
    punish_reason_id smallint not null auto_increment,
    reason  varchar(20) not null,
    primary key(punish_reason_id)
);

insert into punish_reason(reason) values
    ('Unfair advantage'),
    ('Chat Abuse'),
    ('Compromised Account');

create table if not exists player_punish_history (
    player_punish_history_id int unsigned auto_increment,
    player_uuid         uuid,
    punish_reason_id    smallint not null,
    since timestamp     default current_timestamp,
    duration  smallint default -1,
    canceled smallint   default 0,
    who_banned uuid not null,
    primary key (player_punish_history_id, player_uuid),
    foreign key (punish_reason_id) references punish_reason(punish_reason_id)
);

create table if not exists player_zombiehero_stats (
     player_uuid    uuid not null,
     coin           int default 0,
     times_played   int default 0,
     wins           int default 0,
     loses          int default 0,
     monster_kills int default 0,
     human_kills    int default 0,
     primary key (player_uuid)
);

create table if not exists player_zombiehero_quest (
    player_uuid uuid,
    daily_quest_kills  int default 0,
    daily_quest_mined  int default 0,
    daily_quest_played int default 0,
    daily_quest_kills_selected boolean default false,
    daily_quest_mined_selected boolean default false,
    daily_quest_played_selected boolean default false,

    weekly_quest_kills  int default 0,
    weekly_quest_mined  int default 0,
    weekly_quest_played int default 0,
    weekly_quest_selected boolean default false,
    primary key (player_uuid)
);

create table if not exists player_option (
    player_uuid uuid,
    auto_load_resource_pack boolean default true,
    primary key (player_uuid)
);