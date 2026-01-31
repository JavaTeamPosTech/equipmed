FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

COPY pom.xml .

COPY api-gateway/pom.xml ./api-gateway/
COPY ms-equipamentos/pom.xml ./ms-equipamentos/
COPY ms-transparencia/pom.xml ./ms-transparencia/

COPY ms-operacional/pom.xml ./ms-operacional/
COPY ms-operacional/src ./ms-operacional/src

RUN mvn dependency:go-offline -B -DskipTests

RUN mvn clean install -pl ms-operacional -am -Dfile.encoding=UTF-8 -B -V -DskipTests

FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/ms-operacional/target/*.jar app.jar
CMD ["java", "-jar", "app.jar"]