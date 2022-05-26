FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE=*.jar
COPY ${JAR_FILE} herd-management.jar
ENTRYPOINT ["java", "-jar", "herd-management.jar"]