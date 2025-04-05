create table if not exists user_profiles(
      user_id int primary key not null auto_increment,
      username varchar(64) unique not null,
      password varchar(256) not null
);

create table if not exists games(
    game_id int primary key not null,
    title varchar(256) unique not null,
    positive_review_percentage float not null,
    metacritic_score float default 0.0,
    price float default 0.0
);

create table if not exists game_tags(
    game_id int not null,
    tag varchar(256) not null,
    constraint game_to_tags foreign key(game_id) references games(game_id)
        on delete CASCADE
        on update CASCADE
);

create table if not exists user_games(
       user_id int not null,
       game_id int not null,
       time_played float not null,
       rating float default 0.0,
       review enum('positive', 'none', 'negative') default 'none',
       score float default 0.0,
       constraint player_to_user_profile foreign key(user_id) references user_profiles(user_id)
           on delete CASCADE
           on update CASCADE,
       constraint game_to_user_games foreign key(game_id) references games(game_id)
           on delete CASCADE
           on update CASCADE
);