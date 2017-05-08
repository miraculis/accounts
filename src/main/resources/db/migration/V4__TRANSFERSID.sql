delete from transfers;
alter table transfers drop column id;
alter table transfers add column id bigint not null;
insert into transfers(id, fromId, toId, amount, ts) select generate_series, 1, 2, 10000, 10000000 from generate_series(1, 100);