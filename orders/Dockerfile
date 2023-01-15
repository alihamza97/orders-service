#Start with a base image containing Java runtime
FROM openjdk:11
# Add the application's jar to the container
COPY target/orders-0.0.1-SNAPSHOT.jar orders-0.0.1-SNAPSHOT.jar
#execute the application
ENTRYPOINT ["java","-jar","/orders-0.0.1-SNAPSHOT.jar"]
