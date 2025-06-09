create table authors (
    id bigserial,
    full_name varchar(255),
    primary key (id)
);

create table genres (
    id bigserial,
    name varchar(255),
    primary key (id)
);

create table books (
    id bigserial,
    title varchar(255),
    author_id bigint references authors (id) on delete cascade,
    primary key (id)
);

create table books_genres (
    book_id bigint references books(id) on delete cascade,
    genre_id bigint references genres(id) on delete cascade,
    primary key (book_id, genre_id)
);

create table comments (
    id bigserial,
    book_id bigint references books(id) on delete cascade,
    comment varchar(255),
    primary key (id)
);

create table users (
    id bigserial,
    Username varchar(20),
    Password varchar(100),
    primary key (id)
);

create table roles (
    id bigserial,
    name varchar(100),
    primary key (id)
);


create table users_roles (
    user_id bigint references users(id) on delete cascade,
    role_id bigint references roles(id) on delete cascade,
    primary key (user_id, role_id)
);