DROP TABLE IF EXISTS users;
CREATE TABLE users (
    uid int AUTO_INCREMENT PRIMARY KEY,
    userName VARCHAR(255) DEFAULT NULL,
    passwordHash VARCHAR(255) DEFAULT NULL
);