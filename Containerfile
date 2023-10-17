FROM openjdk:8-jdk-alpine
MAINTAINER alan.a.prastyo@gmail.com
COPY target/JaguarJava.war JaguarJava.war
ENTRYPOINT ["java","-jar","/JaguarJava.war"]
