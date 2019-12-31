FROM openjdk:8-jdk-alpine
RUN mkdir -p /kep/spring-boot && mkdir /kep/logs
ARG JAR_FILE
COPY ${JAR_FILE} /kep/spring-boot/app.jar
VOLUME /kep/logs
EXPOSE 9191
ENTRYPOINT ["/usr/bin/java", "-jar", "/kep/spring-boot/app.jar"]
