# java-filmorate
Template repository for Filmorate project.

[***Filmorate DB***](https://app.quickdatabasediagrams.com/#/d/E089zB)

![***Filmorate DB***](/images/QuickDBD-Free%20Diagram.png)

В приложении 2 основные сущности - **film** и **user**.

В таблице **film** описываются основные данные - первичный ключ,
название, описание, дата выхода, длительность и возрастной рейтинг.  
Жанр и рейтинг - являются отдельными сущностями, поэтому вынесены в 
самостоятельные таблицы.
Связь фильма и рейтинга Ассоциации кинокомпаний - для фильма рейтинг 
это внешний ключ, который ссылается на таблицу **raiting**.
Связь фильма и жанра - в объединенной таблице **film_genre**, которая 
хранит фильм и все жанры, которыми обладает этот фильм.

В таблице **user** описывается киноман - первичный ключ, логин, эмайл, 
дата рождения. Остальные сущности представлены в отдельных таблицах и 
соединены с user, так как напрямую от него не зависят.
У user есть друзья, связь user и его друзей выделена в таблице 
**user_friends**, где объединен user, его друзья и статус их дружбы.

В свою очередь, **film** и **user** объединяет таблица **film_likes**, 
которая ссылается как на **film**, так и на **user**.

Получение всех фильмов, которые нравятся пользователю с почтой -
Hello12345@yandex.ru:

SELECT f.name\
FROM film f\
JOIN film_likes fl ON f.film_id = fl.film_id\
JOIN USER u ON u.user_id = fl.user_id\
WHERE u.email = 'Hello12345@yandex.ru';

Получение всех друзей со статусом - UNCONFIRMED:

SELECT u.name,\
u.email\
FROM USER u\
JOIN user_friends uf ON u.user_id = uf.user_id\
JOIN friendship_status fs ON us.status = fs.status_id\
WHERE u.user_id = 3\
AND fs.status_name = 'UNCONFIRMED';

