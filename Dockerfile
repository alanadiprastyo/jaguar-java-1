FROM docker.io/java
LABEL maintainer="Alan Adi Prastyo <alan.a.prastyo@gmail.com>" \
      io.openshift.expose-services="8080:http"
COPY target/JaguarJava.war /opt/JaguarJava.war

EXPOSE 8080
USER 1001

CMD ["java","-jar","/opt/JaguarJava.war"]

