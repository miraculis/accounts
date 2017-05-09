delete from transfers;
delete from accounts;

insert into accounts(id, amount) select generate_series, random() * 1000000 from generate_series(1, 10000);

insert into transfers(id, fromId, toId, amount, ts) select generate_series(1, 1000000), random() * 10000, random() * 10000, random() * 10000, date_part('epoch', now());