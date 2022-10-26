create table phone
(
    id   bigint not null primary key,
    number varchar(50),
    client_id bigint not null,
    constraint fk_phone__client_id__client__id foreign key (client_id) references client(id)
);
