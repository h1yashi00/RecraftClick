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
     human_kills    int default 0,
     monster_kills int default 0,
     gun_kills int default 0,
     melee_kills int default 0,
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


create table if not exists item_type (
    item_type_id smallint not null auto_increment,
    name varchar(20) not null,
    primary key (item_type_id)
);

insert into item_type(name) values
    ('main'),
    ('sub'),
    ('melee'),
    ('skill');


create table if not exists zombie_hero_item (
    zombie_hero_item_id smallint not null auto_increment,
    item_type_id smallint not null,
    name varchar(20) not null,
    foreign key (item_type_id) references item_type(item_type_id),
    primary key (zombie_hero_item_id)
);

insert into zombie_hero_item(item_type_id, name) values
    (1, 'ak47'),
    (1, 'mp5'),
    (1, 'mosin'),
    (1, 'awp'),
    (1, 'm870'),
    (1, 'saiga'),
    (2, 'glock'),
    (2, 'desert_eagle'),
    (3, 'nata'),
    (3, 'hammer'),
    (4, 'ammo_dump'),
    (4, 'grenade'),
    (4, 'zombie_grenade'),
    (4, 'zombie_hit_grenade');

create table if not exists player_zombiehero_unlocked_item (
    player_uuid uuid,
    item_type_id smallint not null,
    foreign key (item_type_id) references item_type(item_type_id),
    primary key (player_uuid)
);

create table if not exists player_zombiehero_choose (
    player_uuid uuid primary key,
    item_main_id smallint not null check((select item_type_id from zombie_hero_item where zombie_hero_item_id = item_main_id) = 1) references item_type(item_type_id)
--    item_gun_sub_id  smallint not null references item_type(item_type_id),
--    item_melee_id smallint not null references item_type_id(item_type_id),
--    item_skill_id smallint not null references item_type_id(item_type_id)
);