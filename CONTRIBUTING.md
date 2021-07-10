# Contributing

## Reading

## Dependencies

### MYSQL

To start mysql, you must first build the image and then run it.

1. To build the container:

   ```SH
   docker build -t mysqldb docker/mysql/
    ```

1. To start the mysql container

    ``` SH
    docker run --name merrestdb -p3306:3306 -d mysqldb
    docker start merrestdb
    ```

### Java

You must have Java 11 and Maven installed.

1. To start the project

    ```SH
    ./mvnw clean spring-boot:run
    ```
