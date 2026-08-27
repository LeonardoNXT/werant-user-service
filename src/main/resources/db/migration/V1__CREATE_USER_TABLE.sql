CREATE TABLE users
(
    id                 UUID PRIMARY KEY,
    name               VARCHAR(100) NOT NULL,
    username           VARCHAR(50)  NOT NULL,
    password           VARCHAR(255) NOT NULL,
    email              VARCHAR(255) NOT NULL UNIQUE,
    phone_number       VARCHAR(50)  NOT NULL,
    status             VARCHAR(20)  NOT NULL,
    profile_picture_id UUID         NOT NULL UNIQUE,
    created_at         TIMESTAMP    NOT NULL,
    updated_at         TIMESTAMP    NOT NULL
);