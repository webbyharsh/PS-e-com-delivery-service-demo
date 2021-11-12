FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} delivery-service-0.0.1-SNAPSHOT.jar
CMD java $JVM_OPTS $JAVA_OPTS $APPD_AGENT -Denv=prod -jar  delivery-service-0.0.1-SNAPSHOT.jar
EXPOSE 9006
