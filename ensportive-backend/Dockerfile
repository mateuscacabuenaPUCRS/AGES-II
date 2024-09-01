FROM alpine/java:21-jdk AS build
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests
RUN rm /app/target/*.jar.original

FROM openjdk:21
COPY --from=build /app/target/*.jar ensportive.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ensportive.jar"]