FROM openjdk:18.0oracle
MAINTAINER Javad Nasrolla
ADD ./target/eureka-service.jar /app
CMD ["java", "-Xmx200m", "-jar", "/app/eureka-service.jar"]
EXPOSE 8761
