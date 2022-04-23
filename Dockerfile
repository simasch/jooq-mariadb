FROM mariadb:10.7.3

# Sakila database
ADD 1-sakila-schema.sql 2-sakila-data.sql /docker-entrypoint-initdb.d/

# Root password
ENV MARIADB_ROOT_PASSWORD sakila
# Database
ENV MARIADB_DATABASE sakila

# Default MariaDB port
EXPOSE 3306
