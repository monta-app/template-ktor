CREATE TABLE book
(
    id          BIGINT(20)   NOT NULL AUTO_INCREMENT,
    name        VARCHAR(255) NOT NULL,
    description TEXT         NULL                        DEFAULT NULL,
    status      ENUM ('checked_in','checked_out','lost') DEFAULT 'checked_in',
    status_at   DATETIME     NOT NULL                    DEFAULT CURRENT_TIMESTAMP,
    created_at  DATETIME     NOT NULL                    DEFAULT CURRENT_TIMESTAMP,
    updated_at  DATETIME                                 DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX index_book_status (status),
    INDEX index_book_status_at (status_at)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;