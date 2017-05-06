CREATE TABLE ACCOUNTS (
	id INT not null,
	amount INT not null
);
CREATE TABLE TRANSFERS (
	id INT not null,
	fromId INT not null,
	toId INT not null,
	amount INT not null,
	ts BIGINT not null
);