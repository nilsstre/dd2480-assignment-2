# Base Alpine Linux based image with OpenJDK JRE only
FROM openjdk:8-jre-alpine
# copy application WAR (with libraries inside)
COPY target/assignment-2-test-1.0-SNAPSHOT.jar /assignemnt-2.jag
# specify default command
CMD ["/usr/bin/java", "-jar", "/assignement-2.jar"]