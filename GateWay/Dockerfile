FROM openjdk:18.0oracle
MAINTAINER Javad Nasrolla
ADD ./target/gateway-service.jar /app
CMD ["java", "-Xmx200m", "-jar", "/app/gateway-service.jar"]
EXPOSE 8080
