FROM openjdk:18.0oracle
MAINTAINER Javad Nasrolla
ADD ./target/transaction-service.jar /app
CMD ["java", "-Xmx200m", "-jar", "/app/transaction-service.jar"]
EXPOSE 8083
