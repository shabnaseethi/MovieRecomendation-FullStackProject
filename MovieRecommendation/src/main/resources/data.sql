INSERT INTO users (userName, passwordHash) VALUES
('aaron', '3371f1bc3880e6e47f956face323b87c3de299c8bf1bfd815c5e1ba59098673e'),
('erik', 'c9d22bd28f57026a1648aa12686fc28e5d5f0c39d37a26eee32d3cb005a8584a');

INSERT INTO sessions (sessionId, userId, sessionStart, sessionPeriod) VALUES
('testId', 1, CURRENT_TIMESTAMP(), 1);