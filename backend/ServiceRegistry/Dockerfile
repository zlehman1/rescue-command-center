FROM maven:3.9.5-sapmachine-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN export JAVA_HOME=/usr/lib/jvm/sapmachine-21 && mvn clean package -DskipTests

FROM openjdk:21

WORKDIR /app

COPY --from=build /app/target/ServiceRegistry-*.jar /app/service-registry.jar

EXPOSE 8761

CMD ["java", "-jar", "service-registry.jar"]
