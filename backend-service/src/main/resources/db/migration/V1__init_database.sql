CREATE TABLE if not exists category (
                          id SERIAL NOT NULL PRIMARY KEY,
                          name VARCHAR(255) NOT NULL UNIQUE,
                          description TEXT
);

CREATE TABLE if not exists product (
                         id SERIAL NOT NULL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL UNIQUE,
                         description TEXT,
                         price DOUBLE PRECISION NOT NULL,
                         category_id SERIAL NOT NULL,
                         CONSTRAINT fk_product_category FOREIGN KEY (category_id)
                             REFERENCES category(id)
                             ON DELETE SET NULL
);

CREATE TABLE if not exists customer (
                          id SERIAL NOT NULL PRIMARY KEY,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          phone VARCHAR(50) NOT NULL,
                          address TEXT
);

CREATE TABLE if not exists invoice (
                         id SERIAL NOT NULL PRIMARY KEY,
                         invoice_identifier VARCHAR(100) NOT NULL UNIQUE,
                         invoice_date TIMESTAMP NOT NULL,
                         invoice_status VARCHAR(50) NOT NULL,
                         total_price DOUBLE PRECISION NOT NULL,
                         customer_id SERIAL NOT NULL,
                         CONSTRAINT fk_invoice_customer FOREIGN KEY (customer_id)
                             REFERENCES customer(id)
                             ON DELETE SET NULL
);

CREATE TABLE if not exists invoice_product (
                                 invoice_id SERIAL NOT NULL,
                                 product_id SERIAL NOT NULL,
                                 PRIMARY KEY (invoice_id, product_id),
                                 CONSTRAINT fk_invoice_product_invoice FOREIGN KEY (invoice_id)
                                     REFERENCES invoice(id)
                                     ON DELETE NO ACTION,
                                 CONSTRAINT fk_invoice_product_product FOREIGN KEY (product_id)
                                     REFERENCES product(id)
                                     ON DELETE NO ACTION
);
