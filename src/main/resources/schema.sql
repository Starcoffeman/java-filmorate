CREATE TABLE IF NOT EXISTS USERS
(
    id       INT          NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email    VARCHAR(255) NOT NULL,
    login    VARCHAR(255) NOT NULL,
    name     VARCHAR(255),
    birthday TIMESTAMP,
    deleted  BOOLEAN DEFAULT FALSE,
    UNIQUE (email),
    UNIQUE (login)
);


CREATE TABLE IF NOT EXISTS MPA
(
    id   INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS FILMS
(
    id           INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name         VARCHAR(255),
    description  VARCHAR(255),
    release_date TIMESTAMP,
    rate         INT,
    duration     INT,
    mpa      INT REFERENCES MPA (id),
    deleted      BOOL DEFAULT FALSE
);


CREATE TABLE IF NOT EXISTS GENRES
(
    id   INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS FILM_GENRES
(
    film_id   INT references USERS (id),
    genres_id INT references GENRES (id),
    primary key (film_id, genres_id)
);

CREATE TABLE IF NOT EXISTS FRIENDS
(
    user_id   INT not null references USERS (id),
    friend_id INT not null references USERS (id),
    primary key (user_id, friend_id)
);

CREATE TABLE IF NOT EXISTS LIKES
(
    user_id INT references USERS (id),
    film_id INT references USERS (id),
    PRIMARY KEY (user_id, film_id)
);
