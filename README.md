# Technical assignment

## Requirements

- Java 11
- Maven
- Postgresql

## Clone git repository locally
> git clone https://github.com/mikulicmateo/plm.git

## Open the project with IDE

Set SKD to Java 11

## Edit database configuration

Open application.properties file and edit following variables with your local information.

> spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
> 
> spring.datasource.username=postgres
> 
> spring.datasource.password=root

If you don't want database to create and drop with every running delete line:

> spring.jpa.hibernate.ddl-auto=create

and run this script in postgres:

> CREATE TABLE public.product (
> 
>   id bigint NOT NULL,
> 
>   is_available boolean,
> 
>   code character varying(10),
> 
>   description character varying(255),
> 
>   name character varying(255),
> 
>   price_eur double precision,
> 
>   price_hrk double precision
> 
>);

## Compile with maven

> mvn clean package 

## Run

### Run in IDE

Run PlmApplication.java from with your IDE

### Run with maven

> mvn spring-boot:run