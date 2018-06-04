drop table if exists hp_goods, hp_customers, hp_items, hp_receipts;
create table hp_goods as select * from BAKERY.goods;
create table hp_customers as select * from BAKERY.customers;
create table hp_items as select * from BAKERY.items;
create table hp_receipts as select * from BAKERY.receipts;

grant all on aveluru.hp_goods to hasty@'%';
grant all on aveluru.hp_customers to hasty@'%';
grant all on aveluru.hp_items to hasty@'%';
grant all on aveluru.hp_receipts to hasty@'%';