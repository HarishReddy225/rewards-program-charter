create table customer
(
	id integer not null,
    name varchar(255) not null
);

create table transaction
(
	id integer not null,
    customer_id integer not null,
    transaction_date date not null,
    transaction_amount double,
    foreign key (customer_id) references customer(id)
)