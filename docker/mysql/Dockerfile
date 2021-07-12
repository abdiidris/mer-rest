FROM mysql:latest
ENV MYSQL_ROOT_PASSWORD root
ENV MYSQL_DB merrestdb
ENV MYSQL_USER meruser
ENV MYSQL_PASSWORD merpassword
ADD setup.sql ./setup.sql
EXPOSE 3306