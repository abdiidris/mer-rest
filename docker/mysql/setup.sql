DROP DATABASE IF EXISTS amerestdb;
CREATE DATABASE amerestdb;
GRANT ALL ON amerestdb.* TO 'ameuser'@'%';

ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY 'root';

FLUSH PRIVILEGES;

