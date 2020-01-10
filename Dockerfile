FROM maven:3.3.3

VOLUME /tmp
RUN mvn clean package
ARG JAR_FILE=target/*.jar
COPY target/*.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]