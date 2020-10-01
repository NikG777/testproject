FROM adoptopenjdk/openjdk11:armv7l-centos-jdk-11.0.8_10
MAINTAINER experto.com
VOLUME /tmp
EXPOSE 8080
ADD target/testproject-0.0.1-SNAPSHOT.jar testproject.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","testproject.jar"]