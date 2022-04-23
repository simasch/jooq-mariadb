# jOOQ with MariaDB

This project introduces [jOOQ](https://jooq.org).

A MariaDB database with pre-installed Sakila schema and example data is used.

### Run the Database

It's recommend to use Docker to run the database:

    docker run -p 3306:3306 -d simas/mariadb-sakila 

Alternatively you can use SkySQL or install MariaDB and then apply the SQL script from https://dev.mysql.com/doc/sakila/en/

### Run the Build with Tests

The jOOQ Maven plugin generates the jOOQ classes from the database. 

    mvn test
