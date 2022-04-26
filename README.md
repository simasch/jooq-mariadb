# jOOQ with MariaDB

This project introduces [jOOQ](https://jooq.org).

## Schema and Test Data

The project use the Sakila film rental example database from https://github.com/jOOQ/sakila

## Run the Build with Tests

First a TestContainer with the MariaDB is started. Then FlyWay executes the database migrations and finally jOOQ generates the classes from the database. 

Simply run:

    mvn test

