create table category(
    id serial primary key,
    name varchar(100) not null
);

create table song(
    id serial primary key,
    pathname varchar(200) not null,
    name varchar(100) not null,
    points integer default 0
);

create table category_song(
    category_id integer not null,
    foreign key (category_id) references category(id),
    song_id integer not null,
    foreign key (song_id) references song(id),
    primary key(category_id, song_id)
);
