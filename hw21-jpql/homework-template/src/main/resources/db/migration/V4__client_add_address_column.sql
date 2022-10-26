alter table client add address_id bigint null;
alter table client add constraint fk_client__address_id__address__id foreign key (address_id) references address(id);
