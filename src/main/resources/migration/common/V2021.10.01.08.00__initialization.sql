CREATE TABLE book
(
    id             BIGINT(20)   NOT NULL AUTO_INCREMENT,
    name           VARCHAR(255) NOT NULL,
    description    TEXT         NULL     DEFAULT NULL,
    checked_out_at DATETIME     NULL     DEFAULT NULL,
    created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME              DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;