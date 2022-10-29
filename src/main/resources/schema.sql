-- drop table if exists friends;
-- drop table if exists films_genres;
-- drop table if exists likes;
-- drop table if exists genres;
-- drop table if exists films;
-- drop table if exists mpa;
-- drop table if exists users;

create table if not exists mpa(
    mpa_id int auto_increment primary key,
    mpa_name varchar(5) not null
);

create table if not exists films
(
    film_id      int auto_increment primary key,
    film_name    varchar(150) not null,
    description  varchar(1000),
    release_date date,
    duration     integer,
    mpa_id       varchar(5),
    constraint films_pk
        primary key (film_id),
    constraint "films_mpa_fk"
        foreign key (mpa_id) references mpa (mpa_id)
);

create table if not exists users
(
    user_id   int auto_increment primary key,
    user_name varchar(50) not null,
    login     varchar(50),
    email     varchar(50),
    birthday  date,
    constraint users_pk
        primary key (user_id)
);

create table if not exists genres
(
    genre_id  int auto_increment primary key,
    genre_name varchar(100) not null,
    constraint genres_pk
        primary key (genre_id)
);

create table if not exists films_genres
(
    film_id  integer not null,
    genre_id integer not null,
    constraint "films_genres_films_fk"
        foreign key (film_id) references FILMS (film_id),
    constraint films_genres_genres_fk
        foreign key (genre_id) references genres (genre_id)
);

create table if not exists likes
(
    film_id integer not null,
    user_id integer not null,
    constraint "likes_films_fk"
        foreign key (film_id) references films (film_id),
    constraint "likes_users_fk"
        foreign key (user_id) references users (user_id)
);

create table if not exists friends
(
    user_id   integer not null,
    friend_id integer not null,
    constraint "friends_users_user_id_fk"
        foreign key (user_id) references users (user_id),
    constraint "friends_users_friend_id_fk"
        foreign key (friend_id) references users (user_id)
);
