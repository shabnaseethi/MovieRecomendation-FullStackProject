INSERT INTO users (userName, passwordHash) VALUES
('aaron', '3371f1bc3880e6e47f956face323b87c3de299c8bf1bfd815c5e1ba59098673e'),
('erik', 'c9d22bd28f57026a1648aa12686fc28e5d5f0c39d37a26eee32d3cb005a8584a'),
('person2', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8');

-- Login info:
-- aaron, (can't remember...)
-- erik, cypher
-- person2, password


INSERT INTO genres (genreId,genreName) VALUES
(28,'Action'),
(18,'Drama');



INSERT INTO genre_preferences (userId, genreId) VALUES
(1, 28),
(1, 18),
(2, 18);


INSERT INTO sessions (sessionId, userId, sessionStart, sessionPeriod) VALUES
('testId', 1, CURRENT_TIMESTAMP(), 1);

