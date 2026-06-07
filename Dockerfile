FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests -B



FROM eclipse-temurin:21-jre AS run


WORKDIR /app

COPY --from=build /app/target/fruit-api-h2-0.0.1-SNAPSHOT.jar ./fruit-api-h2-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "fruit-api-h2-0.0.1-SNAPSHOT.jar"]

