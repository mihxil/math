FROM openjdk:17
COPY ./target/test-0.1-SNAPSHOT.jar /tmp/test.jar
WORKDIR /tmp
ENTRYPOINT ["java","-jar", "/tmp/test.jar"]
