create table Age_ratings
(
    Age_rating_title                  varchar(15) unique,
    Maximum_allowed_age               TINYINT UNSIGNED,
    Possibility_of_going_with_parents bool,

    Primary key (Age_raiting_title, Maximum_allowed_age, Possibility_of_going_with_parents),
    Check (Maximum_allowed_age <= 30 and Maximum_allowed_age > 30)
);

create table Age_ratings_ids
(
    Age_rating_id                     int primary key,
    Age_rating_title                  varchar(15) unique not null,
    Maximum_allowed_age               TINYINT UNSIGNED not null,
    Possibility_of_going_with_parents bool               not null,

    Foreign key
        (Age_raiting_title, Maximum_allowed_age, Possibility_of_going_with_parents)
        references Age_raitings (Age_raiting_title, Maximum_allowed_age, Possibility_of_going_with_parents)
        on delete cascade
);

create table Genres
(
    Genre_id   int primary key auto_increment,
    Genre_name varchar(20) unique
);

create table Films
(
    Film_id       int primary key auto_increment,
    Film_name     varchar(30) not null unique,
    Producer      varchar(20) not null,
    Age_rating_id int         not null,
    Film_image    varchar(40) unique,
    Duration      time        not null,
    Release_date  date        not null,
    description   text        not null,
    Foreign key (Age_rating_id) references Age_ratings_ids (Age_rating_id)
);

create table Films_Genres
(
    Film_id  int references Film_id (Films) on delete cascade,
    Genre_id int references Genre_id (Genres) on delete cascade,

    Primary key (Film_id, Genre_id)
);

create table Sessions
(
    Session_id        int primary key auto_increment,
    Film_id           int         not null references Films (Film_id) on delete cascade,
    Session_full_date date unique not null,
    ThreeD_Supporting boolean     not null default false

);
create table Roles
(
    Role_id    int primary key auto_increment,
    Role_title varchar(20) unique not null
);
create table Users
(
    User_id      int primary key auto_increment,
    Mail         varchar(30) unique not null,
    First_name   varchar(20),
    Last_name    varchar(20),
    Phone_number varchar(15),
    Is_Admin     boolean,
    Birthday     date               not null,
    Password     int                not null
);

create table Tickets
(
    Ticket_id    int primary key auto_increment,
    Customer_id  int  not null references Users (User_id) on delete cascade,
    Session_id   int  not null references Sessions (Session_id) on delete cascade,
    Place_column int  not null,
    Place_row    int  not null,
    Price        int  not null default 0,
    Booking_date date not null,

    Check (Place_column > 0 and Place_column <= 10),
    Check (Place_row > 0 and Place_row <= 10)
);

create table Comments
(
    Comment_id      int primary key auto_increment,
    Film_id         int  not null references Films (Film_id) on delete cascade,
    Author_id       int  not null references Users (User_id) on delete cascade,
    Content         text not null,
    Date_of_writing date not null
);

DELIMITER
//
CREATE TRIGGER `fill price`
    Before INSERT
    ON `tickets`
    FOR EACH ROW
BEGIN
    Set new.price = 50 + 5 * new.Place_row
	+ 5 * (select Hour(Session_full_date) from Sessions where session_id = new.session_id)
	+ if ((select ThreeD_Supporting from Sessions where session_id = new.session_id), 20, 0);
END;

