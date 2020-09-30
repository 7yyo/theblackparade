create database if not exists transfer;
use transfer;
create table if not exists account_card_info(id int auto_increment,card_number int,card_balance decimal,primary key(id));
create table if not exists account_statement_info(id int auto_increment, card_number int,transfer_type int,primary key(id));