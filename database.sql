CREATE TABLE users (
    username VARCHAR(255) NOT NULL PRIMARY KEY,
    password VARCHAR(255),
    name VARCHAR(255),
    token VARCHAR(255),
    token_expired_at BIGINT
);

CREATE TABLE requests (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255),
    operations VARCHAR(255),
    regions VARCHAR(255),
    table_target VARCHAR(255),
    columns_target VARCHAR(255),
    file_location VARCHAR(255),
    FOREIGN KEY (username) REFERENCES users(username)
);

CREATE TABLE params (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    request_id INT,
    columns VARCHAR(255),
    values_fill VARCHAR(255),
    FOREIGN KEY (request_id) REFERENCES requests(id) ON DELETE CASCADE
);

CREATE TABLE conditions (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    request_id INT,
    columns VARCHAR(255),
    comparative VARCHAR(255),
    values_conditions VARCHAR(255),
    FOREIGN KEY (request_id) REFERENCES requests(id) ON DELETE CASCADE
);
