create table Age_ratings
(
    Age_rating_id                     int primary key auto_increment,
    Age_rating_title                  varchar(15) unique,
    Maximum_allowed_age               TINYINT UNSIGNED,
    Possibility_of_going_with_parents bool,


    Check (Maximum_allowed_age <= 30 and Maximum_allowed_age > 10),
    unique (Maximum_allowed_age, Possibility_of_going_with_parents)
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
    Foreign key (Age_rating_id) references Age_ratings (Age_rating_id),
    Unique (Film_name,Producer,Release_date)
);

create table Films_Genres
(
    Film_id  int references Films (Film_id) on delete cascade,
    Genre_id int references Genres (Genre_id) on delete cascade,

    Primary key (Film_id, Genre_id)
);

create table Sessions
(
    Session_id        int primary key auto_increment,
    Film_id           int         not null references Films (Film_id) on delete cascade,
    Session_full_date date unique not null,
    ThreeD_Supporting boolean     not null default false
);

create table Users
(
    User_id      int primary key auto_increment,
    Mail         varchar(30) unique     not null,
    First_name   varchar(20),
    Last_name    varchar(20),
    Phone_number varchar(15),
    Is_Admin     boolean      default 0 not null,
    Coins        int unsigned default 0 not null,
    Birthday     date                   not null,
    Password     varchar(15)            not null
);

create table Tickets
(
    Ticket_id    int primary key auto_increment,
    Customer_id  int              not null references Users (User_id) on delete cascade,
    Session_id   int              not null references Sessions (Session_id) on delete cascade,
    Place_column tinyint unsigned not null,
    Place_row    tinyint unsigned not null,
    Price        int unsigned     not null default 0,
    Booking_date date             not null,

    Check (Place_column > 0 and Place_column <= 8),
    Check (Place_row > 0 and Place_row <= 8)
);

create table Comments
(
    Comment_id      int primary key auto_increment,
    Film_id         int  not null references Films (Film_id) on delete cascade,
    Author_id       int  not null references Users (User_id) on delete cascade,
    Content         text not null,
    Date_of_writing date not null
);

CREATE TRIGGER `fill price`
    BEFORE INSERT
    ON `tickets`
    FOR EACH ROW
BEGIN
    Set new.Price = 50 + 5 * new.Place_row
        + 5 * (select Hour(Session_full_date) from Sessions where session_id = new.session_id)
        + (select ThreeD_Supporting from Sessions where session_id = new.session_id) * 20;
end;
delimiter ;
