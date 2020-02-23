# Kafka Java Spring Boot

Small (and very simple) Spring Boot App that consumes and produces Kafka events.

This app produces the Fibonacci Sequence and send it to a Kafka topic (`fibonacci`). There is also a consumer that 
can be accessed using `http://localhost:9191/kafka-java-spring/fibonacci`.

## Build

```
$ mvn clean package
```

You can use this [docker compose file](docker/docker-compose.yml) to create a single node Kafka cluster and run this app.

The topic used can be created using 

```bash
$ docker exec kafka kafka-topics --zookeeper zookeeper:2181 --topic fibonacci --create --partitions 1 --replication-factor 1
```
