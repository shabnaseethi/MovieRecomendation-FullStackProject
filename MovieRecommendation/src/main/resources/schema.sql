DROP TABLE IF EXISTS users;
CREATE TABLE users (
    uid INT AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(255) DEFAULT NULL,
    passwordHash VARBINARY(32) DEFAULT NULL
);

DROP TABLE IF EXISTS sessions;
CREATE TABLE sessions (
    sessionId VARCHAR(255) PRIMARY KEY,
    userId INT,
    sessionStart TIMESTAMP DEFAULT NULL,
    sessionPeriod INT DEFAULT NULL,
    CONSTRAINT FK_userId FOREIGN KEY (userId) REFERENCES users(uid)
);

CREATE TABLE IF NOT EXISTS genres (
    genreId INT PRIMARY KEY,
    genreName VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS genre_preferences (
    genre_preferencesId INT AUTO_INCREMENT PRIMARY KEY,
    userId INT NOT NULL,
    genreId INT NOT NULL,
    FOREIGN KEY (genreId) REFERENCES genres(genreId)
);


