CREATE TABLE account
(
    id       INT GENERATED ALWAYS AS IDENTITY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    enabled  BOOLEAN      NOT NULL DEFAULT true,
    CONSTRAINT pk_account PRIMARY KEY (id),
    CONSTRAINT uq_account_username UNIQUE (username)
);