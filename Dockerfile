FROM mariadb:10.7.3

# Install curl, download the sakila db, and prepare the database
RUN apt-get update && \
    apt-get install -y curl && \
    cd /docker-entrypoint-initdb.d/ && \
    curl -L http://downloads.mysql.com/docs/sakila-db.tar.gz | tar -xz && \
    mv sakila-db/sakila-schema.sql ./01-sakila-schema.sql && \
    mv sakila-db/sakila-data.sql ./02-sakila-data.sql && \
    rm -rf sakila-db && \
    apt-get clean && apt-get autoclean

# Root password
ENV MARIADB_ROOT_PASSWORD sakila
# Database
ENV MARIADB_DATABASE sakila

# Default MariaDB port
EXPOSE 3306
