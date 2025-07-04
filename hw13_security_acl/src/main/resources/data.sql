insert into authors(full_name)
values ('Author_1'), ('Author_2'), ('Author_3'), ('Author_4');

insert into genres(name)
values ('Genre_1'), ('Genre_2'), ('Genre_3'),
       ('Genre_4'), ('Genre_5'), ('Genre_6');

insert into books(title, author_id)
values ('BookTitle_1', 1), ('BookTitle_2', 2), ('BookTitle_3', 3);

insert into books_genres(book_id, genre_id)
values (1, 1),   (1, 2),
       (2, 3),   (2, 4),
       (3, 5),   (3, 6);


insert into comments(comment, book_id)
values ('comment_1_1', 1), ('comment_1_2', 1), ('comment_2_1', 2), ('comment_3_1', 3);


INSERT INTO users(id, Username, Password)
  VALUES (1, 'USER', '$2a$12$3xhExkLmROmGyfVAAFq16.UWFVJx3qEUBK9Lxe1Is.X3geAs9ttvC'), (2, 'ADMIN','$2a$12$3xhExkLmROmGyfVAAFq16.UWFVJx3qEUBK9Lxe1Is.X3geAs9ttvC');

INSERT INTO roles(id, name)
  VALUES (1, 'USER'), (2, 'ADMIN');

insert into users_roles(user_id, role_id)
values (1, 1), (2, 2);


INSERT INTO acl_sid (id, principal, sid) VALUES
(1, 1, 'ADMIN'),
(2, 1, 'USER'),
(3, 0, 'ADMIN');

INSERT INTO acl_class (id, class) VALUES
(1, 'ru.otus.hw.models.Book');

INSERT INTO acl_object_identity (id, object_id_class, object_id_identity, parent_object, owner_sid, entries_inheriting) VALUES
(1, 1, 1, NULL, 3, 0),
(2, 1, 2, NULL, 3, 0),
(3, 1, 3, NULL, 3, 0);

INSERT INTO acl_entry (id, acl_object_identity, ace_order, sid, mask,
                       granting, audit_success, audit_failure) VALUES
--книга 1
(1, 1, 1, 1, 1, 1, 1, 1),--ADMIN READ
--(2, 1, 2, 1, 15, 1, 1, 1),--ADMIN FULL
(2, 1, 2, 1, 2, 1, 1, 1),--ADMIN WRITE
(8, 1, 4, 1, 8, 1, 1, 1),--ADMIN DELETE
(3, 1, 3, 3, 1, 1, 1, 1),--ROLE_ADMIN READ
--книга 2
(4, 2, 1, 2, 1, 1, 1, 1),
(5, 2, 2, 3, 1, 1, 1, 1),
--книга 3
(6, 3, 1, 3, 1, 1, 1, 1),
(7, 3, 2, 3, 2, 1, 1, 1);