drop table if exists phone_order_shipment;

CREATE TABLE phone_order_shipment
(
    id                 VARCHAR(36) NOT NULL PRIMARY KEY,
    phone_order_id            VARCHAR(36) UNIQUE,
    tracking_number    VARCHAR(50),
    created_date       TIMESTAMP,
    last_modified_date DATETIME(6) DEFAULT NULL,
    version            BIGINT      DEFAULT NULL,
    CONSTRAINT bos_pk FOREIGN KEY (phone_order_id) REFERENCES phone_order (id)
) ENGINE = InnoDB;

ALTER TABLE phone_order
    ADD COLUMN phone_order_shipment_id VARCHAR(36);

ALTER TABLE phone_order
    ADD CONSTRAINT bos_shipment_fk
        FOREIGN KEY (phone_order_shipment_id) REFERENCES phone_order_shipment (id);