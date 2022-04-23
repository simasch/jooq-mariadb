# jOOQ with MariaDB

This project introduces [jOOQ](https://jooq.org).

### Run the Build with Tests

First a TestContainer with the MariaDB is started. Then FlyWay executes the database migrations and finally jOOQ generates the classes from the database. 

Simply run:

    mvn test
