drop table customer;

create table customer
(
    id    INTEGER default nextval('customer_id_seq')primary key,
    name  TEXT not null,
    email TEXT not null,
    age   INT,
    gender TEXT not null
);

alter table customer
    add constraint customer_email_unique unique (email);

ALTER TABLE customer
    ADD CONSTRAINT gender_check CHECK (gender IN ('MALE', 'FEMALE'));