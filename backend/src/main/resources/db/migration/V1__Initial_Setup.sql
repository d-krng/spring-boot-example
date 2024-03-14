
/*Different option*/
create Sequence customer_id_seq;


create table customer
(
    id    INTEGER default nextval('customer_id_seq')primary key,
    name  TEXT not null,
    email TEXT not null,
    age   INT,
    gender TEXT not null
);


/*create table customer
(
    id    BIGSERIAL primary key,
    name  TEXT not null,
    email TEXT not null,
    age   INT  not null
);*/
alter table customer
    add constraint customer_email_unique unique (email);

ALTER TABLE customer
    ADD CONSTRAINT gender_check CHECK (gender IN ('MALE', 'FEMALE'));