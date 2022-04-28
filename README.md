# jOOQ with MariaDB

This project introduces [jOOQ](https://jooq.org).

## Schema and Test Data

The project use the Sakila film rental example database from https://github.com/jOOQ/sakila

## Run the Build with Tests

First a TestContainer with the MariaDB is started. Then FlyWay executes the database migrations and finally jOOQ generates the classes from the database. 

Simply run:

    mvn test

## Webinar

Watch the webinar [Type-Safe SQL with jOOQ on SkySQL](https://go.mariadb.com/22Q3-WBN-GLBL-DBaaS-Type-safe-SQL-jOOQ-on-SkySQL-2022-04-28_Registration-LP.html).

Also check [this repository](Watch the webinar [Type-Safe SQL with jOOQ on SkySQL](https://go.mariadb.com/22Q3-WBN-GLBL-DBaaS-Type-safe-SQL-jOOQ-on-SkySQL-2022-04-28_Registration-LP.html).) with minimal configuration using Spring Boot and jOOQ to connect to a running MariaDB database.
