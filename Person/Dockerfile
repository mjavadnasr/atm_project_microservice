FROM openjdk:18.0oracle
MAINTAINER Javad Nasrolla
ADD ./target/person-service.jar /app
CMD ["java", "-Xmx200m", "-jar", "/app/person-service.jar"]
EXPOSE 8081
