FROM openjdk:17
ENV TZ="Europe/Amsterdam"
COPY ./target/test-0.1-SNAPSHOT.jar /tmp/test.jar
WORKDIR /tmp

ENTRYPOINT ["java","-jar", "/tmp/test.jar"]
