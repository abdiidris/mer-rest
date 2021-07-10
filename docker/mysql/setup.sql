DROP DATABASE IF EXISTS merrestdb;
CREATE DATABASE merrestdb;
GRANT ALL ON merrestdb.* TO 'meruser'@'%';

ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'root';

FLUSH PRIVILEGES;

