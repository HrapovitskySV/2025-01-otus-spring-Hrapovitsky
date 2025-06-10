insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3'), ('Author_4');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3), ('BookTitle_4', null);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);


insert into comments(comment, book_id)
values ('comment_1_1', 1), ('comment_1_2', 1), ('comment_2_1', 2), ('comment_3_1', 3);


INSERT INTO users(id, Username, Password)
  VALUES (1, 'USER', '$2a$12$3xhExkLmROmGyfVAAFq16.UWFVJx3qEUBK9Lxe1Is.X3geAs9ttvC'), (2, 'ADMIN','2');

INSERT INTO roles(id, name)
  VALUES (1, 'ROLE_USER'), (2, 'ROLE_ADMIN');

  insert into users_roles(user_id, role_id)
  values (1, 1),   (2, 2);