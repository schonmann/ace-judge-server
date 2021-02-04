#
# Build stage
#
FROM maven:alpine AS build
WORKDIR /home/app/
COPY pom.xml .
RUN mvn -Dmaven.test.skip=true verify clean --fail-never
COPY src src
RUN mvn -Dmaven.test.skip=true package

#
# Package stage
#
FROM java:8
RUN mkdir /upload-dir
VOLUME /upload-dir
COPY --from=build /home/app/target/ace-judge-server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
