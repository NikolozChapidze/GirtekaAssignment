CREATE TABLE customer
(
    id                    BIGINT  NOT NULL,
    full_name             VARCHAR(255),
    type                  VARCHAR(20),
    PRIMARY KEY (id)
);

CREATE TABLE card
(
    id                    BIGINT  NOT NULL,
    customer_id           BIGINT  NOT NULL,
    type                  VARCHAR(20),
    card_number           VARCHAR(20),
    expiry                TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_card_customer FOREIGN KEY(customer_id) REFERENCES customer (id)
);

CREATE TABLE account
(
    id                    BIGINT NOT NULL,
    customer_id           BIGINT NOT NULL,
    iban                  VARCHAR(30),
    currency              VARCHAR(3),
    balance               NUMERIC(19,2),
    PRIMARY KEY (id),
    CONSTRAINT fk_account_customer FOREIGN KEY(customer_id) REFERENCES customer (id)
);