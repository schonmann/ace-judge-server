#
# Build stage
#
FROM maven:alpine AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -Dmaven.test.skip=true -f /home/app/pom.xml clean package

#
# Package stage
#
FROM java:8
RUN mkdir /upload-dir
VOLUME /upload-dir
COPY --from=build /home/app/target/ace-judge-server-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
