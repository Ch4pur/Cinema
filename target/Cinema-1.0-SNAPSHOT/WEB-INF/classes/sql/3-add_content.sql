/*------users------*/
insert into users (Mail, First_name, Last_name, Phone_number, Birthday, Password, Is_Admin)
VALUES ('admin@nure.ua', null, null, '0965584484', '2002.03.29', '1234567s', 1),
       ('user@gmail.com', null, null, null, '2002.03.29', '1234567s', 0);
/*------Genres--------*/
insert into genres (Genre_name)
values ('Horror'),
       ('Shooter'),
       ('Detective'),
       ('Melodrama'),
       ('Thriller');
/*-----age ratings---------*/
insert into age_ratings (Age_rating_title, Maximum_allowed_age, Possibility_of_going_with_parents)
values ('Pg-13', 13, 1),
       ('Ag-13', 13, 0),
       ('18+', 18, 0);

/*------films------------*/
insert into films (Film_name, Producer, Age_rating_id, Film_image, Duration, Release_date, Description)
values ('Mandalorian', 'Jhon Phavro', 1, 'mandalorian.jpg', '01:30:00', '2019.01.11', '«Мандалорец» (англ. The Mandalorian) — американский телесериал в жанре космический вестерн, созданный Джоном Фавро для стримингового сервиса Disney+. Это первый игровой сериал, являющийся составной частью вселенной «Звёздных войн»'),
       ('Nobody', 'Ilya Naishuller', 3, 'nobody.jpg', '02:00:00', '2020.10.11', 'A bystander who intervenes to help a woman being harassed by a group of men becomes the target of a vengeful drug lord.'),
       ('Spider-man Into the spider verse','Avi Arad', 1, 'spider-man-into-the-spider-verse.jpg', '01:30:00','2019.10.11', 'Summaries. Teen Miles Morales becomes the Spider-Man of his universe, and must join with five spider-powered individuals from other dimensions to stop a threat for all realities. ... Then he is bitten by a radioactive spider and he obtains certain powers, similar to those of Spiderman.'),
       ('The shawshank redemption', 'Niki Marvin', 3, 'the-shawshank-redemption.jpg','02:30:00','1994.09.10','It tells the story of banker Andy Dufresne (Tim Robbins), who is sentenced to life in Shawshank State Penitentiary for the murders of his wife and her lover, despite his claims of innocence.');
/*---------sessions--------*/
insert into sessions (Film_id, Session_full_date, ThreeD_Supporting)
VALUES (1, '2002-11-01 12:00:00', 0),
       (1, '2021-03-08 9:30:00', 0),
       (1, '2021-03-08 14:30:00', 0),
       (1, '2021-04-08 21:00:00', 0);
