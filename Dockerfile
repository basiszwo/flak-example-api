FROM openjdk:8u121-jdk

EXPOSE 4567

RUN apt-get update -qq && apt-get install -y maven

RUN mkdir /api

WORKDIR /api

ADD . /api

run mvn package

CMD ["java", "-jar", "/api/target/api-1.0-SNAPSHOT-jar-with-dependencies.jar", "/api/config.properties"]
