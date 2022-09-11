FROM openjdk:18.0oracle
MAINTAINER Javad Nasrolla
ADD ./target/account-service.jar /app
CMD ["java", "-Xmx200m", "-jar", "/app/account-service.jar"]
EXPOSE 8081
