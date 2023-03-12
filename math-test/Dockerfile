FROM openjdk:17
ENV TZ="Europe/Amsterdam"
ARG VERSION=FILL
COPY ./target/math-test-$VERSION.jar /tmp/math-test.jar
WORKDIR /tmp

ENTRYPOINT ["java","-jar", "/tmp/math-test.jar"]
