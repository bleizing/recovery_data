CREATE TABLE requests (
        id VARCHAR(100) NOT NULL, 
        operations VARCHAR (10) NOT NULL,
        regions VARCHAR (30),
        tables VARCHAR (30),
        PRIMARY KEY (id),
    )

CREATE TABLE params (
    id INT (100) NOT NULL,
    request_id VARCHAR(100) NOT NULL,
    columns VARCHAR(100),
    values_params VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY fk_request_params(request_id) REFERENCES requests(id)
)

CREATE TABLE conditions (
    id INT(100) NOT NULL,
    request_id VARCHAR(100) NOT NULL,
    columns VARCHAR(100),
    values_conditions VARCHAR(255),
    PRIMARY KEY (id),
    FOREIGN KEY fk_request_conditions(request_id) REFERENCES requests(id)
)
