# # Use an official OpenJDK runtime as a parent image
# FROM openjdk:17-jdk-slim

# # Set the working directory in the container
# WORKDIR /app

# # Copy the built jar from the Maven 'target' directory into the container
# COPY target/*.jar app.jar

# # Expose port 5000 (the port your application is using)
# EXPOSE 80

# # Run the jar file with environment variables
# ENTRYPOINT ["java", "-jar", "app.jar"]




FROM openjdk:17-jdk-slim

WORKDIR /app

# Define build arguments
ARG SERVER_PORT
ARG DB_URL
ARG DB_USERNAME
ARG DB_PASSWORD
ARG AWS_REGION
ARG AWS_ACCESS_KEY
ARG AWS_SECRET_KEY
ARG AWS_SQS_ENDPOINT

# Set environment variables
ENV SERVER_PORT=$SERVER_PORT
ENV DB_URL=$DB_URL
ENV DB_USERNAME=$DB_USERNAME
ENV DB_PASSWORD=$DB_PASSWORD
ENV AWS_REGION=$AWS_REGION
ENV AWS_ACCESS_KEY=$AWS_ACCESS_KEY
ENV AWS_SECRET_KEY=$AWS_SECRET_KEY
ENV AWS_SQS_ENDPOINT=$AWS_SQS_ENDPOINT

COPY target/*.jar app.jar

EXPOSE $SERVER_PORT

ENTRYPOINT ["java", "-jar", "app.jar"]