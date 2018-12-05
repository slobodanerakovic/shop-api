-- database and user creation, and granting
sudo su - postgres
psql template1
template1=# create user test with password 'test';
template1=# create database shop;
template1=# grant all privileges on database shop to test;
template1=# grant all on database shop to test;
\q
exit
-- exit psql and logout as user postgres