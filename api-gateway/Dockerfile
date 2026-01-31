FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .

COPY ms-equipamentos/pom.xml ./ms-equipamentos/
COPY ms-operacional/pom.xml ./ms-operacional/
COPY ms-transparencia/pom.xml ./ms-transparencia/

COPY api-gateway/pom.xml ./api-gateway/
COPY api-gateway/src ./api-gateway/src

RUN mvn dependency:go-offline -B -DskipTests

RUN mvn clean install -pl api-gateway -am -Dfile.encoding=UTF-8 -B -V -DskipTests

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/api-gateway/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]