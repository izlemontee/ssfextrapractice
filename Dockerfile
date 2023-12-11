FROM maven:3-eclipse-temurin-21

#working and target dir
WORKDIR /app

#copy all the required stuff
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
COPY src src


#build app
RUN mvn package -Dmaven.test.skip=true

ENV PORT=8080 
ENV SPRING_REDIS_HOST=localhost SPRING_REDIS_PORT=6379
ENV SPRING_REDIS_USERNAME= SPRING_REDIS_PASSWORD=

EXPOSE ${PORT} 
ENTRYPOINT SERVER_PORT=${PORT}  java -jar target/ssfworkshoptrial-0.0.1-SNAPSHOT.jar