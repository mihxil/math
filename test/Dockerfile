FROM openjdk:17
COPY ./target/test-0.1-SNAPSHOT-jar-with-dependencies.jar /tmp
WORKDIR /tmp
ENTRYPOINT ["java","-jar", "/tmp/test-0.1-SNAPSHOT-jar-with-dependencies.jar"]
