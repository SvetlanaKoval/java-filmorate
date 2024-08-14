-- Добавление пользователей в таблицу users
INSERT INTO users (email, login, name, birthday)
VALUES ('user1@example.com', 'user1', 'User One', '1990-01-01'),
       ('user2@example.com', 'user2', 'User Two', '1991-02-02'),
       ('user3@example.com', 'user3', 'User Three', '1992-03-03');

INSERT INTO user_friends (user_id, friend_id, friendship_status)
VALUES (3, 1, 'UNCONFIRMED');

insert into rating (name)
VALUES ('G'),
       ('PG'),
       ('PG-13'),
       ('R'),
       ('NC-17');

INSERT INTO genres (name)
VALUES ('Комедия'),
       ('Драма'),
       ('Мультфильм'),
       ('Триллер'),
       ('Документальный'),
       ('Боевик');

INSERT INTO films (name, description, release_date, duration, rating_id)
VALUES ('Film One', 'Description for film one', '2024-01-01', 120, 1),
       ('Film Two', 'Description for film two', '2023-12-01', 150, 2);

INSERT INTO film_genres (film_id, genre_id)
VALUES (1, 1),
       (1, 2),
       (2, 1);

INSERT INTO film_likes (user_id, film_id)
VALUES (1, 1),
       (1, 2),
       (2, 1);
