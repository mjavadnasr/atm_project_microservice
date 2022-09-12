FROM openjdk:18.0oracle
MAINTAINER Javad Nasrolla
ADD ./target/configserver-service.jar /app
CMD ["java", "-Xmx200m", "-jar", "/app/configserver.jar"]
EXPOSE 9595
