FROM java:8

VOLUME /tmp

EXPOSE 8080

ADD target/ace-judge-server-0.0.1-SNAPSHOT.jar ace-judge-server-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "ace-judge-server-0.0.1-SNAPSHOT.jar"]