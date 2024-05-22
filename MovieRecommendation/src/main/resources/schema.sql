
DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE users (
    uid INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(255) DEFAULT NULL,
    passwordHash VARBINARY(32) DEFAULT NULL
);

DROP TABLE IF EXISTS sessions CASCADE;
CREATE TABLE sessions (
    sessionId VARCHAR(255) PRIMARY KEY,
    userId INT,
    sessionStart TIMESTAMP DEFAULT NULL,
    sessionPeriod INT DEFAULT NULL,
    CONSTRAINT FK_userId FOREIGN KEY (userId) REFERENCES users(uid)
);

DROP TABLE IF EXISTS genres CASCADE;
CREATE TABLE genres (
    genreId INT PRIMARY KEY,
    genreName VARCHAR(255) NOT NULL
);

DROP TABLE IF EXISTS genre_preferences CASCADE;
CREATE TABLE genre_preferences (
    genre_preferencesId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    genreId INT NOT NULL,
    FOREIGN KEY (genreId) REFERENCES genres(genreId)
);

DROP TABLE IF EXISTS favourite_movies CASCADE;
CREATE TABLE favourite_movies (
    favourite_moviesId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    movieId INT NOT NULL,
    FOREIGN KEY (userId) REFERENCES users(uid)
);


